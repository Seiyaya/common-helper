package xyz.seiyaya.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 公共的bean添加
 * @author wangjia
 * @version 1.0
 * @date 2019/12/5 16:15
 */
@Configuration
public class CommonBeanConfig {

    @Resource
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
