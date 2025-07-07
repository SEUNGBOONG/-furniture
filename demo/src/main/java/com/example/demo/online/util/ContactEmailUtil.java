package com.example.demo.online.util;

import com.example.demo.common.exception.Setting;
import com.example.demo.online.controller.dto.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactEmailUtil {

    private final JavaMailSender mailSender;

    public void sendContactEmail(ContactRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(Setting.EMAIL.toString());
        helper.setFrom(Setting.EMAIL.toString());
        helper.setSubject(Setting.SET_SUBJECT.toString());
        String content = getString(request);

        helper.setText(content, true);

        mailSender.send(message);
    }

    private static String getString(final ContactRequest request) {
        return "<h2>온라인 문의 내용</h2>" +
                "<p><strong>성명:</strong> " + request.getName() + "</p>" +
                "<p><strong>연락처:</strong> " + request.getPhone() + "</p>" +
                "<p><strong>이메일:</strong> " + request.getEmail() + "</p>" +
                "<p><strong>상담 내용:</strong><br>" + request.getMessage() + "</p>";
    }
}
