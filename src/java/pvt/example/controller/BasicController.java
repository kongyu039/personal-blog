package pvt.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pvt.example.common.config.GlobalVariable;
import pvt.example.common.utils.SnowFlakeUtil;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.BasicAsyncService;
import pvt.example.service.BasicService;
import pvt.example.service2.BasicService2;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Resource
    private BasicAsyncService basicAsyncService;
    @Resource
    private BasicService2 basicService2;
    @Resource
    private GlobalVariable globalVariable;
    @Value("${spring.application.name}")
    private String appName;

    /** 统计 post category tag 数量 */
    @GetMapping("/count-total")
    public ResultVO<Map<String, Integer>> countTotalAll() { return successResultVO(basicService.countTotalAll()); }

    /**
     * 统计每年每天的文章post数量count
     * @param year 年份
     */
    @GetMapping("/count-post-day")
    public ResultVO<List<Map<String, Integer>>> countPostDayAll(String year) {
        return successResultVO(basicService.countDayCalculate(year));
    }

    /** 统计 category和tag 数量count */
    @GetMapping("/count-category-tag")
    public ResultVO<Map<String, List<Map<String, Integer>>>> countCategoryTagAll() {
        return successResultVO(basicService.countCategoryTag());
    }

    /** 获取ip和host */
    @GetMapping("/get-ip_host")
    public ResultVO<Map<String, String>> getIpHost(HttpServletRequest request) {
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("ip", request.getRemoteAddr());
        stringStringHashMap.put("host", request.getServerName());
        stringStringHashMap.putAll(basicService.getIpHost());
        return successResultVO(stringStringHashMap);
    }

    @GetMapping("/change-ip_host")
    public ResultVO<Set<String>> changeIpHost(
            @RequestParam @NotBlank @Pattern(regexp = "^(ip|host)$", message = "type为ip/host") String type,
            @RequestParam @NotBlank @Pattern(regexp = "^(add|del)$", message = "flag为add/del") String flag, @RequestParam String content) {
        return successResultVO(basicService.changeIpHost(type, content, flag));
    }

    /** 获取系统信息 */
    @GetMapping("/system-info")
    public ResultVO<Map<String, Map<String, String>>> getSystemInfo() { return successResultVO(basicService.getSystemInfo()); }

    /** 获取雪花ID */
    @GetMapping("/snow-id")
    public ResultVO<Long> snowId() { return successResultVO(SnowFlakeUtil.nextId()); }

    /** 获取应用名称(用于判断配置文件生效) */
    @GetMapping("/app-name")
    public String getAppName() { return appName; }

    /** 获取contextPath路径 jsonp */
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

    /** 清除打包目录 */
    @GetMapping("/btn-clean")
    public ResultVO<Boolean> btnClean() { return successResultVO(basicService2.cleanDir()); }

    @GetMapping("/btn-push")
    public ResultVO<String> btnPushEvent() {
        // 采用 生成目录+git push方式; 两个方法; 最后再调用一个cleanDir方法
        if (globalVariable.getGenFileFlag()) { return successResultVO("正在处理中..."); }
        globalVariable.setGenFileFlag(true);
        // 生成目录
        String flagStr = basicService2.genFileDir();
        if (flagStr != null) {
            globalVariable.setGenFileFlag(false);
            return successResultVO(flagStr);
        }
        // 目录git push
        basicService2.gitPush();
        return successResultVO("test");
    }

    /** 非异步的文件下载(错误则json返回) */
    @GetMapping("/btn-download")
    public ResponseEntity<?> btnDownloadEvent() {
        // 采用 生成目录+zip下载 ; 两个方法; 最后再调用一个cleanDir方法
        if (globalVariable.getGenFileFlag()) { return ResponseEntity.ok(successResultVO("正在处理中...")); }
        globalVariable.setGenFileFlag(true);
        // 生成目录
        String flagStr = basicService2.genFileDir();
        if (flagStr != null) {
            globalVariable.setGenFileFlag(false);
            return ResponseEntity.ok(successResultVO(flagStr));
        }
        // 目录zip打包
        byte[] zipBytes = basicService2.zipFileDir();
        globalVariable.setGenFileFlag(false);
        if (zipBytes == null) { return ResponseEntity.ok(successResultVO("错误...")); }
        HttpHeaders headers = new HttpHeaders();// 设置响应头
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"frontend.zip\"");
        return ResponseEntity.ok().headers(headers).contentLength(zipBytes.length)
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .body(new InputStreamResource(new ByteArrayInputStream(zipBytes)));
    }
}
