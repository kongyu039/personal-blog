package pvt.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pvt.example.common.utils.SnowFlakeUtil;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.BasicService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 信息：src/java/pvt/example/controller/BasicController.java
 * <p>日期：2025/7/27
 * <p>描述：普通控制器
 */
@RestController
@RequestMapping("/basic")
@Validated
public class BasicController extends BaseController {
    @Resource
    private BasicService basicService;
    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/test")
    public void test() {
        // Test测试接口
    }

    @GetMapping("/app-name")
    public String getAppName() { return appName; }

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
    public ResultVO<String> getIp(HttpServletRequest request) { return successResultVO(request.getRemoteAddr()); }

    @GetMapping("/change-ip_host")
    public ResultVO<Set<String>> changeIpHost(
            @RequestParam @NotBlank @Pattern(regexp = "^(ip|host)$", message = "type为ip/host") String type,
            @RequestParam String content,
            @RequestParam @NotBlank @Pattern(regexp = "^(add|del)$", message = "flag为add/del") String flag) {
        return successResultVO(basicService.changeIpHost(type, content, flag));
    }

    @GetMapping("/snow-id")
    public ResultVO<Long> snowId() { return successResultVO(SnowFlakeUtil.nextId()); }

    @PostMapping("/upload")
    public ResultVO<String> upload(@RequestParam("image") MultipartFile file) { return successResultVO(basicService.upload(file)); }

    @PostMapping("/r2upload")
    public ResultVO<String> r2upload(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) { return successResultVO("error"); } // 前置校验
        return successResultVO(basicService.r2Upload(imageFile));
    }

    @GetMapping("/r2-all_key")
    public List<String> getAllR2KeyURL() { return basicService.getAllR2KeyURL(); }
}
