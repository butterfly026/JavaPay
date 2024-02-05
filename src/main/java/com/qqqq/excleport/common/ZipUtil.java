package com.qqqq.excleport.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩成zip文件
 *
 * @author nb
 */
public class ZipUtil {
    /**
     * 字节数
     */
    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 将list中的文件压缩成Zip文件
     *
     * @param folder
     * @param zipFilePath
     * @throws IOException
     */
    public static void folderToZip(List<String> filePathList, String zipFilePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(new File(zipFilePath));
        ZipOutputStream zos = null;
        FileInputStream fis = null;
        try {
            zos = new ZipOutputStream(fos);
            for (String filePath : filePathList) {
                File file = new File(filePath);
                if (!file.exists()) {
                    continue;
                }
                byte[] b = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(file.getName()));
                int len;
                fis = new FileInputStream(file);
                while ((len = fis.read(b)) != -1) {
                    zos.write(b, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
        } finally {
//			System.out.println("关闭流");
            if (zos != null) zos.close();
            if (fis != null) fis.close();
        }
    }
}
