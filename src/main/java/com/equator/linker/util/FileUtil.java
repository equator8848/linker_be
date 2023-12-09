package com.equator.linker.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: Equator
 * @Date: 2021/11/26 22:42
 **/
@Slf4j
public class FileUtil {
    /**
     * 获取压缩目标文件地址
     *
     * @param path
     * @return
     */
    public static String getCompressedFile(String path) {
        return String.format("%s.zip", path);
    }

    /**
     * 删除一个文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件|目录
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        }
        return file.delete();
    }

    /**
     * 获取网络文件大小
     */
    public static long getFileLength(String downloadUrl) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(downloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getContentLengthLong();
        } catch (IOException e) {
            return 0L;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 获取文件拓展名
     *
     * @return
     */
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 递归获取文件大小
     *
     * @param file
     * @return
     */
    public static long getFileLength(File file) {
        if (!file.exists()) {
            return 0;
        }
        return FileUtils.sizeOf(file);
    }

    /**
     * 从下载清单中解析出文件大小
     *
     * @param filePath
     * @return
     */
    public static long parseFileSizeFromManifest(String filePath) {
        return parseFileSizeFromManifest(new File(filePath));
    }

    public static long parseFileSizeFromManifest(File file) {
        if (!file.exists()) {
            return Long.MAX_VALUE;
        }
        try {
            List<String> readLines = FileUtils.readLines(file, Charset.forName("UTF-8"));
            String[] metaList = readLines.get(0).split("\t");
            int sizeIdx = 0;
            for (int i = 0; i < metaList.length; i++) {
                if ("size".equals(metaList[i])) {
                    sizeIdx = i;
                    break;
                }
            }
            long totalFileSize = 0;
            for (int i = 1; i < readLines.size(); i++) {
                String line = readLines.get(i);
                String[] dataList = line.split("\t");
                totalFileSize += Long.valueOf(dataList[sizeIdx]);
            }
            return totalFileSize;
        } catch (IOException e) {
            log.error("parseFileSizeFromManifest error {}", file, e);
            return Long.MAX_VALUE;
        }
    }
}
