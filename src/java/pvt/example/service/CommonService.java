package pvt.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * 信息：src/java/pvt/example/service/CommonService.java
 * <p>日期：2025/7/31
 * <p>描述：
 */
public interface CommonService {
    public Set<String> changeIpHost(String type, String ipHost, String flag);

    public String uploadToR2(MultipartFile imageFile);
}
