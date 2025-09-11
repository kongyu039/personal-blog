package pvt.example.service;

import org.springframework.web.multipart.MultipartFile;
import pvt.example.pojo.entity.ImageStorage;

import java.util.List;

/**
 * 信息：src/java/pvt/example/service/ImageService.java
 * <p>日期：2025/9/1
 * <p>描述：
 */
public interface ImageService {
    public String upload(MultipartFile imageFile);

    public String r2Upload(MultipartFile imageFile);

    public List<ImageStorage> getAllImages(Integer flag);

    public Integer deleteImage(Integer id);
}
