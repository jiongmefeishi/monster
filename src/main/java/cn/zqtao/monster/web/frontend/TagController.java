package cn.zqtao.monster.web.frontend;

import cn.zqtao.monster.dao.repository.TagRepository;
import cn.zqtao.monster.model.entity.NBTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/tag")
public class TagController {

    private final TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<NBTag> tagsList() {
        return tagRepository.findAll();
    }
}
