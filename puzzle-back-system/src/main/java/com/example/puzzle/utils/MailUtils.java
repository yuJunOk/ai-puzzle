package com.example.puzzle.utils;

import com.example.puzzle.constant.MailConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author pengYuJun
 */
@Slf4j
@Component
public class MailUtils {
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public boolean sendMail(String mailTitle, String context, String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(mailTitle);
            message.setText(context);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("邮件发送异常", e);
            return false;
        }
    }

    public boolean sendBugMail(String context) {
        return sendMail(MailConstant.BUG_MAIL_TITLE, context, MailConstant.MANAGE_MAIL_ADDRESS);
    }
}
