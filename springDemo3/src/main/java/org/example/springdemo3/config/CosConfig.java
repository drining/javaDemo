package org.example.springdemo3.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cos")
public class CosConfig {

    /** 腾讯云 API 密钥 SecretId */
    private String secretId;

    /** 腾讯云 API 密钥 SecretKey */
    private String secretKey;

    /** 存储桶所在地域，如 ap-guangzhou */
    private String region;

    /** 存储桶名称，如 oss-1310209398 */
    private String bucket;

    /**
     * 只有当 secret-id 和 secret-key 都不为空时才创建 COSClient Bean
     */
    @Bean
    @ConditionalOnProperty(name = {"cos.secret-id", "cos.secret-key"}, matchIfMissing = false)
    public COSClient cosClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }
}
