package xyz.seiyaya.boot.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MapPropertySource;
import xyz.seiyaya.common.cache.helper.DBParam;

@Slf4j
@Order(2)
public class SecondInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        DBParam dbParam = new DBParam();
        dbParam.set("key2","value2");
        MapPropertySource firstInitializer = new MapPropertySource("secondInitializer", dbParam);
        applicationContext.getEnvironment().getPropertySources().addLast(firstInitializer);
        log.info("run second initializer");
    }
}
