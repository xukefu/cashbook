package com.xkf.cashbook.web.service.impl;

import com.xkf.cashbook.web.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件服务
 *
 * @author xukf01
 */
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender mailSender;
    /**
     * 发送者
     */
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {

        System.out.println("sendAttachmentsMail start");
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            // 发送附件
            File file = new File(filePath);
            file = ResourceUtils.getFile(file.getAbsolutePath());
            helper.addAttachment("cash_book.sql", file);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("sendAttachmentsMail err");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("sendAttachmentsMail err");
        } finally {

        }


    }

    @Override
    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setText(content);
        message.setSubject(subject);

        mailSender.send(message);
    }

}
