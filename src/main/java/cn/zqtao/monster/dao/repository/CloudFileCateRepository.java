package cn.zqtao.monster.dao.repository;

import cn.zqtao.monster.model.entity.NBCloudFileCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CloudFileCateRepository extends JpaRepository<NBCloudFileCate, Long> {

    /**
     * 查找已存在对应的分类数目
     *
     * @param cate
     * @return
     */
    @Query("SELECT COUNT(c) FROM NBCloudFileCate c WHERE c.cnName = :#{#cate.cnName} AND c.name = :#{#cate.name}")
    long findCateCount(@Param("cate") NBCloudFileCate cate);
}
