package cn.zqtao.monster.config.interceptor;

import cn.zqtao.monster.dao.repository.ParamRepository;
import cn.zqtao.monster.web.BaseController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主题解析拦截器
 */
public class ThemeHandlerInterceptor extends BaseController implements HandlerInterceptor {

    private ParamRepository paramRepository;

    public ThemeHandlerInterceptor(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String view = modelAndView.getViewName();
        String simpleView = view + "_simple";
        String normalView = view + "_normal";
        modelAndView.setViewName(handleStyle(simpleView, normalView, this.paramRepository));
    }

}
