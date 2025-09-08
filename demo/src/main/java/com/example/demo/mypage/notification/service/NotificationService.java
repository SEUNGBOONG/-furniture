package com.example.demo.mypage.notification.service;

import com.example.demo.mypage.notification.domain.entity.Notification;
import com.example.demo.mypage.notification.domain.repository.NotificationRepository;
import com.example.demo.mypage.notification.util.OrderCompletedEvent;
import com.example.demo.mypage.order.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.context.event.EventListener;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Async
    @EventListener
    public void handleOrderCompleted(OrderCompletedEvent event) {
        Order order = event.getOrder();

        Notification notification = Notification.builder()
                .memberId(order.getMemberId())
                .title("구매 완료되었습니다.")
                .message("주문하신 상품이 성공적으로 결제되었습니다.")
                .build();

        notificationRepository.save(notification);
    }
}
