package cn.zqtao.monster.dao.repository;

import cn.zqtao.monster.model.entity.NBAbout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<NBAbout, Long> {
}
