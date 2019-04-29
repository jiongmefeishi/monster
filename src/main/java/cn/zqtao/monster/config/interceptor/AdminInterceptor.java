package cn.zqtao.monster.config.interceptor;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.zqtao.monster.config.application.NBContext;
import cn.zqtao.monster.model.constant.Monster;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.util.CookieUtils;
import cn.zqtao.monster.util.NBUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 虽然{@code @NBAuth}也能够处理权限拦截
 * 此处的拦截器更多的是为了处理Session和Cookie的一些逻辑
 */
public class AdminInterceptor extends HandlerInterceptorAdapter {

    private NBContext blogContext;

    public AdminInterceptor(NBContext blogContext) {
        this.blogContext = blogContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = CookieUtils.getCookie(request, Monster.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            String sessionId = cookie.getValue();
            NBSysUser sessionUser = blogContext.getSessionUser(sessionId);
            if (sessionUser == null) {
                handleAjaxRequest(request, response);
                return false;
            } else if (sessionUser.getDefaultRoleId() == blogContext.getApplicationObj(Monster.Session.WEBMASTER_ROLE_ID)) {
                return true;
            } else {
                if (NBUtils.isAjaxRequest(request)) {
                    JSONObject jsonObject = JSONUtil.createObj();
                    jsonObject.putAll(NBR.error("非法访问，即将跳转首页！", Monster.Session.FRONTEND_INDEX));
                    response.getWriter().write(jsonObject.toString());
                } else {
                    response.sendRedirect(Monster.Session.FRONTEND_INDEX);
                }
                return false;
            }
        }
        return false;
    }

    static void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //用户未登录或登录时效过期，请重新登录！
        final String message = "\u7528\u6237\u672a\u767b\u5f55\u6216\u767b\u5f55\u65f6\u6548\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\uff01";
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (NBUtils.isRouterRequest(request)) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.putAll(NBR.custom(-1, message, Monster.Session.LOGIN_URL));
            response.getWriter().write(jsonObject.toString());
        } else if (NBUtils.isAjaxRequest(request) && !NBUtils.isRouterRequest(request)) {
            JSONObject jsonObject = JSONUtil.createObj();
            jsonObject.putAll(NBR.custom(-1, message, Monster.Session.LOGIN_URL));
            response.getWriter().write(jsonObject.toString());
        } else {
            response.sendRedirect(Monster.Session.LOGIN_URL);
        }
    }
}
