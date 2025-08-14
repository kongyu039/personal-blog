package pvt.example.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.sqlite.SQLiteDataSource;
import pvt.example.common.utils.AppUtil;

import javax.sql.DataSource;

/**
 * 信息：src/java/pvt/example/common/config/BeanConfig.java
 * <p>日期：2025/7/27
 * <p>描述：基础Config配置
 */
@Configuration
public class BeanConfig {
    /**
     * @return 自定义数据源 一
     */
    @Bean
    public DataSource dataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + AppUtil.getJarDirectory() + "upload/database.db");
        return dataSource;
    }

    /**
     * @param dataSource 自定义数据源一
     * @return Sql会话工厂
     * @throws Exception 异常
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        // 自定义配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // 开启驼峰命名映射
        sessionFactory.setConfiguration(configuration);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                                                  .getResources("classpath:mapper/*.xml")); // XML映射文件位置
        return sessionFactory.getObject();
    }
}
