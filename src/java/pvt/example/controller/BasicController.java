package pvt.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pvt.example.common.config.GlobalVariable;
import pvt.example.common.utils.FreemarkerUtil;
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

    @GetMapping("/system-info")
    public ResultVO<Map<String, Map<String, String>>> getSystemInfo() { return successResultVO(basicService.getSystemInfo()); }

    @GetMapping("/snow-id")
    public ResultVO<Long> snowId() { return successResultVO(SnowFlakeUtil.nextId()); }

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

    @GetMapping("/btn-clean")
    public ResultVO<Boolean> btnClean() { return successResultVO(basicService2.cleanDir()); }

    // TODO 注意异步响应事件
    @GetMapping("/btn-push")
    public ResultVO<String> btnPushEvent() {
        return successResultVO("test");
    }

    @GetMapping("/btn-download")
    public ResultVO<String> btnDownloadEvent() {
        if (globalVariable.getGenFileFlag()) { return successResultVO("正在处理中..."); }
        globalVariable.setGenFileFlag(true);
        {// 异步执行
            if (!basicService2.cleanDir()) { return successResultVO("清除失败"); }
            if (!basicService2.createDir()) { return successResultVO("初始化目录失败"); }
            if (!basicService2.handlerDataBase()) { return successResultVO("表初始化失败"); }
            if (!basicService2.handlerCopyTmpls()) { return successResultVO("Tmpls复制失败"); }
            if (!basicService2.handlerTmpls()) { return successResultVO("模版填充复制失败"); }
            globalVariable.setGenFileFlag(false);
        }
        return successResultVO("后台处理中...");
    }

    @GetMapping("/test")
    public ResultVO<String> test() {
        System.out.println(FreemarkerUtil.generateString("index.ftl", null));
        return successResultVO(null); }
}
