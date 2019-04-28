package cn.zqtao.monster.web.management.message;

import cn.zqtao.monster.config.permission.NBAuth;
import cn.zqtao.monster.dao.repository.MessageRepository;
import cn.zqtao.monster.model.entity.NBMessage;
import cn.zqtao.monster.model.pojo.framework.LayuiTable;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.model.pojo.framework.Pagination;
import cn.zqtao.monster.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static cn.zqtao.monster.config.permission.NBAuth.Group.AJAX;
import static cn.zqtao.monster.config.permission.NBAuth.Group.ROUTER;
import static cn.zqtao.monster.model.entity.permission.NBSysResource.ResType.NAV_LINK;

@Controller
@RequestMapping("/management/message")
public class AdminMessageController extends BaseController {

    private final MessageRepository messageRepository;

    @Autowired
    public AdminMessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @RequestMapping
    @NBAuth(value = "management:message:page", remark = "消息管理页面", group = ROUTER, type = NAV_LINK)
    public String tagIndex() {
        return "management/message/message";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:message:list", remark = "消息管理页面分页数据接口", group = AJAX)
    public LayuiTable<NBMessage> cateList(Pagination<NBMessage> messagePagination, String clearComment) {
        Sort sort = getJpaSort(messagePagination);
        Pageable pageable = PageRequest.of(messagePagination.getPage() - 1, messagePagination.getLimit(), sort);
        if (StringUtils.isEmpty(messagePagination)) {
            Page<NBMessage> tagPage = messageRepository.findAll(pageable);
            return layuiTable(tagPage, pageable);
        } else {
            Example<NBMessage> messageExample = Example.of(
                    NBMessage.builder().clearComment(clearComment).build(),
                    ExampleMatcher.matching()
                            .withMatcher("clearComment", ExampleMatcher.GenericPropertyMatcher::contains).withIgnoreCase()
                            .withIgnorePaths("post", "enable")
            );
            Page<NBMessage> commentPage = messageRepository.findAll(messageExample, pageable);
            return layuiTable(commentPage, pageable);
        }
    }


    @RequestMapping("/update")
    @ResponseBody
    @NBAuth(value = "management:message:update", remark = "修改评论状态", group = AJAX)
    public NBR delete(@RequestParam("id") Long id, boolean enable) {
        return ajaxDone(
                () -> messageRepository.updateMessageStatus(id, enable) == 1,
                () -> "修改留言"
        );
    }
}
