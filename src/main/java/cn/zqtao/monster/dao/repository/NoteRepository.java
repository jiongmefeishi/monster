package cn.zqtao.monster.dao.repository;

import cn.zqtao.monster.model.entity.NBNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface NoteRepository extends JpaRepository<NBNote, Long> {

    /**
     * 更新笔记show的状态
     *
     * @param id
     * @param show
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBNote n set n.show = ?2 where n.id = ?1")
    int updateNoteShowStatus(Long id, Boolean show);

    /**
     * 更新笔记top的状态
     *
     * @param id
     * @param top
     * @return
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update NBNote n set n.top = ?2 where n.id = ?1")
    int updateNoteTopStatus(Long id, Boolean top);
}
