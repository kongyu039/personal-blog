package pvt.example.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    private static DataSource createDataSource(String url) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    private static SqlSessionFactory createSqlSessionFactory(DataSource dataSource, String xmlPath) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        // 自定义配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // 开启驼峰命名映射
        sessionFactory.setConfiguration(configuration);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(xmlPath)); // XML映射文件位置
        return sessionFactory.getObject();
    }

    /**
     * 主SQLite数据源
     * @return 自定义数据源 一
     */
    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource() {
        return BeanConfig.createDataSource("jdbc:sqlite:" + AppUtil.getJarDirectory("upload", "database.db"));
    }

    /**
     * 主SQLite工厂
     * @param dataSource 自定义数据源一
     * @return Sql会话工厂
     * @throws Exception 异常
     */
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        return BeanConfig.createSqlSessionFactory(dataSource, "classpath*:mapper/*.xml");
    }

    /**
     * 副SQLite数据源
     * @return 自定义数据源 一
     */
    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource() {
        return BeanConfig.createDataSource("jdbc:sqlite:" + AppUtil.getJarDirectory("frontend", "database.db"));
    }

    /**
     * 副SQLite工厂
     * @param dataSource 自定义数据源二
     * @return Sql会话工厂
     * @throws Exception 异常
     */
    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
        return BeanConfig.createSqlSessionFactory(dataSource, "classpath*:mapper2/*.xml");
    }
}
