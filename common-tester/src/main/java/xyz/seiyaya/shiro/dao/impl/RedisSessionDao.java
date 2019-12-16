package xyz.seiyaya.shiro.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.seiyaya.common.cache.service.CacheService;
import xyz.seiyaya.common.helper.StringHelper;
import xyz.seiyaya.shiro.dao.SessionDao;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;

import static xyz.seiyaya.common.cache.service.CacheService.CACHE_SERVICE_REDIS;
import static xyz.seiyaya.shiro.bean.ShiroConstant.SHIRO_SESSION_PREFIX;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/13 9:48
 */
@Slf4j
public class RedisSessionDao extends AbstractSessionDAO implements SessionDao {

    static String[] STATIC_FILE = {".html",".java",".png",".jpg"};

    @Autowired
    @Qualifier(CACHE_SERVICE_REDIS)
    private CacheService cacheService;

    @Override
    protected Serializable doCreate(Session session) {
        HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request != null){
            // 静态文件不生成session
            String uri = request.getServletPath();
            for(String fileEnd : STATIC_FILE){
                if (uri.endsWith(fileEnd)) {
                    return null;
                }
            }
        }
        Serializable serializable = this.generateSessionId(session);
        this.update(session);
        return serializable;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session s = null;
        HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null){
            // 静态文件不生成session
            String uri = request.getServletPath();
            for(String fileEnd : STATIC_FILE){
                if (uri.endsWith(fileEnd)) {
                    return null;
                }
            }
            s = (Session)request.getAttribute("session_"+sessionId);
        }
        if (s != null){
            return s;
        }

        Session session = cacheService.getObject(SHIRO_SESSION_PREFIX + sessionId, Session.class);
        if (request != null && session != null){
            request.setAttribute("session_"+sessionId, session);
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }

        HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null){
            // 静态文件不生成session
            String uri = request.getServletPath();
            for(String fileEnd : STATIC_FILE){
                if (uri.endsWith(fileEnd)) {
                    return;
                }
            }
        }

        PrincipalCollection pc = (PrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        String principalId = pc != null ? pc.getPrimaryPrincipal().toString() : StringHelper.EMPTY;
    }

    @Override
    public void delete(Session session) {

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }
}
