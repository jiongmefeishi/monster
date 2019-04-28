package cn.zqtao.monster.dao.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 表明是Mybatis的Mapper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface MybatisDao {

    String value() default "";
}
