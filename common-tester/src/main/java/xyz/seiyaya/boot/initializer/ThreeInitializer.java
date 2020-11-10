package xyz.seiyaya.boot.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MapPropertySource;
import xyz.seiyaya.common.helper.DBParam;

@Slf4j
@Order(3)
public class ThreeInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        DBParam dbParam = new DBParam();
        dbParam.set("key3","value3");
        MapPropertySource threeInitializer = new MapPropertySource("threeInitializer", dbParam);
        applicationContext.getEnvironment().getPropertySources().addLast(threeInitializer);
        log.info("run three initializer");
    }
}
