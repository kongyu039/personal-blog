package pvt.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 信息：src/java/pvt/example/BootMain.java
 * <p>日期：2025/7/26
 * <p>描述：一体式博客与发布
 */
@SpringBootApplication// 默认解析注解的根包
@MapperScan(basePackages = {"pvt.example.mapper"}) // 同包下Mapper同位置对应xxxMapper.xml
public class BootMain extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BootMain.class);
        app.run(args);
    }

    // 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BootMain.class);// 指向原先用main方法执行的Application启动类
    }
}