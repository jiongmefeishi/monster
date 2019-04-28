package cn.zqtao.monster.service.content;

import cn.zqtao.monster.model.entity.NBComment;
import cn.zqtao.monster.model.pojo.bo.CommentQueryBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    /**
     * 查询评论的分页信息
     *
     * @param pageable
     * @param commentQueryBO
     * @return
     */
    Page<NBComment> findPageInfo(Pageable pageable, CommentQueryBO commentQueryBO);
}
