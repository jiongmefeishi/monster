package cn.zqtao.monster.config.application;

import cn.zqtao.monster.model.constant.Monster;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.util.CookieUtils;
import cn.zqtao.monster.util.NBUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局的上下文对象
 * 用于操作session和cookie
 */
@Component
public class NBContext extends ConcurrentHashMap<String, NBSession> {

    private static ConcurrentHashMap<String, Object> applicationContext = new ConcurrentHashMap<>(100);

    public void setSessionUser(HttpServletRequest request, HttpServletResponse response, NBSysUser sessionUser) {
        NBSession session = NBSession.builder()
                .sessionUser(sessionUser)
                .expired(false)
                .host(NBUtils.getRemoteAddress(request))
                .build();
        Cookie cookie = CookieUtils.getCookie(request, Monster.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            session.setId(cookie.getValue());
        }
        CookieUtils.setCookie(response, Monster.Session.SESSION_ID_COOKIE, session.getId(), -1);
        put(session.getId(), session);
    }

    public NBSysUser getSessionUser(String uuid) {
        Optional<NBSession> user = Optional.ofNullable(get(uuid));
        return user.map(NBSession::getSessionUser).orElse(null);
    }

    public void removeSessionUser(String uuid) {
        super.remove(uuid);
    }

    public void clearAll() {
        super.clear();
    }

    public void setApplicationObj(String key, Object value) {
        applicationContext.put(key, value);
    }

    public <T> T getApplicationObj(String key) {
        //noinspection unchecked
        return (T) applicationContext.get(key);
    }

    public void removeApplicationObj(String key) {
        applicationContext.remove(key);
    }

}
