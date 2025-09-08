package com.example.demo.mypage.notification.controller;

import com.example.demo.login.global.annotation.Member;
import com.example.demo.mypage.notification.domain.entity.Notification;
import com.example.demo.mypage.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/my")
    public List<Notification> getMyNotifications(@Member Long memberId) {
        return notificationRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    }
}
