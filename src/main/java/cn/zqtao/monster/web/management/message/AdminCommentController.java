package cn.zqtao.monster.web.management.message;

import cn.zqtao.monster.config.permission.NBAuth;
import cn.zqtao.monster.dao.repository.CommentRepository;
import cn.zqtao.monster.model.entity.NBComment;
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

/**
 * created by Wuwenbin on 2018/8/15 at 16:28
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management/comment")
public class AdminCommentController extends BaseController {

    private final CommentRepository commentRepository;

    @Autowired
    public AdminCommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @RequestMapping
    @NBAuth(value = "management:comment:page", remark = "评论管理页面", group = ROUTER, type = NAV_LINK)
    public String tagIndex() {
        return "management/message/comment";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:comment:list", remark = "评论管理页面分页数据接口", group = AJAX)
    public LayuiTable<NBComment> cateList(Pagination<NBComment> commentPagination, String clearComment) {
        Sort sort = getJpaSort(commentPagination);
        Pageable pageable = PageRequest.of(commentPagination.getPage() - 1, commentPagination.getLimit(), sort);
        if (StringUtils.isEmpty(clearComment)) {
            Page<NBComment> tagPage = commentRepository.findAll(pageable);
            return layuiTable(tagPage, pageable);
        } else {
            Example<NBComment> commentExample = Example.of(
                    NBComment.builder().clearComment(clearComment).build(),
                    ExampleMatcher.matching()
                            .withMatcher("clearComment", ExampleMatcher.GenericPropertyMatcher::contains).withIgnoreCase()
                            .withIgnorePaths("post", "enable")
            );
            Page<NBComment> commentPage = commentRepository.findAll(commentExample, pageable);
            return layuiTable(commentPage, pageable);
        }
    }


    @RequestMapping("/update")
    @ResponseBody
    @NBAuth(value = "management:comment:update", remark = "修改评论状态", group = AJAX)
    public NBR delete(@RequestParam("id") Long id, boolean enable) {
        return ajaxDone(
                () -> commentRepository.updateCommentStatus(id, enable) == 1,
                () -> "修改评论"
        );
    }
}
