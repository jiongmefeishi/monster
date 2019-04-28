package cn.zqtao.monster.web.management;

import cn.zqtao.monster.config.permission.NBAuth;
import cn.zqtao.monster.dao.repository.MenuRepository;
import cn.zqtao.monster.model.entity.permission.NBSysMenu;
import cn.zqtao.monster.model.entity.permission.NBSysResource.ResType;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.model.pojo.business.MenuTree;
import cn.zqtao.monster.service.dashboard.DashboardService;
import cn.zqtao.monster.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

import static cn.zqtao.monster.config.permission.NBAuth.Group;

/**
 * created by Wuwenbin on 2018/7/21 at 23:31
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management")
public class AdminIndexController {

    private final MenuRepository menuRepository;
    private final DashboardService dashboardService;

    @Autowired
    public AdminIndexController(MenuRepository menuRepository, DashboardService dashboardService) {
        this.menuRepository = menuRepository;
        this.dashboardService = dashboardService;
    }

    @RequestMapping("/index")
    @NBAuth(value = "management:index:page", remark = "后台管理首页", type = ResType.OTHER, group = Group.PAGE)
    public String index(Model model) {
        NBSysUser user = NBUtils.getSessionUser();

        Long userRoleId = Objects.requireNonNull(user).getDefaultRoleId();
        List<NBSysMenu> menus = menuRepository.findAllByRoleIdOrderBy(userRoleId, true);
        List<MenuTree> menuTrees = MenuTree.buildByRecursive(menus);
        model.addAttribute("menus", menuTrees);

        String avatar = user.getAvatar();
        model.addAttribute("avatar", avatar);
        return "admin_index";
    }

    @RequestMapping("/dashboard")
    @NBAuth(value = "management:index:dashboard", remark = "管理页面仪表盘界面", type = ResType.NAV_LINK, group = Group.ROUTER)
    public String dashboard(Model model) {
        model.addAttribute("data", dashboardService.calculateData());
        model.addAttribute("c", dashboardService.findLatestComment());
        model.addAttribute("tableData", dashboardService.findTableStatistics());
        return "management/dashboard";
    }
}

