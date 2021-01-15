package com.cmit.mvne.billing.preparation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.preparation.common.ErrorCodeStatus;
import com.cmit.mvne.billing.preparation.common.ProcessPathProperties;
import com.cmit.mvne.billing.preparation.entity.*;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import com.cmit.mvne.billing.preparation.service.*;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/29
 */
@Slf4j
@Service
public class ReProcessRejectedServiceImpl implements ReProcessRejectedService {

    @Autowired
    private ProcessPathProperties processPathProperties;

    @Autowired
    private CdrErrorService cdrErrorService;
    @Autowired
    private CdrVerifyService cdrVerifyService;
    @Autowired
    private CdrParsingService cdrParsingService;
    @Autowired
    private CdrDupCheckService cdrDupCheckService;

    @Autowired
    private CdrGprsRatingService cdrGprsRatingService;
    @Autowired
    private CdrGprsSettleService cdrGprsSettleService;
    @Autowired
    private CdrSmsRatingService cdrSmsRatingService;
    @Autowired
    private CdrSmsSettleService cdrSmsSettleService;
    @Autowired
    private CdrErrorFileService cdrErrorFileService;


    @Override
    public IPage<RejectedDTO> getRejectedCdrsPage(String errorType, String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable) {
        return cdrErrorService.findRejectedCdrsPage(errorType, filename, errorCode, status, startTime, endTime, pageable);
    }


//    /**
//     * 根据时间查询改时间段内的话单级错单
//     * @param filename 文件名
//     * @param errorCode 错误码
//     * @param status 状态
//     * @param startTime 开始时间
//     * @param endTime 结束时间
//     * @param pageable 分页对象
//     * @return
//     * @throws IOException
//     */
//    @Override
//    public IPage<RejectedDTO> getRejectedFiles(String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable) throws IOException {
//
//        Page<RejectedDTO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true);
//
//        File errorPathFile = new File(processPathProperties.getErrorPath());
//        File[] files = errorPathFile.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                long fileCreateTime = 0L;
//                try {
//                    fileCreateTime = FileUtil.getCreationTime(pathname);
//                } catch (IOException e) {
//                    log.error("Get file:{} creation time failed!", pathname.getName(), e);
//                    return false;
//                }
//
//                return pathname.isFile() && pathname.getName().startsWith("Severe")
//                        && startTime !=null ? fileCreateTime>=startTime : true && endTime != null ? fileCreateTime<=endTime : true;
//            }
//        });
//
//        List<RejectedDTO> rejectedDTOList = new ArrayList<>();
//        for (File file : files) {
//            RejectedDTO rejectedDTO = new RejectedDTO();
//            rejectedDTO.setFilename(file.getName());
//            rejectedDTO.setFileReceiveTime(FileUtil.getLastModifiedTime(file));
//            rejectedDTO.setErrorCreationTime(FileUtil.getCreationTime(file));
//            rejectedDTO.setStatus(ErrorCodeStatus.INITIAL);
//            rejectedDTOList.add(rejectedDTO);
//        }
//
//        page.setRecords(rejectedDTOList.subList((int)page.getPages(), rejectedDTOList.size() > page.getSize() ? (int)page.getSize() : rejectedDTOList.size()));
//        return page;
//    }

    /**
     * 根据时间查询改时间段内的话单级错单
     * @param filename 文件名
     * @param errorCode 错误码
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页对象
     * @return
     * @throws IOException
     */
    @Override
    public IPage<RejectedDTO> getRejectedFilesPage(String errorType, String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable) throws IOException {

        return cdrErrorFileService.findRejectedFilesPage(errorType, filename, errorCode, status, startTime, endTime, pageable);

    }


