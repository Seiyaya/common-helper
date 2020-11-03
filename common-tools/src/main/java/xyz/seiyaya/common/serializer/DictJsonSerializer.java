package xyz.seiyaya.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import xyz.seiyaya.common.annotation.DictFormat;
import xyz.seiyaya.common.helper.SpringHelper;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/3 16:52
 */
@Data
public class DictJsonSerializer extends JsonSerializer<Object> {

    private boolean showOriginal;

    private String type;

    private String fieldName;

    private String defaultValue;

    public static final String PREFIX_DICT = "DICT:CACHE:";

    public DictJsonSerializer(DictFormat dictFormat){
        this.type = dictFormat.type();
        this.fieldName = dictFormat.fieldName();
        this.defaultValue = dictFormat.defaultValue();
        this.showOriginal = dictFormat.showOriginal();
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // TODO: 没有该属性直接在json里面不显示
        if(this.showOriginal){
            jsonGenerator.writeObject(o);
        }else{
            jsonGenerator.writeObject(null);
        }
        StringRedisTemplate bean = SpringHelper.getBean(StringRedisTemplate.class);
        Object result = bean.opsForHash().get(PREFIX_DICT + type, o.toString());
        jsonGenerator.writeFieldName(this.fieldName);
        if(result != null){
            jsonGenerator.writeObject(result.toString());
        }else{
            jsonGenerator.writeObject(defaultValue);
        }
    }
}
