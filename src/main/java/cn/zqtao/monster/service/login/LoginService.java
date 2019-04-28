package cn.zqtao.monster.service.login;

import cn.zqtao.monster.model.pojo.framework.NBR;

/**
 * 登录服务接口
 */
public interface LoginService<T> {


    /**
     * 登录方法
     *
     * @param data
     * @return
     */
    NBR doLogin(T data);


}
