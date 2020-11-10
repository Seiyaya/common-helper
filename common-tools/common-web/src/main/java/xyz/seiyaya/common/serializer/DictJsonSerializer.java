package xyz.seiyaya.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;
import xyz.seiyaya.common.annotation.DictFormat;
import xyz.seiyaya.common.cache.service.CacheService;
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
        CacheService bean = SpringHelper.getBean(CacheService.class);
        Object result = bean.hget(PREFIX_DICT + type, o.toString());
        result = result != null ? result.toString() : defaultValue;
        if(this.showOriginal){
            jsonGenerator.writeObject(o);
            jsonGenerator.writeFieldName(this.fieldName);
            jsonGenerator.writeObject(defaultValue);
        }else{
            jsonGenerator.writeObject(result);
        }
    }
}
