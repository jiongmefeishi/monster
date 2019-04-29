package cn.zqtao.monster.config.interceptor;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import cn.zqtao.monster.config.application.NBContext;
import cn.zqtao.monster.dao.repository.UserRepository;
import cn.zqtao.monster.model.constant.Monster;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.util.CookieUtils;
import cn.zqtao.monster.util.NBUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器更多的是为了处理Session和Cookie的一些逻辑
 */
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

    private UserRepository userRepository = NBUtils.getBean(UserRepository.class);

    private NBContext blogContext;

    public SessionInterceptor(NBContext blogContext) {
        this.blogContext = blogContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = CookieUtils.getCookie(request, Monster.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            String sessionId = cookie.getValue();
            NBSysUser sessionUser = blogContext.getSessionUser(sessionId);
            if (sessionUser == null) {
                Cookie rememberCookie = CookieUtils.getCookie(request, Monster.Session.REMEMBER_COOKIE_NAME);
                if (rememberCookie != null) {
                    String userString = rememberCookie.getValue();
                    try {
                        String username = userString.split(Monster.Session.COOKIE_SPLIT)[0];
                        String password = userString.split(Monster.Session.COOKIE_SPLIT)[1];
                        NBSysUser cookieUser = userRepository.findByUsernameAndPasswordAndEnableTrue(Base64.decodeStr(username), password);
                        if (cookieUser != null) {
                            blogContext.setSessionUser(request, response, cookieUser);
                            log.info("[已登录用户]");
                            return true;
                        }
                    } catch (Exception ignore) {
                    }
                }
                log.info("[未登录用户]");
                AdminInterceptor.handleAjaxRequest(request, response);
                return false;
            } else {
                log.info("[已登录用户]");
                return true;
            }
        } else {
            response.sendRedirect(Monster.Session.LOGIN_URL);
            return false;
        }
    }
}

