package cn.zqtao.monster.config.configuration;

import cn.zqtao.monster.config.application.NBContext;
import cn.zqtao.monster.config.interceptor.AdminInterceptor;
import cn.zqtao.monster.config.interceptor.ApplicationInterceptor;
import cn.zqtao.monster.config.interceptor.SessionInterceptor;
import cn.zqtao.monster.config.interceptor.ThemeHandlerInterceptor;
import cn.zqtao.monster.dao.repository.ParamRepository;
import cn.zqtao.monster.model.constant.Upload;
import cn.zqtao.monster.service.param.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final NBContext blogContext;
    private final Environment env;
    private final ParamService paramService;
    private final ParamRepository paramRepository;

    @Autowired
    public WebMvcConfig(NBContext blogContext, Environment env, ParamService paramService, ParamRepository paramRepository) {
        this.blogContext = blogContext;
        this.env = env;
        this.paramService = paramService;
        this.paramRepository = paramRepository;
    }

    /**
     * 添加一些虚拟路径的映射
     * 静态资源路径和上传文件的路径
     * 如果配置了七牛云上传，则上传路径无效
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        String uploadPath = env.getProperty("noteblog.upload.path");
        registry.addResourceHandler(Upload.FileType.VISIT_PATH + "/**").addResourceLocations(uploadPath);
    }

    /**
     * 添加一些全局的拦截器，做一些操作
     * 验证用户，设置user到session中等
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApplicationInterceptor(blogContext, paramService)).addPathPatterns("/**").excludePathPatterns("/static/**");
        registry.addInterceptor(new SessionInterceptor(blogContext)).addPathPatterns("/management/**", "/token/**");
        registry.addInterceptor(new AdminInterceptor(blogContext)).addPathPatterns("/management/**");
        registry.addInterceptor(new ThemeHandlerInterceptor(paramRepository))
                .addPathPatterns("/article/**", "/a/**").excludePathPatterns("/article/comments","/article/approve","/a/comments","/a/approve");

    }

    /**
     * 添加全局拦截的error移除处理类
     *
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> container.addErrorPages(
                new ErrorPage(HttpStatus.NOT_FOUND, "/error?errorCode=404"),
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error?errorCode=500"),
                new ErrorPage(Throwable.class, "/error?errorCode=500")
        );
    }


}
