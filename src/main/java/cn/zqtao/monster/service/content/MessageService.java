package cn.zqtao.monster.service.content;

import cn.zqtao.monster.model.entity.NBMessage;
import cn.zqtao.monster.model.pojo.bo.MessageQueryBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * created by Wuwenbin on 2018/9/7 at 9:42
 *
 * @author wuwenbin
 */
public interface MessageService {

    /**
     * 查询消息的分页信息
     *
     * @param pageable
     * @param messageQueryBO
     * @return
     */
    Page<NBMessage> findPageInfo(Pageable pageable, MessageQueryBO messageQueryBO);
}
