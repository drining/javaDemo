package org.example.springdemo3.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.springdemo3.config.CosConfig;
import org.example.springdemo3.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "文件上传", description = "上传文件（支持 COS 和本地回退）")
public class UploadController {

    /** 本地存储目录（兜底用） */
    private static final String UPLOAD_DIR = "uploads";

    @Autowired(required = false)
    private COSClient cosClient;

    @Autowired
    private CosConfig cosConfig;

    @PostMapping("/upload")
    @Operation(summary = "上传文件（图片等）")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择文件");
        }

        // 获取文件后缀
        String originalName = file.getOriginalFilename();
        String suffix = "";
        if (originalName != null && originalName.contains(".")) {
            suffix = originalName.substring(originalName.lastIndexOf("."));
        }

        // 生成唯一文件名
        String filename = UUID.randomUUID() + suffix;

        // 如果 COS 已配置，上传到腾讯云 COS；否则存本地
        if (cosClient != null) {
            return uploadToCos(file, filename);
        } else {
            return uploadToLocal(file, filename);
        }
    }

    /** 上传到腾讯云 COS */
    private Result<String> uploadToCos(MultipartFile file, String key) {
        try {
            PutObjectRequest putRequest = new PutObjectRequest(
                    cosConfig.getBucket(), "uploads/" + key, file.getInputStream(), null);
            PutObjectResult putResult = cosClient.putObject(putRequest);

            String url = "https://" + cosConfig.getBucket() + ".cos."
                    + cosConfig.getRegion() + ".myqcloud.com/uploads/" + key;

            log.info("COS 上传成功: ETag={}, URL={}", putResult.getETag(), url);
            return Result.success(url);
        } catch (IOException e) {
            log.error("COS 上传失败", e);
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    /** 兜底：存到本地 uploads/ 目录 */
    private Result<String> uploadToLocal(MultipartFile file, String filename) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            String url = "/uploads/" + filename;
            log.info("本地存储成功: {}", url);
            return Result.success(url);
        } catch (IOException e) {
            log.error("本地存储失败", e);
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
