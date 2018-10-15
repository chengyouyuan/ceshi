package com.winhxd.b2c.detection.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;


/**
 * HikariCP连接池配置
 */
@Configuration
@MapperScan(basePackages = "com.winhxd.b2c.detection.mapper")
public class MyBatisConfig {

    @Value("${spring.datasource.b2c-detection.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.b2c-detection.username}")
    private String user;

    @Value("${spring.datasource.b2c-detection.password}")
    private String password;

    @Bean(name = "detectionData")
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        /* 数据源 */
        config.setJdbcUrl(dataSourceUrl);
        //用户名
        config.setUsername(user);
        /* 密码 */
        config.setPassword(password);
        /* 是否自定义配置，为true时下面两个参数才生效 */
        config.addDataSourceProperty("cachePrepStmts", "true");
        /* 连接池大小默认25，官方推荐250-500 */
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        /* 单条语句最大长度默认256，官方推荐2048 */
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        /* 新版本MySQL支持服务器端准备，开启能够得到显著性能提升 */
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

}