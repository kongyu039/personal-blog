package pvt.example.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pvt.example.common.config.HostIpConfig;
import pvt.example.common.config.R2Config;
import pvt.example.common.utils.AppUtil;
import pvt.example.service.BasicService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 信息：src/java/pvt/example/service/impl/BasicServiceImpl.java
 * <p>日期：2025/7/31
 * <p>描述：通用Service服务实现类
 */
@Service
public class BasicServiceImpl implements BasicService {
    private static final Logger logger = LoggerFactory.getLogger(BasicServiceImpl.class);
    @Resource
    private HostIpConfig hostIpConfig;

    private S3Client getS3Client() {
        return S3Client.builder()
                       .endpointOverride(URI.create(R2Config.END_POINT))
                       .credentialsProvider(() -> AwsBasicCredentials.create(R2Config.ACCESS_KEY, R2Config.SECRET_KEY))
                       .region(Region.of(R2Config.REGION))
                       .build();
    }

    /**
     * 本地存储文件方法
     * @param imageFile 图片文件
     * @param objectKey 图片存储key(日期路径+名称)
     * @throws IOException IO异常
     */
    private void localStorage(MultipartFile imageFile, String objectKey) throws IOException {
        File fileDist = Paths.get(AppUtil.getJarDirectory(), "upload/images/", objectKey).toFile();
        if (!fileDist.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            fileDist.getParentFile().mkdirs();
        }
        imageFile.transferTo(fileDist); // 本地存储
    }

    /**
     * 修改白名单Ip和Host
     * @param type 类型 ip/host
     * @param content 内容
     * @param flag 标记 add/del
     * @return 白名单Set集合
     */
    @Override
    public Set<String> changeIpHost(String type, String content, String flag) {
        if (content.isEmpty()) { return "ip".equalsIgnoreCase(type) ? hostIpConfig.getAllowedIPs() : hostIpConfig.getAllowedHosts(); }
        if ("ip".equalsIgnoreCase(type)) {
            if ("add".equalsIgnoreCase(flag)) {
                hostIpConfig.addIp(content);
            } else {
                hostIpConfig.removeIp(content);
            }
            return hostIpConfig.getAllowedIPs();
        } else {
            if ("add".equalsIgnoreCase(flag)) {
                hostIpConfig.addHost(content);
            } else {
                hostIpConfig.removeHost(content);
            }
            return hostIpConfig.getAllowedHosts();
        }
    }

    @Override
    public String upload(MultipartFile imageFile) {
        // TODO 本地的图片需要存储到数据库内(方便删除,可以有post_id,也可以没有)
        try {
            // 生成唯一文件名（含日期路径 yyyy/MM/dd）
            String toDayPath = AppUtil.toDayFormatter();
            String fileExt = Objects.requireNonNull(imageFile.getOriginalFilename())
                                    .substring(imageFile.getOriginalFilename().lastIndexOf("."));
            // 保存 key-value 到数据库 或者 同时保存本地图片
            String objectKey = toDayPath + "/" + AppUtil.calculateCheckMD5(imageFile.getInputStream(), true) + fileExt;
            this.localStorage(imageFile, objectKey);
            return objectKey;
        } catch (IOException e) {
            logger.error("文件上传本地失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * AWS R2 Upload上传
     * @param imageFile 图片文件
     * @return 生成可访问URL（含CDN域名）
     */
    @Override
    public String r2Upload(MultipartFile imageFile) {
        try (InputStream imageInputStream = imageFile.getInputStream();
             S3Client s3Client = this.getS3Client()) {
            // 生成唯一文件名（含日期路径 yyyy/MM/dd）
            String toDayPath = AppUtil.toDayFormatter();
            String fileExt = Objects.requireNonNull(imageFile.getOriginalFilename())
                                    .substring(imageFile.getOriginalFilename().lastIndexOf("."));
            // 保存项目的 唯一key
            String objectKey = toDayPath + "/" + AppUtil.calculateCheckMD5(imageFile.getInputStream(), true) + fileExt;
            PutObjectRequest putRequest = PutObjectRequest.builder() // 构建上传请求
                                                          .bucket(R2Config.BUCKET_NAME)
                                                          .key(objectKey)
                                                          .contentType(imageFile.getContentType())
                                                          .build();
            s3Client.putObject(putRequest, RequestBody.fromInputStream(imageInputStream, imageFile.getSize()));// 执行上传
            return "https://" + R2Config.CDN_DOMAIN + "/" + objectKey;
        } catch (IOException | S3Exception e) {
            logger.error("R2文件上传失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取R2对象Key的访问URL
     * @param objectKey 对象键
     * @return 对象的访问URL
     */
    @Override
    public String getR2KeyURL(String objectKey) {
        S3Client s3Client = this.getS3Client();
        // 使用ListObjectsV2来查找包含该文件名的对象
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                                                           .bucket(R2Config.BUCKET_NAME)
                                                           .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        // 遍历所有对象，查找匹配的文件
        for (S3Object object : response.contents()) {
            if (object.key().endsWith(objectKey)) { return R2Config.CDN_DOMAIN + "/" + object.key(); } // 找到匹配的对象，返回完整路径
        }
        return null;
    }

    /**
     * 获取R2所有对象Key的访问URL
     * @return 对象的访问URL
     */
    @Override
    public List<String> getAllR2KeyURL() {
        S3Client s3Client = this.getS3Client();
        // 使用ListObjectsV2来查找包含该文件名的对象
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                                                           .bucket(R2Config.BUCKET_NAME)
                                                           .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        return response.contents().stream().map(item -> R2Config.CDN_DOMAIN + "/" + item.key()).collect(Collectors.toList());
    }

    /**
     * 通过objectKey删除R2存储文件
     * @param objectKey 项目唯一键
     */
    @Override
    public void deleteR2ByKey(String objectKey){
        S3Client s3Client = this.getS3Client();
        s3Client.deleteObject(DeleteObjectRequest.builder()
                                                 .bucket(R2Config.BUCKET_NAME)
                                                 .key(objectKey)
                                                 .build());
    }
}