    /**
     * 重批业务逻辑，重批过程中如果系统中断，需要手动处理查重库中的重复id
     * @param idList
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void reprocessRejectedCdrs(List<Long> idList) {
        Collection<CdrError> cdrErrorList = cdrErrorService.listByIds(idList);

        List<CdrError> reProcessErrorList = new ArrayList<CdrError>();
        List<CdrGprsRating> cdrGprsRatingList = new ArrayList<>();
        List<CdrSmsRating> cdrSmsRatingList = new ArrayList<>();

        List<CdrGprsSettle> cdrGprsSettleList = new ArrayList<>();
        List<CdrSmsSettle> cdrSmsSettleList = new ArrayList<>();

        Set<String> cdrsThisTime = new HashSet<String>(){};

        log.info("Reprocess cdrs: {}", cdrErrorList.size());
        for(CdrError cdrError : cdrErrorList) {
            if (cdrError.getErrorCode().equals("Severe255") || (cdrError.getErrorCode().equals("Severe250")) || (cdrError.getErrorCode().equals("Severe251"))) {
                // 不重处理重单
                log.error("CdrErrors contain dup error: {}", cdrError.getId());
                continue;
            }
            if (cdrError.getStatus().equals(ErrorCodeStatus.SUCCESS)) {
                // 不重处理已经成功处理的
                log.error("CdrErrors have been reprocessed: {}", cdrError.getId());
                continue;
            }
            String[] cdr = cdrParsingService.parseToArray(cdrError.getCdrDetail());

            String verifyResult = cdrVerifyService.verify(cdr, cdrError.getReceiveTime());
            if (!StringUtils.isEmpty(verifyResult)) {
                cdrError.setErrorCode(verifyResult);
                cdrError.setRedoTime(new Date());
                cdrError.setStatus(ErrorCodeStatus.FAIL);
                reProcessErrorList.add(cdrError);
                continue;
            }


            String dupCheckResult = cdrDupCheckService.checkDup(cdr, cdrError.getOriginFile(), DateTimeUtil.strToDate(cdr[6], cdr[13]), cdrsThisTime, cdrError.getLineNumber());
            cdrDupCheckService.deleteRedoLog(cdrError.getOriginFile());
            cdrDupCheckService.deleteDupLog(cdrError.getOriginFile());
            if(!StringUtils.isEmpty(dupCheckResult)) {
                cdrError.setErrorCode(dupCheckResult);
                cdrError.setRedoTime(new Date());
                cdrError.setStatus(ErrorCodeStatus.FAIL);
                reProcessErrorList.add(cdrError);
                continue;
            }



            cdrParsingService.doTransformCdr(cdr, cdrError.getLineNumber().longValue(), cdrError.getOriginFile(), cdrError.getReceiveTime(),
                    cdrGprsRatingList, cdrGprsSettleList, cdrSmsRatingList, cdrSmsSettleList);

            // 重处理后话单不删除
            cdrError.setStatus(ErrorCodeStatus.SUCCESS);
            cdrError.setRedoTime(new Date());
            reProcessErrorList.add(cdrError);

        }

        log.info("Reprocessing successfully cdrs: {}", cdrGprsRatingList.size());
        // 更新
        cdrErrorService.saveOrUpdateBatch(cdrErrorList);
        cdrGprsRatingService.saveBatch(cdrGprsRatingList);
        cdrGprsSettleService.saveBatch(cdrGprsSettleList);
        cdrSmsRatingService.saveBatch(cdrSmsRatingList);
        cdrSmsSettleService.saveBatch(cdrSmsSettleList);
    }

    @Override
    public List<RejectedDTO> getRejectedCdrs(String filename, String errorCode, String status, Long startTime, Long endTime) {
        return cdrErrorService.findRejectedCdrs(filename, errorCode, status, startTime, endTime);
    }

    @Override
    public List<RejectedDTO> getRejectedFiles(String errorType, String filename, String errorCode, String status, Long startTime, Long endTime) {
        return cdrErrorFileService.findRejectedFiles(errorType, filename, errorCode, status, startTime, endTime);

    }

}
