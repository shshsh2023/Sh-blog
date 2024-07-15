package com.sh.utils.mail;

import com.alibaba.fastjson.JSON;
import com.sh.constants.CommonConst;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class MailUtil {

    /**
     * 1. 来源人名
     * 2. 来源内容
     */
    public static final String originalText = "<hr style=\"border: 1px dashed #ef859d2e;margin: 20px 0\">\n" +
            "            <div>\n" +
            "                <div style=\"font-size: 18px;font-weight: bold;color: #C5343E\">\n" +
            "                    %s\n" +
            "                </div>\n" +
            "                <div style=\"margin-top: 6px;font-size: 16px;color: #000000\">\n" +
            "                    <p>\n" +
            "                        %s\n" +
            "                    </p>\n" +
            "                </div>\n" +
            "            </div>";

    /**
     * 发件人
     */
    public static final String replyMail = "你之前的评论收到来自 %s 的回复";
    public static final String commentMail = "你的文章 %s 收到来自 %s 的评论";
    public static final String messageMail = "你收到来自 %s 的留言";
    public static final String loveMail = "你收到来自 %s 的祝福";
    public static final String imMail = "你收到来自 %s 的消息";
    public static final String notificationMail = "你收到来自 %s 的订阅";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private Environment env;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 1. 网站名称
     * 2. 邮件类型
     * 3. 发件人
     * 4. 发件内容
     * 5. originalText
     * 6. 网站名称
     */
    @Getter
    private String mailText;

    @PostConstruct
    public void init() {
        this.mailText = "<div style=\"font-family: serif;line-height: 22px;padding: 30px\">\n" +
                "    <div style=\"display: flex;flex-direction: column;align-items: center\">\n" +
                "        <div style=\"margin: 10px auto 20px;text-align: center\">\n" +
                "            <div style=\"line-height: 32px;font-size: 26px;font-weight: bold;color: #000000\">\n" +
                "                嘿！你在 %s 中收到一条新消息。\n" +
                "            </div>\n" +
                "            <div style=\"font-size: 16px;font-weight: bold;color: rgba(0, 0, 0, 0.19);margin-top: 21px\">\n" +
                "                %s\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div style=\"min-width: 250px;max-width: 800px;min-height: 128px;background: #F7F7F7;border-radius: 10px;padding: 32px\">\n" +
                "            <div>\n" +
                "                <div style=\"font-size: 18px;font-weight: bold;color: #C5343E\">\n" +
                "                    %s\n" +
                "                </div>\n" +
                "                <div style=\"margin-top: 6px;font-size: 16px;color: #000000\">\n" +
                "                    <p>\n" +
                "                        %s\n" +
                "                    </p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            %s\n" +
                "            <a style=\"width: 150px;height: 38px;background: #ef859d38;border-radius: 32px;display: flex;align-items: center;justify-content: center;text-decoration: none;margin: 40px auto 0\"\n" +
                "               href=\"https://localhost:5173/\" target=\"_blank\">\n" +
                "                <span style=\"color: #DB214B\">有朋自远方来</span>\n" +
                "            </a>\n" +
                "        </div>\n" +
                "        <div style=\"margin-top: 20px;font-size: 12px;color: #00000045\">\n" +
                "            此邮件由 %s 自动发出，直接回复无效（一天最多发送 " + CommonConst.COMMENT_IM_MAIL_COUNT + " 条通知邮件和 " + CommonConst.CODE_MAIL_COUNT + " 条验证码邮件），退订请联系站长。\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    @Async
    public void sendMailMessage(List<String> to, String subject, String text) {
        log.info("发送邮件===================");
        log.info("to：{}", JSON.toJSONString(to));
        log.info("subject：{}", subject);
        log.info("text：{}", text);
        try {
            MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
                //true代表支持复杂的类型
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                //邮件发信人
                message.setFrom(env.getProperty("email.username"));
                //邮件收信人1或多个
                message.setTo(to.toArray(new String[0]));
                //邮件主题
                message.setSubject(subject);
                //邮件内容
                message.setText(text, true);
                //邮件发送时间
                message.setSentDate(new Date());
            };

            javaMailSender.setUsername(env.getProperty("email.username"));
            javaMailSender.setPassword(env.getProperty("email.password"));

            javaMailSender.send(mimeMessagePreparator);
            log.info("发送成功==================");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送失败==================");
            log.error(e.getMessage());
        }
    }
}
