package com.cmit.mvne.billing.preparation.handler;

import com.cmit.mvne.billing.preparation.common.ErrorCodeStatus;
import com.cmit.mvne.billing.preparation.common.ProcessPathProperties;
import com.cmit.mvne.billing.preparation.dao.CdrErrorFileMapper;
import com.cmit.mvne.billing.preparation.entity.CdrErrorFile;
import com.cmit.mvne.billing.preparation.entity.CdrFile;
import com.cmit.mvne.billing.preparation.service.CdrDupCheckService;
import com.cmit.mvne.billing.preparation.service.FileVerifyService;
import com.cmit.mvne.billing.preparation.service.ProcessService;
import com.cmit.mvne.billing.preparation.service.impl.CdrDupCheckServiceImpl;
import com.cmit.mvne.billing.preparation.service.impl.FileVerifyServiceImpl;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import com.cmit.mvne.billing.preparation.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Tap3 解码 handler 从ftp获取文件后对文件进行解码
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2019/6/18
 */
@Slf4j
public class ProccessHandler {

    @Autowired
    private ProcessPathProperties pathProperties;
    @Autowired
    private ProcessService processService;
    @Autowired
    private CdrDupCheckService dupCheckService;
    @Autowired
    private FileVerifyService fileVerifyService;
    @Autowired
    private CdrErrorFileMapper cdrErrorFileMapper;

    /**
     * 预处理，对工作目录下的文件进行清理
     */
    @PostConstruct
    public void init() throws IOException {

        // 初始化目录
        initDirs();

        File workDir = new File(pathProperties.getWorkPath());
        log.info("Scan work directory {} " , workDir);
        // 扫描过滤出.dealing后缀的文件，null表示不递归目录，递归为DirectoryFileFilter.INSTANCE
        Collection<File> files = FileUtils.listFiles(workDir, FileFilterUtils.suffixFileFilter(".dealing"), null);
        for(File file : files) {
            log.info("Redoing file : {}, Deleting inserted duplicate values and move file to process directory! ", file.getName());
            String filename = file.getName();
            String originFilename = filename.split("\\.")[0];

            if(dupCheckService.existRedoLog(originFilename)) {
                // 清除因为异常中断 插入查重库中的查重键，以便重处理

                //dupCheckService.cleanDupValue(originFilename);
                File originFile = new File(pathProperties.getInputPath() + originFilename);
                FileUtils.moveFile(file, originFile);
                log.info("delete duplicate values and move file {} to {} success!", file.getAbsolutePath(), pathProperties.getInputPath());
            } else {
                // redo Log 已删除，说明入库已成功将文件挪至备份目录
                File backupFile = new File(pathProperties.getBackupPath() +  originFilename);
                FileUtils.moveFile(file, backupFile);
                log.info("move file {} to backup path {} success!", file.getAbsolutePath(), pathProperties.getBackupPath());
            }


        }
    }

    //@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public File handleFile(File srcFile) throws IOException {

        File workFile = null;

        if(srcFile.isFile()) {

            log.info("Load file from {}, filename:{}, download time :{}, File time: {}, file size:{}",
                    pathProperties.getInputPath(),
                    srcFile.getName(),
                    LocalDateTime.now(),
                    DateTimeUtil.getDateofTimestamp(srcFile.lastModified()),
                    srcFile.length());

            workFile = new File(pathProperties.getWorkPath() +  srcFile.getName() + ".dealing" );
            // 移动文件到工作目录
            FileUtils.moveFile(srcFile, workFile);
            log.info("Move file {} to work directory {} success!", srcFile.getAbsolutePath(), pathProperties.getWorkPath());

            // 文件级校验
            log.info("File-level verification, checking file: {} ", srcFile.getAbsolutePath());
            String result = fileVerifyService.verifyFile(workFile);
            if (!StringUtils.isEmpty(result)) {
                File errorFile = new File(pathProperties.getErrorPath() + result +  srcFile.getName());
                FileUtils.moveFile(workFile, errorFile);

                CdrErrorFile cdrErrorFile = new CdrErrorFile();
                cdrErrorFile.setFilePath(errorFile.getAbsolutePath());
                cdrErrorFile.setErrorCode(result);
                cdrErrorFile.setOriginFile(srcFile.getName());
                cdrErrorFile.setReceiveTime(DateTimeUtil.getDateofTimestamp(errorFile.lastModified()));
                cdrErrorFile.setStatus(ErrorCodeStatus.INITIAL);
                cdrErrorFileMapper.insert(cdrErrorFile);

                return null;
            }

            log.info("Create redo log: {}", srcFile.getName() + ".redo");
            // 创建查重数据 重写日志
            dupCheckService.createRedoLog(srcFile.getName());
            CdrFile cdrFile = new
                    CdrFile(srcFile, workFile);
            processService.process(cdrFile);


        }

        return workFile;

    }

    public Message<?> handleErrorMessage(MessagingException messageException) {

        log.error("file {} handle throws exception, exception stacktrace :", messageException.getFailedMessage().getHeaders().get(FileHeaders.FILENAME), messageException);

        File originFile = (File)messageException.getFailedMessage().getPayload();
        File workFile = getWorkFile(originFile);

        // 移除文件查重建，方便重处理
        //fileVerifyService.deleteFileDupKey(originFile.getName());
        // 移除查重键，方便重处理
        //dupCheckService.cleanDupValue(originFile.getName());
        //dupCheckService.deleteRedoLog(originFile.getName());

        File errorFile = new File(pathProperties.getErrorPath() + originFile.getName() + ".failed");
        try {
            FileUtils.moveFile(workFile, errorFile);
        } catch (IOException e) {
            log.error("move file to error path failed！", e);
        }

        return null;

    }

    private File getWorkFile(File originFile) {
        return new File(pathProperties.getWorkPath() +  originFile.getName() + ".dealing" );
    }

    private void initDirs() {
        // 初始化目录
        File processWorkPath = new File(pathProperties.getWorkPath());
        if(!processWorkPath.exists()) {
            processWorkPath.mkdirs();
        }
        File processSuccessPath = new File(pathProperties.getSuccessPath());
        if(!processSuccessPath.exists()) {
            processSuccessPath.mkdirs();
        }
        File processErrorPath = new File(pathProperties.getErrorPath());
        if(!processErrorPath.exists()) {
            processErrorPath.mkdirs();
        }
        File processBackupPath = new File(pathProperties.getBackupPath());
        if(!processBackupPath.exists()) {
            processBackupPath.mkdirs();
        }

    }

}
