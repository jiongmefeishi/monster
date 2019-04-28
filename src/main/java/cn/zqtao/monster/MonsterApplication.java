package cn.zqtao.monster;

import cn.zqtao.monster.dao.annotation.MybatisDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "cn.zqtao.monster.dao.mapper", annotationClass = MybatisDao.class)
@EnableCaching
public class MonsterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonsterApplication.class, args);
    }

}
