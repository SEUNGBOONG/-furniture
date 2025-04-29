package com.example.demo.login.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public String sendEmail(String toEmail, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        String authNum = createCode();
        session.setAttribute(toEmail, authNum);

        MimeMessage emailForm = createEmailForm(toEmail, authNum);
        emailSender.send(emailForm);
        return authNum;
    }

    private String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0: key.append((char) (random.nextInt(26) + 97)); break;
                case 1: key.append((char) (random.nextInt(26) + 65)); break;
                case 2: key.append(random.nextInt(10)); break;
            }
        }
        return key.toString();
    }

    private MimeMessage createEmailForm(String email, String authNum) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("인증번호");
        message.setFrom("tmdduqflfl@naver.com");
        message.setText(setContext(authNum), "utf-8", "html");
        return message;
    }

    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context);
    }
}
