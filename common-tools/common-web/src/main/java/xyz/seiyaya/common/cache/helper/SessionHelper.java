package xyz.seiyaya.common.cache.helper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 10:40
 */
public class SessionHelper {
    public static <T> T getCurrentUser(HttpServletRequest request,Class<T> clazz) {
        return (T)request.getAttribute("current_user");
    }
}
