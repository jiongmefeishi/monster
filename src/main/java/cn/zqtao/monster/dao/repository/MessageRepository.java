package cn.zqtao.monster.dao.repository;

import cn.zqtao.monster.model.entity.NBMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface MessageRepository extends JpaRepository<NBMessage, Long>, JpaSpecificationExecutor<NBMessage> {

    /**
     * 更新消息状态
     *
     * @param id
     * @param enable
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBMessage m set m.enable = ?2 where m.id = ?1")
    int updateMessageStatus(Long id, boolean enable);
}
