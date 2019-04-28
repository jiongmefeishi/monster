package cn.zqtao.monster.service.content;

import cn.zqtao.monster.model.constant.TagType;
import cn.zqtao.monster.model.pojo.vo.NBTagVO;

import java.util.List;
import java.util.Map;

public interface TagService {

    /**
     * 查找文章/笔记相关tag并selected
     *
     * @param referId
     * @param type    文章还是笔记{@code TagType}
     * @return
     */
    List<NBTagVO> findSelectedTagsByReferId(Long referId, TagType type);

    /**
     * 查询标签使用数到首页标签面板上显示
     *
     * @return
     */
    List<Map<String, Object>> findTagsTab();
}
