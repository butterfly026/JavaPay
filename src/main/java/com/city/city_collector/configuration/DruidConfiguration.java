package com.city.city_collector.configuration;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

    private static final String DB_PREFIX = "spring.datasource";

    @Bean
    public ServletRegistrationBean druidServlet() {
        logger.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
                new String[]{"/druid/*"});

        servletRegistrationBean.addInitParameter("loginUsername", "cnmadmin$$!");
        servletRegistrationBean.addInitParameter("loginPassword", "cnmadmin$$!2021@@@@");

        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter(),
                new ServletRegistrationBean[0]);
        filterRegistrationBean.addUrlPatterns(new String[]{"/*"});
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.json,/druid/*");
        return filterRegistrationBean;
    }

    @Component
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    class IDataSourceProperties {
        private String url;

        private String username;

        private String password;

        private String driverClassName;

        private int initialSize;

        private int minIdle;

        private int maxActive;

        private int maxWait;

        private int timeBetweenEvictionRunsMillis;

        private int minEvictableIdleTimeMillis;

        private int maxEvictableIdleTimeMillis;

        private String validationQuery;

        private boolean testWhileIdle;

        private boolean testOnBorrow;

        private boolean testOnReturn;

        private boolean poolPreparedStatements;

        private int maxPoolPreparedStatementPerConnectionSize;

        private String filters;

        private String connectionProperties;

        private boolean keepAlive;

        IDataSourceProperties() {
        }

        @Bean
        @Primary
        public DataSource dataSource() {
            DruidDataSource datasource = new DruidDataSource();
            datasource.setUrl(this.url);
            datasource.setUsername(this.username);
            datasource.setPassword(this.password);
            datasource.setDriverClassName(this.driverClassName);

            datasource.setInitialSize(this.initialSize);
            datasource.setMinIdle(this.minIdle);
            datasource.setMaxActive(this.maxActive);
            datasource.setMaxWait(this.maxWait);
            datasource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
            datasource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
            datasource.setMaxEvictableIdleTimeMillis(this.maxEvictableIdleTimeMillis);

//            System.out.println("QQA:"+this.timeBetweenEvictionRunsMillis+"|"+this.minEvictableIdleTimeMillis+"--"+this.maxEvictableIdleTimeMillis+"|"+this.minIdle+"|"+this.maxWait+"--"+this.validationQuery);

            datasource.setValidationQuery(this.validationQuery);
            datasource.setTestWhileIdle(this.testWhileIdle);
            datasource.setTestOnBorrow(this.testOnBorrow);
            datasource.setTestOnReturn(this.testOnReturn);
            datasource.setPoolPreparedStatements(this.poolPreparedStatements);
            datasource.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);
            datasource.setKeepAlive(this.keepAlive);
            try {
                datasource.setFilters(this.filters);
            } catch (SQLException e) {
                System.err.println("druid configuration initialization filter: " + e);
            }
            datasource.setConnectionProperties(this.connectionProperties);
//            System.out.println("init datasource=======================================");
            return datasource;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return this.driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public int getInitialSize() {
            return this.initialSize;
        }

        public void setInitialSize(int initialSize) {
            this.initialSize = initialSize;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public int getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }

        public int getTimeBetweenEvictionRunsMillis() {
            return this.timeBetweenEvictionRunsMillis;
        }

        public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        }

        public int getMinEvictableIdleTimeMillis() {
            return this.minEvictableIdleTimeMillis;
        }

        public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        }

        public String getValidationQuery() {
            return this.validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        public boolean isTestWhileIdle() {
            return this.testWhileIdle;
        }

        public void setTestWhileIdle(boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        public boolean isTestOnBorrow() {
            return this.testOnBorrow;
        }

        public void setTestOnBorrow(boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public boolean isTestOnReturn() {
            return this.testOnReturn;
        }

        public void setTestOnReturn(boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public boolean isPoolPreparedStatements() {
            return this.poolPreparedStatements;
        }

        public void setPoolPreparedStatements(boolean poolPreparedStatements) {
            this.poolPreparedStatements = poolPreparedStatements;
        }

        public int getMaxPoolPreparedStatementPerConnectionSize() {
            return this.maxPoolPreparedStatementPerConnectionSize;
        }

        public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
            this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
        }

        public String getFilters() {
            return this.filters;
        }

        public void setFilters(String filters) {
            this.filters = filters;
        }

        public String getConnectionProperties() {
            return this.connectionProperties;
        }

        public void setConnectionProperties(String connectionProperties) {
            this.connectionProperties = connectionProperties;
        }

        /**
         * maxEvictableIdleTimeMillis
         *
         * @return maxEvictableIdleTimeMillis
         */
        public int getMaxEvictableIdleTimeMillis() {
            return maxEvictableIdleTimeMillis;
        }

        /**
         * 设置 maxEvictableIdleTimeMillis
         *
         * @param maxEvictableIdleTimeMillis maxEvictableIdleTimeMillis
         */
        public void setMaxEvictableIdleTimeMillis(int maxEvictableIdleTimeMillis) {
            this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
        }

        /**
         * keepAlive
         *
         * @return keepAlive
         */
        public boolean isKeepAlive() {
            return keepAlive;
        }

        /**
         * 设置 keepAlive
         *
         * @param keepAlive keepAlive
         */
        public void setKeepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
        }
    }
}
