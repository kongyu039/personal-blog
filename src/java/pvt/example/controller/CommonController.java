package pvt.example.controller;

import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pvt.example.common.config.HostIpConfig;
import pvt.example.common.utils.SnowFlakeUtil;
import pvt.example.pojo.vo.ResultVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * 信息：src/java/pvt/example/controller/CommonController.java
 * <p>日期：2025/7/27
 * <p>描述：普通控制器
 */
@RestController
@RequestMapping("/common")
@Validated
public class CommonController extends BaseController {
    @Resource
    private HostIpConfig hostIpConfig;

    @GetMapping("/get-ip")
    private ResultVO<String> getIp(HttpServletRequest request) { return successResultVO(request.getRemoteAddr()); }

    @GetMapping("/change-ip")
    private ResultVO<Set<String>> changeIp(@RequestParam @NotBlank String ip
            , @RequestParam @NotBlank @Pattern(regexp = "^(add|del)$", message = "flag为'add'或'del'") String flag) {
        if ("add".equalsIgnoreCase(flag)) {
            hostIpConfig.addIp(ip);
        } else {
            hostIpConfig.removeIp(ip);
        }
        return successResultVO(hostIpConfig.getAllowedIPs());
    }

    @GetMapping("/change-host")
    private ResultVO<Set<String>> changeHost(@RequestParam @NotBlank String host
            , @RequestParam @NotBlank @Pattern(regexp = "^(add|del)$", message = "flag为'add'或'del'") String flag) {
        if ("add".equalsIgnoreCase(flag)) {
            hostIpConfig.addHost(host);
        } else {
            hostIpConfig.removeHost(host);
        }
        return successResultVO(hostIpConfig.getAllowedIPs());
    }

    @GetMapping("/snow-id")
    private ResultVO<Long> snowId() { return successResultVO(SnowFlakeUtil.getID()); }

    @PostMapping("/upload")
    private ResultVO<String> upload(@RequestParam("image") MultipartFile imageFile) {
        try {
            // MD5 值
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(imageFile.getBytes()).toUpperCase();
            // 文件后缀 .xxx
            String fileSuffix = Objects.requireNonNull(imageFile.getOriginalFilename())
                                       .substring(imageFile.getOriginalFilename().lastIndexOf("."));
            // 拼接的文件名称
            String fileName = md5DigestAsHex + fileSuffix;
            // // 拼接日期的文件路径+名称
            // File fileDist = Paths.get(AppUtil.getJarDirectory(), "upload/images", AppUtil.currentDatePath(), fileName).toFile();
            // if (!fileDist.getParentFile().exists()) {
            //     //noinspection ResultOfMethodCallIgnored
            //     fileDist.getParentFile().mkdirs();
            // }
            // imageFile.transferTo(fileDist);
            // return successResultVO(fileName);
            return successResultVO("/static/images/2025/08/14/C17EC44320C27A2E2022628BA701C35A.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
