package cn.zqtao.monster.service.mail;

import cn.zqtao.monster.model.entity.NBArticle;

public interface MailService {

    /**
     * 发送评论通知邮件
     * @param site
     * @param article
     * @param comment
     */
    void sendNoticeMail(String site, NBArticle article, String comment);
}
