package pvt.example.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 信息：src/java/pvt/example/common/config/R2Config.java
 * <p>日期：2025/8/24
 * <p>描述：
 */
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class R2Config {
    /** R2操作端点 */
    public static String END_POINT;
    /** R2区域 */
    public static String REGION;
    /** R2桶名称 */
    public static String BUCKET_NAME;
    /** R2自定义域名 */
    public static String CDN_DOMAIN;
    /** API访问密钥 */
    public static String ACCESS_KEY;
    /** API机密访问密钥 */
    public static String SECRET_KEY;
    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;
    @Value("${cloud.aws.s3.region}")
    private String region;
    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;
    @Value("${cloud.aws.s3.cdn-domain}")
    private String cdnDomain;
    @Value("${cloud.aws.s3.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.s3.credentials.secret-key}")
    private String secretKey;

    @PostConstruct
    private void init() {
        R2Config.END_POINT = this.endpoint;
        R2Config.REGION = this.region;
        R2Config.BUCKET_NAME = this.bucketName;
        R2Config.CDN_DOMAIN = this.cdnDomain;
        R2Config.ACCESS_KEY = this.accessKey;
        R2Config.SECRET_KEY = this.secretKey;
    }
}
