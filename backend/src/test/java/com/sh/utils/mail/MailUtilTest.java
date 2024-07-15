package com.sh.utils.mail;

import com.sh.config.PoetryResult;
import com.sh.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author song
 * @version 0.0.1
 * @date 2024/7/15 19:14
 */
@SpringBootTest
class MailUtilTest {

    @Autowired
    private UserService userService;


    @Test
    void sendMailMessage() {
//        mailUtil.sendMailMessage(List.of("643947568@qq.com"), "2");
        PoetryResult codeForForgetPassword = userService.getCodeForForgetPassword("643947568@qq.com", 2);
        System.out.println(codeForForgetPassword);

    }
}