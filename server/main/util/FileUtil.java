package main.util;

import main.config.FileIOConfiguration;

import java.io.*;
import java.util.UUID;

public class FileUtil {
    public static String upload(File file) {
        String name = file.getName();
        String filename = UUID.randomUUID() + name.substring(name.lastIndexOf('.'));

        String url = "";
        url = FileIOConfiguration.TARGET_DST+"\\"+filename;

        try (FileInputStream fis = new FileInputStream(file);
             FileOutputStream fos = new FileOutputStream(url)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            return url;

        } catch (IOException e) {
            // 捕获并打印IO异常
            e.printStackTrace();
            // 文件上传失败
            return null;
        }
    }

    /**
     * 从服务器存储下载文件到本地目录
     * @param sourceUrl 文件在服务器上的完整存储路径
     * @param targetDir 下载到本地的目标目录
     * @param originalFileName 文件下载后希望使用的原始文件名
     * @return 下载成功返回true，失败返回false
     */
    public static boolean download(String sourceUrl, String targetDir, String originalFileName) {
        File sourceFile = new File(sourceUrl);
        if (!sourceFile.exists()) {
            System.err.println("下载失败：源文件不存在 " + sourceUrl);
            return false;
        }

        // 确保目标目录存在
        File targetDirectory = new File(targetDir);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs(); // 如果目录不存在，则创建它
        }

        // 构建目标文件的完整路径，使用传入的原始文件名
        File destFile = new File(targetDirectory, originalFileName);

        // 使用 try-with-resources 确保流被自动关闭
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {

            // 创建缓冲区
            byte[] buffer = new byte[1024];
            int bytesRead;

            // 循环读取源文件并写入目标文件
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            // 文件下载成功
            return true;

        } catch (IOException e) {
            // 捕获并打印IO异常
            e.printStackTrace();
            // 文件下载失败
            return false;
        }
    }

    /**
     * 根据提供的文件路径删除文件
     * @param fileUrl 要删除的文件的完整路径
     * @return 删除成功返回true，文件不存在或删除失败返回false
     */
    public static boolean delete(String fileUrl) {
        File fileToDelete = new File(fileUrl);

        if (!fileToDelete.exists()) {
            System.err.println("删除失败：文件不存在 " + fileUrl);
            return false;
        }

        if (fileToDelete.delete()) {
            // 文件删除成功
            return true;
        } else {
            // 文件删除失败
            System.err.println("删除失败：无法删除文件 " + fileUrl);
            return false;
        }
    }
}
