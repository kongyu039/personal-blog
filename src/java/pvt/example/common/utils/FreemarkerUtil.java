package pvt.example.common.utils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 信息：src/java/pvt/example/common/utils/FreemarkerUtil.java
 * <p>日期：2025/9/9
 * <p>描述：Freemarker工具类
 */
public class FreemarkerUtil {
    private static final Configuration configuration;

    static {
        // 初始化FreeMarker配置
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        // FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
        // 设置类路径下的模板目录
        // factoryBean.setTemplateLoaderPath("classpath:/tmpls/");
        // configuration.setClassLoaderForTemplateLoading();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TemplateLoader templateLoader = new ClassTemplateLoader(classLoader, "/tmpls/");
        configuration.setTemplateLoader(templateLoader);// 设置模板文件所在目录（classpath下的tmpls目录）
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());// 设置编码
    }

    /**
     * 渲染模板并生成文件
     * @param templateName 模板文件名（位于tmpls目录下）
     * @param dataModel 数据模型
     * @param outputFile 输出文件
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static void generateFile(String templateName, Map<String, Object> dataModel, File outputFile) {
        // 渲染模板并写入文件
        try {
            // 首先确保输出目录存在
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                // 创建目录，包括任何必要的父目录
                if (!parentDir.mkdirs()) {
                    throw new RuntimeException("无法创建目录: " + parentDir.getAbsolutePath());
                }
            }

            // 现在可以安全地创建文件输出流了
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
                // 获取模板
                Template template = configuration.getTemplate(templateName);
                template.process(dataModel, writer);
            }
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("生成文件失败: " + outputFile.getAbsolutePath(), e);
        }
    }

    /**
     * 渲染模板并返回字符串
     * @param templateName 模板文件名
     * @param dataModel 数据模型
     * @return 渲染后的字符串
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static String generateString(String templateName, Map<String, Object> dataModel) {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
