package xyz.seiyaya.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import xyz.seiyaya.common.helper.StringHelper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/13 9:41
 */
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {

    private static final String PARAM_NAME = "__cookie";

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String sid = request.getParameter("__sid");

        if(StringHelper.isNotEmpty(sid)){
            if(WebUtils.isTrue(request,PARAM_NAME)){
                HttpServletRequest rq = (HttpServletRequest)request;
                HttpServletResponse rs = (HttpServletResponse)response;
                Cookie template = getSessionIdCookie();
                Cookie cookie = new SimpleCookie(template);
                cookie.setValue(sid);
                cookie.saveTo(rq, rs);
            }
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sid);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sid;
        }else{
            return super.getSessionId(request, response);
        }
    }

    @Override
    protected Session newSessionInstance(SessionContext context) {
        Session session = super.newSessionInstance(context);
        session.setTimeout(getGlobalSessionTimeout());
        return session;
    }

    @Override
    public Session start(SessionContext context) {
        try{
            return super.start(context);
        }catch (NullPointerException e) {
            log.error("start", e);
            SimpleSession session = new SimpleSession();
            session.setId(0);
            return session;
        }
    }
}
