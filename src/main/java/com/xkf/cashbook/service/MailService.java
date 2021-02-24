package com.xkf.cashbook.service;

/** 邮件服务
 * @author xukf01
 */
public interface MailService {

    /**
     * 发送普通邮件
     *
     * @param to      接收方
     * @param subject 主题
     * @param content 正文
     * @param filePath 附件路径
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * 发送普通邮件
     *
     * @param to      接收方
     * @param subject 主题
     * @param content 征文
     */
    void sendMail(String to, String subject, String content);
}
