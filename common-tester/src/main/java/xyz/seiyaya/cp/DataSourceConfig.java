package xyz.seiyaya.cp;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.seiyaya.common.helper.StringHelper;

import javax.sql.DataSource;

/**
 * @author wangjia
 * @version v1.0
 * @date 2020/11/27 21:18
 */
@Configuration
public class DataSourceConfig {

    /**
     * 读取 spring.datasource.orders 配置到 DataSourceProperties 对象
     * @return
     */
    @Primary
    @Bean(name = "ordersDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.orders")
    public DataSourceProperties ordersDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "ordersDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.orders.hikari")
    public DataSource ordersDataSource() {
        // <1.1> 获得 DataSourceProperties 对象
        DataSourceProperties properties =  this.ordersDataSourceProperties();
        // <1.2> 创建 HikariDataSource 对象
        return createHikariDataSource(properties);
    }

    /**
     * 读取 spring.datasource.users 配置到 DataSourceProperties 对象
     * 创建 users 数据源的配置对象
     */
    @Bean(name = "usersDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.users")
    public DataSourceProperties usersDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建 users 数据源
     */
    @Bean(name = "usersDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.users.hikari")
    public DataSource usersDataSource() {
        // 获得 DataSourceProperties 对象
        DataSourceProperties properties =  this.usersDataSourceProperties();
        // 创建 HikariDataSource 对象
        return createHikariDataSource(properties);
    }

    /**
     * 创建hikariCP数据源
     * @param properties
     * @return
     */
    private DataSource createHikariDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();

        // 设置连接池的名称
        if(StringHelper.isNotEmpty(properties.getName())){
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }


}
