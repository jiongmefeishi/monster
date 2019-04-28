package cn.zqtao.monster.config.listener;

import lombok.extern.slf4j.Slf4j;
import cn.zqtao.monster.dao.repository.ArticleRepository;
import cn.zqtao.monster.dao.repository.CateRepository;
import cn.zqtao.monster.exception.InitException;
import cn.zqtao.monster.model.entity.NBArticle;
import cn.zqtao.monster.model.entity.NBCate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * created by Wuwenbin on 2018/8/2 at 20:35
 *
 * @author wuwenbin
 */
@Slf4j
@Component
@Order(4)
public class HelloWorldListener implements ApplicationListener<ApplicationReadyEvent> {

    private final CateRepository cateRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public HelloWorldListener(CateRepository cateRepository, ArticleRepository articleRepository) {
        this.cateRepository = cateRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (cateRepository.count() == 0 && articleRepository.count() == 0) {
            log.info("初始化「怪兽营」APP 基本内容，请稍后...");
            long cateId = setFirstCategory();
            setHelloWorldArticle(cateId);
            log.info("初始化「怪兽营」APP 基本内容完毕");
        }
    }


    /**
     * 设置默认文章（HelloWorld）的分类
     *
     * @return
     */
    private Long setFirstCategory() {
        NBCate defaultCate = NBCate.builder()
                .cnName("默认分类")
                .name("def_cate")
                .fontIcon("fa fa-sliders")
                .build();
        defaultCate = cateRepository.save(defaultCate);
        if (defaultCate.getId() != null) {
            return defaultCate.getId();
        }
        throw new InitException("初始化默认文章分类出错");
    }

    private void setHelloWorldArticle(long cateId) {
        final String title = "欢迎使用怪兽营（MONSTER）";
        final String content = "<h2 id=\"h2--noteblog-em-v4-em-\"><a name=\"欢迎使用怪兽营（ <em>MONSTER</em>）\" class=\"reference-link\"></a><span class=\"header-link octicon octicon-link\"></span>欢迎使用怪兽营（ <em>MONSTER</em>）</h2><p>怪兽营是基于springboot+layui编写的一款轻博客系统，可完美搭建您的一款简约博客网站，同时也是非常适用于学习的项目。欢迎大家<a href=\"https://github.com/miyakowork/noteblogv4\" title=\"star\">★star</a>。</p>\n" +
                "<p>如果您觉得此项目帮助到了您，请您给作者<a href=\"https://fly.layui.com/case/u/3425688\" title=\"点个赞\">点个赞</a>。</p>\n" +
                "<p>如果您有任何意见或者建议，请移步QQ群。<br>有任何问题欢迎加QQ群：<a href=\"https://jq.qq.com/?_wv=1027&amp;k=5FgsNj3\" title=\"697053454\">697053454</a>，加入你可以第一时间获取最新信息以及和伙伴们一起交流。</p>\n";
        final String textContent = "欢迎使用怪兽营（MONSTER）怪兽营是基于springboot+layui编写的一款轻博客系统，可完美搭建您的一款简约博客网站，同时也是非常适用于学习的项目。欢迎大家★star。如果您觉得此项目帮助到了您，请您给作者点个赞。如果您有任何意见或者建议，请移步QQ群。有任何问题欢迎加QQ群：697053454，加入你可以第一时间获取最新信息以及和伙伴们一起交流。";
        final String mdContent = "##欢迎使用怪兽营（ *MONSTER4*）\n\n" +
                "怪兽营是基于springboot+layui编写的一款轻博客系统，可完美搭建您的一款简约博客网站，同时也是非常适用于学习的项目。欢迎大家[★star](https://github.com/miyakowork/noteblogv4 \"star\")。\n\n" +
                "如果您觉得此项目帮助到了您，请您给作者[点个赞](https://fly.layui.com/case/u/3425688 \"点个赞\")。\n" +
                "\n如果您有任何意见或者建议，请移步QQ群。\n" +
                "有任何问题欢迎加QQ群：[697053454](https://jq.qq.com/?_wv=1027&k=5FgsNj3 \"697053454\")，加入你可以第一时间获取最新信息以及和伙伴们一起交流。\n";
        final String summary = "欢迎使用怪兽营（MONSTER）怪兽营是基于springboot+layui编写的一款轻博客系统，可完美搭建您的一款简约博客网站，同时也是非常适用于学习的项目。欢迎大家★star。如果您觉得此项目帮助到了您，请";
        NBArticle helloWorldArticle = NBArticle.builder()
                .authorId(1L)
                .cateId(cateId)
                .cover("/static/assets/img/cover.png")
                .content(content)
                .textContent(textContent)
                .cate(cateRepository.getOne(cateId))
                .summary(summary)
                .mdContent(mdContent)
                .post(LocalDateTime.now())
                .draft(false)
                .appreciable(true)
                .title(title).build();
        articleRepository.save(helloWorldArticle);
    }

}
