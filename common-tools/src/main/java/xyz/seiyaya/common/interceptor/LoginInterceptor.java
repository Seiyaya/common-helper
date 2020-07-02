package xyz.seiyaya.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import xyz.seiyaya.common.bean.LoginUserInfo;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.config.Constant;
import xyz.seiyaya.common.helper.ResponseHelper;
import xyz.seiyaya.common.helper.SpringHelper;
import xyz.seiyaya.common.helper.StringHelper;
import xyz.seiyaya.common.helper.UserHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangjia
 * @version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResponseHelper responseHelper = SpringHelper.getBean(ResponseHelper.class);
        ResultBean resultBean = new ResultBean();
        String token = request.getHeader("token");
        if(StringHelper.isBlank(token)){
            responseHelper.writeResult(resultBean.setParamError("token不能为空"),response);
            return false;
        }
        LoginUserInfo loginUserInfo = UserHelper.getLoginUserInfo(token);
        if(loginUserInfo == null){
            responseHelper.writeResult(resultBean.setParamError("token不存在"),response);
            return false;
        }
        request.setAttribute(Constant.UserConstant.USER_KEY,loginUserInfo);
        return true;
    }
}
