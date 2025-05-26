package com.example.demo.login.email.util;

import com.example.demo.login.email.domain.EmailCode;

import com.example.demo.login.email.infrastruture.EmailForm;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailSenderUtil {

    private final JavaMailSender emailSender;

    private final EmailCode emailCode;
    private final EmailForm form;

    public String sendEmail(String toEmail, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        return getString(toEmail, session);
    }

    private String getString(final String toEmail, final HttpSession session) throws MessagingException, UnsupportedEncodingException {
        String authNum = emailCode.createCode();
        session.setAttribute(toEmail, authNum);
        MimeMessage emailForm = form.createEmailForm(toEmail, authNum);
        emailSender.send(emailForm);
        return authNum;
    }

}
