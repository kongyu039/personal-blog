package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pvt.example.common.config.HostIpConfig;
import pvt.example.service.CommonService;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 信息：src/java/pvt/example/service/impl/CommonServiceImpl.java
 * <p>日期：2025/7/31
 * <p>描述：通用Service服务实现类
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private HostIpConfig hostIpConfig;

    @Override
    public Set<String> changeIpHost(String type, String ipHost, String flag) {
        if ("ip".equalsIgnoreCase(type)) {
            if ("add".equalsIgnoreCase(flag)) {
                hostIpConfig.addIp(ipHost);
            } else {
                hostIpConfig.removeIp(ipHost);
            }
            return hostIpConfig.getAllowedIPs();
        } else {
            if ("add".equalsIgnoreCase(flag)) {
                hostIpConfig.addIp(ipHost);
            } else {
                hostIpConfig.removeIp(ipHost);
            }
            return hostIpConfig.getAllowedHosts();
        }
    }

    @Override
    public String uploadToR2(MultipartFile imageFile) {
        // TODO AWS R2 Upload上传 最好可以直接 file 传到 r2 存储中;而不需要本地缓存(缓存也是没问题的,要搞一个缓存表,id,远程连接,本地文件MD5随机)
        // TODO DigestUtils.md5DigestAsHex(imageFile.getBytes()).toUpperCase();
        // TODO 如果MD5相同或文件存在,那么直接返回旧的URL地址就行了
        // https://blog.csdn.net/weixin_43797741/article/details/148211042
        // https://blog.51cto.com/u_16213397/12251082
        // https://search.bilibili.com/all?keyword=%E5%9C%A8springboot%E9%9B%86%E6%88%90Cloudflare+R2%E5%AF%B9%E8%B1%A1%E5%AD%98%E5%82%A8
        // https://juejin.cn/post/7507471067670904858#heading-20
        // https://blog.csdn.net/weixin_61960336/article/details/148208649
        // https://x.javaer.me/article/CloudflareR2_fileUpload
        // https://icewolf-li.github.io/posts/Cloudflare%20R2.html
        // https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2
        return null;
        datetime
    }
}
