package pvt.example.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pvt.example.common.config.R2Config;
import pvt.example.common.utils.AppUtil;
import pvt.example.mapper.ImageMapper;
import pvt.example.pojo.entity.ImageStorage;
import pvt.example.service.ImageService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 信息：src/java/pvt/example/service/impl/ImageServiceImpl.java
 * <p>日期：2025/9/1
 * <p>描述：
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    @Resource
    private ImageMapper imageMapper;
    @Resource
    protected HttpServletRequest request;

    /** 获取S3Client */
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
        File fileDist = AppUtil.getJarDirectory("upload", "images", objectKey).toFile();
        if (!fileDist.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            fileDist.getParentFile().mkdirs();
        }
        imageFile.transferTo(fileDist); // 本地存储
    }

    @Override
    public List<ImageStorage> getAllImages(Integer flag) {
        List<ImageStorage> imageStorages = imageMapper.selectImageByFlag(flag);
        imageStorages = imageStorages.stream().peek(item -> {
            if (item.getFlag() == 1) {
                item.setKey("https://" + R2Config.CDN_DOMAIN + "/" + item.getKey());
            } else {
                item.setKey(request.getContextPath() + "/" + item.getKey());
            }
        }).collect(Collectors.toList());
        return imageStorages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteImage(Integer id) {
        ImageStorage imageStorage = imageMapper.selectImageById(id);
        if (imageStorage == null) { return null; }
        if (imageStorage.getFlag() == 0) {
            Path path = AppUtil.getJarDirectory("upload", "images", imageStorage.getKey());
            File file = path.toFile();
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        } else {
            S3Client s3Client = this.getS3Client();
            s3Client.deleteObject(DeleteObjectRequest.builder()
                                                     .bucket(R2Config.BUCKET_NAME)
                                                     .key(imageStorage.getKey())
                                                     .build());
        }
        return imageMapper.deleteImageById(id);
    }

    @Override
    public String upload(MultipartFile imageFile) {
        try {
            // 生成唯一文件名（含日期路径 yyyy/MM/dd）
            String toDayPath = AppUtil.nowDayFormatter();
            String fileExt = Objects.requireNonNull(imageFile.getOriginalFilename())
                                    .substring(imageFile.getOriginalFilename().lastIndexOf("."));
            // 保存 key-value 到数据库 或者 同时保存本地图片
            String objectKey = toDayPath + "/" + AppUtil.calculateCheckMD5(imageFile.getInputStream(), true) + fileExt;
            this.localStorage(imageFile, objectKey);
            ImageStorage imageStorage = new ImageStorage();
            imageStorage.setFlag(0);
            imageStorage.setKey(objectKey);
            imageMapper.insertImageStorage(imageStorage);
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
            String toDayPath = AppUtil.nowDayFormatter();
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
            {// 保存到数据库DB
                ImageStorage imageStorage = new ImageStorage();
                imageStorage.setFlag(1);
                imageStorage.setKey(objectKey);
                imageMapper.insertImageStorage(imageStorage);
            }
            return "https://" + R2Config.CDN_DOMAIN + "/" + objectKey;
        } catch (IOException | S3Exception e) {
            logger.error("R2文件上传失败: {}", e.getMessage(), e);
            return null;
        }
    }
}
