package cn.zqtao.monster.dao.repository;

import cn.zqtao.monster.model.entity.NBCloudFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CloudFileRepository extends JpaRepository<NBCloudFile, Long> {

    /**
     * 计算某个cateId说下的数量
     *
     * @param cateId
     * @return
     */
    long countByCateId(long cateId);

}
