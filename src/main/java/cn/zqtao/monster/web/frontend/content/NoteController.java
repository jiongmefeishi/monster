package cn.zqtao.monster.web.frontend.content;

import lombok.extern.slf4j.Slf4j;
import cn.zqtao.monster.dao.repository.NoteRepository;
import cn.zqtao.monster.model.entity.NBNote;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.model.pojo.framework.Pagination;
import cn.zqtao.monster.service.content.NoteService;
import cn.zqtao.monster.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller("frontNoteController")
@RequestMapping("/note")
public class NoteController extends BaseController {

    private final NoteRepository noteRepository;
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteRepository noteRepository, NoteService noteService) {
        this.noteRepository = noteRepository;
        this.noteService = noteService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("noteCount", noteRepository.count());
        return "frontend/content/note";
    }

    @RequestMapping(value = "/next", method = RequestMethod.POST)
    @ResponseBody
    public NBR next(Pagination<NBNote> pagination, String t, String cc) {
        Map<String, String> orders = new HashMap<>(2);
        orders.put("top", "desc");
        orders.put("post", "desc");
        Sort sort = getJpaSortWithOther(pagination, orders);
        Pageable pageable = PageRequest.of(pagination.getPage() - 1, pagination.getLimit(), sort);
        Page<NBNote> notePage = noteService.findNotePage(pageable, t, cc);
        return NBR.ok("获取成功", notePage);
    }
}
