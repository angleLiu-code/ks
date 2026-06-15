package com.university.repair.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传工具类
 */
@Slf4j
@Component
public class FileUploadUtils {

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    @Value("${file.max-file-size:10485760}")
    private Long maxFileSize;

    @Value("${file.allowed-extensions:jpg,jpeg,png,gif,pdf,doc,docx}")
    private String allowedExtensions;

    private static String UPLOAD_PATH = "./uploads";
    private static String ALLOWED_EXTENSIONS = "jpg,jpeg,png,gif,pdf,doc,docx";

    static {
        // 确保上传目录存在
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件相对路径
     */
    public static String uploadFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("文件为空");
            }

            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("文件名为空");
            }

            // 检查文件扩展名
            String fileExtension = getFileExtension(originalFilename);
            if (!isAllowedExtension(fileExtension)) {
                throw new RuntimeException("不支持的文件类型: " + fileExtension);
            }

            // 生成新的文件名
            String newFileName = generateFileName(fileExtension);

            // 创建日期目录
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            File dateDir = new File(UPLOAD_PATH + File.separator + datePath);
            if (!dateDir.exists()) {
                dateDir.mkdirs();
            }

            // 保存文件
            String filePath = datePath + "/" + newFileName;
            File targetFile = new File(UPLOAD_PATH + File.separator + filePath);
            file.transferTo(targetFile);

            log.info("文件上传成功: {}", filePath);
            return "/uploads/" + filePath;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        try {
            if (filePath == null || filePath.isEmpty()) {
                return false;
            }

            // 移除路径前缀
            String actualPath = filePath;
            if (filePath.startsWith("/uploads/")) {
                actualPath = filePath.substring(9);
            }

            File file = new File(UPLOAD_PATH + File.separator + actualPath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    log.info("文件删除成功: {}", filePath);
                }
                return deleted;
            }
            return false;
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return false;
        }
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 检查文件扩展名是否允许
     */
    private static boolean isAllowedExtension(String extension) {
        String[] allowedExts = ALLOWED_EXTENSIONS.split(",");
        for (String ext : allowedExts) {
            if (ext.trim().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成文件名
     */
    private static String generateFileName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }
}
