package xyz.seiyaya.common.helper;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;

/**
 * freemarker工具类
 * @author seiyaya
 * @date 2019/8/13 16:08
 */
public class FreeMarkerHelper {


    /**
     * 渲染模版
     * @param template
     * @param model
     * @return
     * @throws Exception
     */
    public static String renderTemplate(Template template,Object model) throws Exception {
        StringWriter writer = new StringWriter();
        template.process(model,writer);
        return writer.toString();
    }
}
