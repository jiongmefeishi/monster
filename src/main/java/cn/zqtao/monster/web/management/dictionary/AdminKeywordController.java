package cn.zqtao.monster.web.management.dictionary;

import cn.hutool.core.util.StrUtil;
import cn.zqtao.monster.config.permission.NBAuth;
import cn.zqtao.monster.dao.repository.KeywordRepository;
import cn.zqtao.monster.model.entity.NBKeyword;
import cn.zqtao.monster.model.pojo.framework.LayuiTable;
import cn.zqtao.monster.model.pojo.framework.NBR;
import cn.zqtao.monster.model.pojo.framework.Pagination;
import cn.zqtao.monster.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
 * created by Wuwenbin on 2018/8/15 at 16:28
 *
 * @author wuwenbin
 */
@Controller
@RequestMapping("/management/dictionary/keyword")
public class AdminKeywordController extends BaseController {

    private final KeywordRepository keywordRepository;

    @Autowired
    public AdminKeywordController(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @RequestMapping
    @NBAuth(value = "management:keyword:page", remark = "关键字管理页面", group = ROUTER, type = NAV_LINK)
    public String cate() {
        return "management/dictionary/keyword";
    }

    @RequestMapping("/list")
    @ResponseBody
    @NBAuth(value = "management:keyword:list", remark = "关键字管理分页数据", group = AJAX)
    public LayuiTable<NBKeyword> keywordList(Pagination<NBKeyword> keywordPagination) {
        Pageable pageable = getPageable(keywordPagination);
        Page<NBKeyword> page = keywordRepository.findAll(pageable);
        return layuiTable(page, pageable);
    }

    @RequestMapping("/create")
    @NBAuth(value = "management:keyword:create", remark = "添加关键字操作", group = AJAX)
    @ResponseBody
    public NBR keywordCreate(NBKeyword keyword) {
        if (keyword != null && StrUtil.isNotEmpty(keyword.getWords())) {
            return ajaxDone(
                    () -> keywordRepository.count(Example.of(NBKeyword.builder().words(keyword.getWords()).build())) == 0,
                    () -> ajaxDone(() -> keywordRepository.save(keyword) != null, () -> "添加关键字信息"),
                    () -> "已存在此关键字"
            );
        }
        return NBR.error("添加关键字描述有误！");
    }

    @RequestMapping("/delete")
    @NBAuth(value = "management:keyword:delete", remark = "删除关键字操作", group = AJAX)
    @ResponseBody
    public NBR delete(Long id) {
        return ajaxDone(id, keywordRepository::deleteById, () -> "删除关键字");
    }

    @RequestMapping("/update")
    @NBAuth(value = "management:keyword:update", remark = "更新关键字文本操作", group = AJAX)
    @ResponseBody
    public NBR wordsUpdate(@Valid NBKeyword keyword, BindingResult result) {
        if (result.getErrorCount() == 0) {
            return ajaxDone(
                    () -> keywordRepository.count(Example.of(NBKeyword.builder().words(keyword.getWords()).build())) == 0,
                    () -> ajaxDone(() -> keywordRepository.save(keyword) != null, () -> "修改关键字信息"),
                    () -> "已存在此关键字信息（关键字重复）"
            );
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }

    @RequestMapping("/update/enable")
    @NBAuth(value = "management:keyword:enable_update", remark = "更新关键字状态操作", group = AJAX)
    @ResponseBody
    public NBR enableUpdate(NBKeyword keyword) {
        return ajaxDone(
                () -> keywordRepository.updateEnableById(keyword.getEnable(), keyword.getId()) == 1,
                () -> "修改关键字可用状态"
        );
    }


}
