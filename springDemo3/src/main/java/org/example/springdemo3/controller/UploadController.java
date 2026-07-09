package org.example.springdemo3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.springdemo3.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "文件上传", description = "通用文件上传接口")
public class UploadController {

    /** 上传文件存储目录（相对于项目根目录） */
    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择文件");
        }

        // 获取原始文件名后缀
        String originalName = file.getOriginalFilename();
        String suffix = "";
        if (originalName != null && originalName.contains(".")) {
            suffix = originalName.substring(originalName.lastIndexOf("."));
        }

        // 生成唯一文件名，防止覆盖
        String filename = UUID.randomUUID().toString() + suffix;

        try {
            // 确保目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            // 返回可访问的 URL
            String url = "/uploads/" + filename;
            log.info("文件上传成功: {}", url);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
