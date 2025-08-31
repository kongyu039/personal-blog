package pvt.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * 信息：src/java/pvt/example/service/BasicService.java
 * <p>日期：2025/7/31
 * <p>描述：
 */
public interface BasicService {
    public Set<String> changeIpHost(String type, String content, String flag);

    public String upload(MultipartFile imageFile);

    public String r2Upload(MultipartFile imageFile);

    public String getR2KeyURL(String objectKey);

    public List<String> getAllR2KeyURL();

    void deleteR2ByKey(String objectKey);
}
