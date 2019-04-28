package cn.zqtao.monster.dao.repository;

import cn.zqtao.monster.model.entity.NBUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<NBUpload, Long> {
}
