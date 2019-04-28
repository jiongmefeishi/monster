package cn.zqtao.monster.web.frontend;

import cn.zqtao.monster.dao.repository.ProfileRepository;
import cn.zqtao.monster.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by Wuwenbin on 2018/11/25 at 13:09
 *
 * @author wuwenbin
 */
@Controller("frontProfileController")
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @RequestMapping
    public String profile(Model model) {
        model.addAttribute("abouts", profileRepository.findAll());
        return "frontend/profile";
    }
}
