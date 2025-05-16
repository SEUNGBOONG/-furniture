package com.example.demo.login.email.service;

import com.example.demo.login.email.domain.EmailCode;

import com.example.demo.login.email.infrastruture.EmailForm;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final EmailCode emailCode;
    private final EmailForm form;

    public String sendEmail(String toEmail, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        String authNum = emailCode.createCode();
        session.setAttribute(toEmail, authNum);
        MimeMessage emailForm = form.createEmailForm(toEmail, authNum);
        emailSender.send(emailForm);
        return authNum;
    }


}
