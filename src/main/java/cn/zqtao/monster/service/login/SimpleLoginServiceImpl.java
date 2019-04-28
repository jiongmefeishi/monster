package cn.zqtao.monster.service.login;

import cn.hutool.core.codec.Base64;
import cn.zqtao.monster.config.application.NBContext;
import cn.zqtao.monster.dao.repository.UserRepository;
import cn.zqtao.monster.model.constant.NoteBlogV4;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.model.pojo.business.SimpleLoginData;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static cn.zqtao.monster.model.pojo.framework.NBR.error;
import static cn.zqtao.monster.model.pojo.framework.NBR.ok;

/**
 * 普通登录方法
 */
@Service("simpleLogin")
@Transactional(rollbackOn = Exception.class)
public class SimpleLoginServiceImpl implements LoginService<SimpleLoginData> {

    private final UserRepository userRepository;
    private final NBContext blogContext;

    @Autowired
    public SimpleLoginServiceImpl(UserRepository userRepository, NBContext blogContext) {
        this.userRepository = userRepository;
        this.blogContext = blogContext;
    }

    @Override
    public NBR doLogin(SimpleLoginData data) {
        NBSysUser findUser = userRepository.findByUsernameAndPasswordAndEnableTrue(data.getBmyName(), data.getBmyPass());
        if (findUser != null) {
            Boolean remember = data.getRemember();
            if (remember != null && remember.equals(Boolean.TRUE)) {
                String cookieValue = Base64.encode(findUser.getUsername())
                        .concat(NoteBlogV4.Session.COOKIE_SPLIT).concat(findUser.getPassword());
                CookieUtils.setCookie(data.getResponse(), NoteBlogV4.Session.REMEMBER_COOKIE_NAME,
                        cookieValue, 15 * 24 * 60 * 60);
            }
            blogContext.setSessionUser(data.getRequest(), data.getResponse(), findUser);
            long masterRoleId = blogContext.getApplicationObj(NoteBlogV4.Session.WEBMASTER_ROLE_ID);
            String redirectUrl =
                    findUser.getDefaultRoleId() == masterRoleId
                            ? NoteBlogV4.Session.MANAGEMENT_INDEX
                            : NoteBlogV4.Session.FRONTEND_INDEX;
            return ok("登陆成功！", redirectUrl);
        } else {
            return error("用户名或密码错误！");
        }

    }


}
