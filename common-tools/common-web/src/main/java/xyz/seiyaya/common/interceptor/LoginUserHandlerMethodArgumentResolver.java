package xyz.seiyaya.common.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import xyz.seiyaya.common.annotation.LoginUser;
import xyz.seiyaya.common.constant.Constant;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangjia
 * @version 1.0
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (webRequest.getNativeRequest() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            return request.getAttribute(Constant.User.USER_KEY);
        }
        return null;
    }
}
