package cn.zqtao.monster.service.mail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.zqtao.monster.dao.repository.ParamRepository;
import cn.zqtao.monster.model.constant.Monster;
import cn.zqtao.monster.model.entity.NBArticle;
import cn.zqtao.monster.model.entity.permission.NBSysUser;
import cn.zqtao.monster.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Wuwenbin on 2019-01-08 at 22:27
 *
 * @author wuwenbin
 */
@Service
public class MailServiceImpl implements MailService {
    private final ParamRepository paramRepository;


    @Autowired
    public MailServiceImpl(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;

    }

    @Override
    public void sendNoticeMail(String site, NBArticle article, String comment) {
        String host = paramRepository.findByName(Monster.Param.MAIL_SMPT_SERVER_ADDR).getValue();
        String port = paramRepository.findByName(Monster.Param.MAIL_SMPT_SERVER_PORT).getValue();
        String from = paramRepository.findByName(Monster.Param.MAIL_SERVER_ACCOUNT).getValue();
        String user = paramRepository.findByName(Monster.Param.MAIL_SENDER_NAME).getValue();
        String pass = paramRepository.findByName(Monster.Param.MAIL_SERVER_PASSWORD).getValue();
        if (StrUtil.isNotEmpty(host)
                && StrUtil.isNotEmpty(port)
                && StrUtil.isNotEmpty(from)
                && StrUtil.isNotEmpty(pass)) {
            MailAccount account = new MailAccount();
            account.setHost(host);
            account.setPort(Integer.valueOf(port));
            account.setAuth(true);
            account.setSslEnable(true);
            account.setFrom(from);
            account.setUser(user);
            account.setPass(pass);
            NBSysUser u = NBUtils.getSessionUser();
            String targetMail = u != null ? u.getEmail() : host;

            String subject = "你的文章 - 【{}】 有人发表评论了";
            String content = "<p>您发布的文章{}有人发表了新评论：</p>" +
                    "<p style='font-style:italic;'>{}</p>" +
                    "<p>，请<a href='{}article/{}' target='_blank'>查看</a></p>";
            MailUtil.send(account,
                    CollUtil.newArrayList(targetMail),
                    StrUtil.format(subject, article.getTitle()),
                    StrUtil.format(content, article.getTitle(), comment, site, article.getId()),
                    true);
        }
    }
}
