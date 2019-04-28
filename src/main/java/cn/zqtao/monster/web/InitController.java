package cn.zqtao.monster.web;

import cn.zqtao.monster.config.permission.NBAuth;
import cn.zqtao.monster.dao.repository.ParamRepository;
import cn.zqtao.monster.model.constant.NoteBlogV4;
import cn.zqtao.monster.model.entity.permission.NBSysResource.ResType;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.service.authority.AuthorityService;
import cn.zqtao.monster.service.param.ParamService;
import cn.zqtao.monster.util.FontAwesomeUtil;
import cn.zqtao.monster.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static cn.zqtao.monster.config.permission.NBAuth.Group;

/**
 * created by Wuwenbin on 2018/7/16 at 14:10
 *
 * @author wuwenbin
 */
@Controller
public class InitController {

    private final ParamService paramService;
    private final ParamRepository paramRepository;
    private final AuthorityService authorityService;

    @Autowired
    public InitController(ParamService paramService, AuthorityService authorityService, ParamRepository paramRepository) {
        this.paramService = paramService;
        this.authorityService = authorityService;
        this.paramRepository = paramRepository;
    }

    @RequestMapping("/init")
    public String init() {
        boolean initialization =
                paramService.getValueByName(NoteBlogV4.Param.INIT_STATUS)
                        .equals(NoteBlogV4.Init.INIT_NOT);
        return initialization ? "init" : "redirect:/";
    }

    @RequestMapping("/init/submit")
    @ResponseBody
    public NBR initSubmit(HttpServletRequest request, String username, String password, String email) {
        paramService.saveInitParam(request.getParameterMap());
        authorityService.initMasterAccount(username, password, email);
        paramRepository.updateValueByName(NoteBlogV4.Param.MAIL_SERVER_ACCOUNT, email);
        paramRepository.updateInitParam("1", "init_status");
        return NBR.ok("初始化设置成功！");
    }

    @NBAuth(value = "user:font:page", remark = "字体图标预览", group = Group.PAGE, type = ResType.NAV_LINK)
    @RequestMapping("/font/list")
    public String b(HttpServletRequest request) {
        String fontAwesome = NBUtils.getFilePathInClassesPath("static/plugins/font-awesome/css/font-awesome.css");
        List<String> a = FontAwesomeUtil.getAllFonts(fontAwesome);
        request.setAttribute("fonts", a);
        return "fonts";
    }

}
