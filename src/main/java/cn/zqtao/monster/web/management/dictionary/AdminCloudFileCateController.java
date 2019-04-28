package cn.zqtao.monster.web.management.dictionary;

import cn.hutool.core.util.StrUtil;
import cn.zqtao.monster.config.permission.NBAuth;
import cn.zqtao.monster.dao.repository.CloudFileCateRepository;
import cn.zqtao.monster.dao.repository.CloudFileRepository;
import cn.zqtao.monster.model.entity.NBCloudFileCate;
import cn.zqtao.monster.model.pojo.framework.LayuiTable;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.model.pojo.framework.Pagination;
import cn.zqtao.monster.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static cn.zqtao.monster.config.permission.NBAuth.Group.AJAX;
import static cn.zqtao.monster.config.permission.NBAuth.Group.ROUTER;
import static cn.zqtao.monster.model.entity.permission.NBSysResource.ResType.NAV_LINK;

/**
 * created by Wuwenbin on 2018/8/15 at 16:29
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management/dictionary/cloudFileCate")
public class AdminCloudFileCateController extends BaseController {

    private final CloudFileRepository cloudFileRepository;
    private final CloudFileCateRepository cloudFileCateRepository;

    @Autowired
    public AdminCloudFileCateController(CloudFileRepository cloudFileRepository, CloudFileCateRepository cloudFileCateRepository) {
        this.cloudFileRepository = cloudFileRepository;
        this.cloudFileCateRepository = cloudFileCateRepository;
    }

    @RequestMapping
    @NBAuth(value = "management:cloudFileCate:page", remark = "云文件分类管理页面", group = ROUTER, type = NAV_LINK)
    public String cate() {
        return "management/dictionary/cloudFileCate";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:cloudFileCate:list", remark = "云文件分类管理分页数据", group = AJAX)
    public LayuiTable<NBCloudFileCate> cateList(Pagination<NBCloudFileCate> catePage) {
        //jpa分页是从0开始
        Pageable pageable = getPageable(catePage);
        Page<NBCloudFileCate> page = cloudFileCateRepository.findAll(pageable);
        return layuiTable(page, pageable);
    }

    @RequestMapping("/create")
    @NBAuth(value = "management:cloudFileCate:create", remark = "添加云文件分类操作", group = AJAX)
    @ResponseBody
    public NBR cateCreate(NBCloudFileCate cate) {
        if (cate != null && StrUtil.isNotEmpty(cate.getName())) {
            return ajaxDone(
                    () -> cloudFileCateRepository.findCateCount(cate) == 0,
                    () -> ajaxDone(() -> cloudFileCateRepository.save(cate) != null, () -> "添加云文件分类信息"),
                    () -> "已存在此云文件分类信息"
            );
        }
        return NBR.error("添加云文件分类信息有误！");
    }

    @RequestMapping("/delete")
    @NBAuth(value = "management:cloudFileCate:delete", remark = "删除云文件分类操作", group = AJAX)
    @ResponseBody
    public NBR delete(Long cateId) {
        return ajaxDone(
                () -> cloudFileRepository.countByCateId(cateId) == 0,
                () -> ajaxDone(cateId, cloudFileCateRepository::deleteById, () -> "删除云文件分类"),
                () -> "此云文件分类下还有项目！"
        );
    }

    @RequestMapping("/update")
    @NBAuth(value = "management:cloudFileCate:update", remark = "修改云文件分类操作", group = AJAX)
    @ResponseBody
    public NBR update(@Valid NBCloudFileCate cate, BindingResult result) {
        if (result.getErrorCount() == 0) {
            return ajaxDone(
                    () -> cloudFileCateRepository.findCateCount(cate) == 0,
                    () -> ajaxDone(() -> cloudFileCateRepository.save(cate) != null, () -> "修改云文件分类信息"),
                    () -> "已存在此云文件分类信息（云文件分类名/中文名重复）"
            );
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }
}
