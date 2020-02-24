package xyz.seiyaya.boot.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MapPropertySource;
import xyz.seiyaya.common.helper.DBParam;

@Slf4j
@Order(1)
public class FirstInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        DBParam dbParam = new DBParam();
        dbParam.set("key1","value1");
        MapPropertySource firstInitializer = new MapPropertySource("firstInitializer", dbParam);
        applicationContext.getEnvironment().getPropertySources().addLast(firstInitializer);
        log.info("run first initializer");
    }
}
