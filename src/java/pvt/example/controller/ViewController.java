package pvt.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 信息：src/java/pvt/example/controller/ViewController.java
 * <p>日期：2025/8/2
 * <p>描述：视图控制(Thymeleaf专用)
 */
@Controller
public class ViewController {
    @GetMapping("/index.html")
    public String index() { return "index"; }

    /** 首页 */
    @GetMapping("/content/home.html")
    public String contentHome() { return "content/home"; }

    /** 文章 */
    @GetMapping("/content/posts.html")
    public String contentPosts() { return "content/posts"; }

    /** 标签和分类 */
    @GetMapping("/content/tags.html")
    public String contentTags() { return "content/tags"; }

    /** 基础设置 */
    @GetMapping("/content/setting.html")
    public String contentSetting() { return "content/setting"; }

    /** 回收站 */
    @GetMapping("/content/recycle.html")
    public String contentRecycle() { return "content/recycle"; }

    @GetMapping("/common/editor.html")
    public String commonEditor() { return "common/editor"; }
}
