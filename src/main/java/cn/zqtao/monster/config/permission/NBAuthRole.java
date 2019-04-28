package cn.zqtao.monster.config.permission;

import java.lang.annotation.*;

/**
 * 基于注解的权限简单验证
 * 此处为权限注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NBAuthRole {

    /**
     * 角色名
     * 只要满足该角色即可访问，不需要匹配权限
     *
     * @return
     */
    String[] value();


}
