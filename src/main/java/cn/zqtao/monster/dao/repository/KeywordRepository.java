package cn.zqtao.monster.dao.repository;

import cn.zqtao.monster.model.entity.NBKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface KeywordRepository extends JpaRepository<NBKeyword, Long> {

    /**
     * 更新状态
     *
     * @param enable
     * @param id
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("UPDATE NBKeyword k SET k.enable = ?1 WHERE k.id = ?2")
    int updateEnableById(boolean enable, long id);

    /**
     * 更新关键字文本
     *
     * @param words
     * @param id
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("UPDATE NBKeyword k SET k.words = ?1 WHERE k.id = ?2")
    int updateTextById(String words, long id);
}
