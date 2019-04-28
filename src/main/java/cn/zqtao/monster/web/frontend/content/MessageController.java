package cn.zqtao.monster.web.frontend.content;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.zqtao.monster.dao.repository.CateRepository;
import cn.zqtao.monster.dao.repository.KeywordRepository;
import cn.zqtao.monster.dao.repository.MessageRepository;
import cn.zqtao.monster.dao.repository.UserRepository;
import cn.zqtao.monster.model.entity.NBKeyword;
import cn.zqtao.monster.model.entity.NBMessage;
import cn.zqtao.monster.model.pojo.bo.MessageQueryBO;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.model.pojo.framework.Pagination;
import cn.zqtao.monster.service.content.MessageService;
import cn.zqtao.monster.service.content.TagService;
import cn.zqtao.monster.util.NBUtils;
import cn.zqtao.monster.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.zqtao.monster.model.pojo.framework.NBR.error;

@Controller
@RequestMapping
public class MessageController extends BaseController {

    private final MessageRepository messageRepository;
    private final KeywordRepository keywordRepository;
    private final CateRepository cateRepository;
    private final TagService tagService;
    private final MessageService messageService;
    private final UserRepository userRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository, KeywordRepository keywordRepository,
                             CateRepository cateRepository, TagService tagService, MessageService messageService,
                             UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.keywordRepository = keywordRepository;
        this.cateRepository = cateRepository;
        this.tagService = tagService;
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @RequestMapping("/msg")
    public String index(Model model, Pagination<NBMessage> messagePagination, MessageQueryBO messageQueryBO) {
        model.addAttribute("cates", cateRepository.findAll());
        model.addAttribute("tags", tagService.findTagsTab());
        Pageable pageable = messagePageable(messagePagination);
        model.addAttribute("messages", messageService.findPageInfo(pageable, messageQueryBO));
        return "frontend/content/message";
    }


    @RequestMapping(value = "/token/msg/sub", method = RequestMethod.POST)
    @ResponseBody
    public NBR sub(@Valid NBMessage message, BindingResult bindingResult, HttpServletRequest request) {
        if (!bindingResult.hasErrors()) {
            message.setIpAddr(NBUtils.getRemoteAddress(request));
            message.setUserAgent(request.getHeader("user-agent"));
            final String key = "noteblog.develop";
            boolean develop = NBUtils.getBean(Environment.class).getProperty(key, Boolean.class, true);
            if (!develop) {
                message.setIpCnAddr(NBUtils.getIpCnInfo(NBUtils.getIpInfo(message.getIpAddr())));
            } else {
                message.setIpCnAddr("本地/未知");
            }
            message.setComment(NBUtils.stripSqlXSS(message.getComment()));
            message.setClearComment(HtmlUtil.cleanHtmlTag(message.getComment()));
            message.setUser(userRepository.getOne(message.getUserId()));
            message.setEnable(true);
            message.setPost(LocalDateTime.now());
            List<NBKeyword> keywords = keywordRepository.findAll();
            keywords.forEach(kw -> message.setComment(message.getComment().replace(kw.getWords(), StrUtil.repeat("*", kw.getWords().length()))));
            return ajaxDone(() -> messageRepository.save(message) != null, () -> "发表留言");
        } else {
            return error("提交的留言内容不合法");
        }
    }


    @RequestMapping(value = "/msg/lists", method = RequestMethod.POST)
    @ResponseBody
    public Page<NBMessage> comments(Pagination<NBMessage> messagePagination, MessageQueryBO messageQueryBO) {
        Pageable pageable = messagePageable(messagePagination);
        return messageService.findPageInfo(pageable, messageQueryBO);
    }


    private Pageable messagePageable(Pagination<NBMessage> messagePagination) {
        Map<String, String> orders = new HashMap<>(1);
        orders.put("post", "desc");
        Sort sort = getJpaSortWithOther(messagePagination, orders);
        return PageRequest.of(messagePagination.getPage() - 1, messagePagination.getLimit(), sort);
    }
}
