package xyz.seiyaya.common.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import xyz.seiyaya.common.annotation.DictFormat;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/3 16:49
 */
public class DictFormatSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for(BeanPropertyWriter writer : beanProperties){
            DictFormat dictFormat = writer.getAnnotation(DictFormat.class);
            if( dictFormat != null){
                writer.assignSerializer(new DictJsonSerializer(dictFormat));
            }
        }
        return super.changeProperties(config, beanDesc, beanProperties);
    }
}
