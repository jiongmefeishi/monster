package cn.zqtao.monster.service.content;

import cn.zqtao.monster.model.entity.NBNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {

    /**
     * 新增一个笔记
     *
     * @param nbNote
     * @param tagNames
     */
    void createNote(NBNote nbNote, String tagNames);

    /**
     * 修改一篇笔记
     *
     * @param nbNote
     * @param tagNames
     */
    void updateNote(NBNote nbNote, String tagNames);

    /**
     * 查找随笔/笔记分页信息
     *
     * @param pageable
     * @param title
     * @param clearContent
     * @return
     */
    Page<NBNote> findNotePage(Pageable pageable, String title, String clearContent);
}
