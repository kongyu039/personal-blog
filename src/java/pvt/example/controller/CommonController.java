package pvt.example.controller;

import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pvt.example.common.utils.SnowFlakeUtil;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.CommonService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private CommonService commonService;


    @GetMapping("/get-path")
    public void getContextPath(@RequestParam(name = "callback", defaultValue = "contextPath", required = false) String callback,
                               HttpServletRequest request, HttpServletResponse response) {
        String contextPath = request.getContextPath();
        String jsCode = String.format("function %s(url){return '%s/'+url;};", callback, contextPath);
        response.setContentType("application/javascript;charset=UTF-8");
        try {
            response.getWriter().write(jsCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-ip")
    private ResultVO<String> getIp(HttpServletRequest request) { return successResultVO(request.getRemoteAddr()); }

    @GetMapping("/change-ip_host")
    private ResultVO<Set<String>> changeIpHost(@RequestParam @NotBlank String type, @RequestParam @NotBlank String ipHost
            , @RequestParam @NotBlank @Pattern(regexp = "^(add|del)$", message = "flag为'add/del'") String flag) {
        return successResultVO(commonService.changeIpHost(type,ipHost,flag));
    }

    @GetMapping("/snow-id")
    private ResultVO<Long> snowId() { return successResultVO(SnowFlakeUtil.nextId()); }

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

    @PostMapping("/r2upload")
    private ResultVO<String> r2upload(@RequestParam("image") MultipartFile imageFile) {
        // 前置校验
        if (imageFile.isEmpty()) { return successResultVO("error"); }
        // 调用服务层
        return successResultVO(commonService.uploadToR2(imageFile));
    }
}
