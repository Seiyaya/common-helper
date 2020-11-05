package xyz.seiyaya.common.cache.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.seiyaya.common.bean.ResultBean;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/29 10:10
 */
@Slf4j
@Component
public class ResponseHelper {

    @Resource
    private ObjectMapper objectMapper;

    public void writeResult(ResultBean resultBean, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf8");
        try {
            PrintWriter pw = response.getWriter();
            String s = objectMapper.writeValueAsString(resultBean);
            pw.print(s);
            pw.flush();
            pw.close();
        }catch (Exception e){
            log.error("服务器响应失败:",e);
        }
    }
}
