package xyz.seiyaya.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import xyz.seiyaya.common.cache.service.CacheService;
import xyz.seiyaya.common.cache.service.impl.RedisCacheServiceImpl;
import xyz.seiyaya.common.serializer.DictFormatSerializerModifier;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * 公共的bean添加
 *
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

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //去掉默认的时间戳格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //单引号处理
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //反序列化时，属性不存在的兼容处理
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //空值不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //序列化时，日期的统一格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.registerModule(new SimpleModule().addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.stripTrailingZeros().toPlainString());
            }
        }));
        mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(new DictFormatSerializerModifier()));
        return mapper;
    }

    @Bean
    public CacheService cacheService(){
        return new RedisCacheServiceImpl();
    }
}
