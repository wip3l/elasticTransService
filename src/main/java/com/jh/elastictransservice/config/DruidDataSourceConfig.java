package com.jh.elastictransservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({DruidDataSourceConfig.DruidDataSourceProperties.class})
public class DruidDataSourceConfig {

    private final DruidDataSourceProperties properties;

    @Bean(name = "dataSource")
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());
        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setMaxActive(properties.getMaxActive());
        druidDataSource.setMaxWait(properties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(properties.getValidationQuery());
        druidDataSource.setTestWhileIdle(properties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(properties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(properties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
//        druidDataSource.setMaxOpenPreparedStatements(properties.getMaxOpenPreparedStatements());
//        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            druidDataSource.setFilters(properties.getFilters());
            druidDataSource.init();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return druidDataSource;
    }

    @Data
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    static class DruidDataSourceProperties {

        private String driverClassName;
        private String url;
        private String username;
        private String password;

        private int initialSize;
        private int minIdle;
        private int maxActive;
        private long maxWait;
        private long timeBetweenEvictionRunsMillis;
        private long minEvictableIdleTimeMillis;
        private String validationQuery;
        private boolean testWhileIdle;
        private boolean testOnBorrow;
        private boolean testOnReturn;
        private boolean poolPreparedStatements;
        private int maxOpenPreparedStatements;
        private int maxPoolPreparedStatementPerConnectionSize;
        private String filters;

    }

}  