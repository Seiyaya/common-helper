package xyz.seiyaya.common.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * 全部都是用jackjson来进行json的徐利强化
 * @author wangjia
 * @version 1.0
 * @date 2020/11/5 15:27
 */
@Slf4j
public class JSONHelper {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //去掉默认的时间戳格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //单引号处理
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //反序列化时，属性不存在的兼容处理
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //空值不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.registerModule(new SimpleModule().addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.stripTrailingZeros().toPlainString());
            }
        }));
    }

    public static String toJSONString(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("",e);
        }
        return "";
    }

    public static <T> T getObject(String json,Class<T> clazz){
        if (StringHelper.isBlank(json)) {
            return null;
        }
        try {
            mapper.setDateFormat(new SimpleDateFormat(DateHelper.YYYY_MM_DD_HH_MM_SS));
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("toJSON()错误，原因："+e.getMessage(),e);
        }
    }

    public static void main(String[] args) {
        String s = JSONHelper.toJSONString(Integer.valueOf("12"));
        System.out.println(s);
    }
}
