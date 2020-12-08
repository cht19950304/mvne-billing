package com.cmit.mvne.billing.preparation.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Collection;

/**
 * 文件处理工具类
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/1/6
 */
@Slf4j
public class FileUtil {

    public static void writeToFile(File destFile, final Collection<?> lines) throws IOException {

//        createParentDirs(destFile);
        FileUtils.writeLines(destFile, "utf8", lines);

        log.info("write to file {} success!", destFile.getAbsolutePath());

    }

    public static void renameFile(File srcFile, File destFile) throws IOException {

//        createParentDirs(destFile);
        FileUtils.moveFile(srcFile, destFile);

        log.info("rename srcfile {} to destfile {} success!", srcFile.getAbsolutePath(), destFile.getAbsolutePath());

    }

    /**
     * 挪文件
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @param isForce 是否删除目标文件
     * @throws IOException
     */
    public static void moveFile(File srcFile, File destFile, boolean isForce) throws IOException {
        if(isForce) {
            destFile.delete();
        }
        FileUtils.moveFile(srcFile, destFile);

        log.info("move srcfile {} to destfile {} success!", srcFile.getAbsolutePath(), destFile.getAbsolutePath());

    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
//        createParentDirs(destFile);
        FileUtils.copyFile(srcFile, destFile);

        log.info("copy srcfile {} to destfile {} success!", srcFile.getAbsolutePath(), destFile.getAbsolutePath());

    }

    public static long getCreationTime(File file) throws IOException {
        Path path = file.toPath();

        BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

        return basicFileAttributes.creationTime().toMillis();
    }


    public static long getLastModifiedTime(File file) throws IOException {
        Path path = file.toPath();

        BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

        return basicFileAttributes.lastModifiedTime().toMillis();
    }

    public static long getLastAccessTime(File file) throws IOException {
        Path path = file.toPath();

        BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

        return basicFileAttributes.lastAccessTime().toMillis();
    }


//    private static void createParentDirs(File file) throws IOException {
//        if (file == null) {
//            throw new NullPointerException();
//        }
//        File parent = file.getCanonicalFile().getParentFile();
//        if (parent == null) {
//            return;
//        }
//        parent.mkdirs();
//        if (!parent.isDirectory()) {
//            throw new IOException("Unable to create parent directories of " + file);
//        }
//    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\workspace\\cmit_projects\\mvne-billing\\process\\error\\Fatal100S4SVRO000828");
        System.out.println(DateTimeUtil.getDateofTimestamp(getCreationTime(file)));
        System.out.println(DateTimeUtil.getDateofTimestamp(getLastModifiedTime(file)));
        System.out.println(DateTimeUtil.getDateofTimestamp(file.lastModified()));
        System.out.println(DateTimeUtil.getDateofTimestamp(getLastAccessTime(file)));
    }
}
