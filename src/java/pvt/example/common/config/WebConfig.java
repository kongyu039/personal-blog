package pvt.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pvt.example.common.utils.AppUtil;

import java.io.File;

/**
 * 信息：src/java/pvt/example/common/config/WebConfig.java
 * <p>日期：2025/7/27
 * <p>描述：Web MVC 配置类 自定义静态资源映射规则
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取当前工作目录
        // 定义静态资源路径，假设你的静态资源在 user.dir 目录下的 "static" 文件夹中
        String uploadPath = "file:" + new File(AppUtil.getJarDirectory(), "upload").getAbsolutePath() + File.separator;
        // 添加资源处理器，映射到 /static/**
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/") // 资源类路径 /static/ 下的所有
                .addResourceLocations(uploadPath); // 绝对文件夹路径 可能是开发的user.dir 或者 jar包同目录下的
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
    }
}
