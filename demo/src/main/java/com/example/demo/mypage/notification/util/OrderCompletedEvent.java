package com.example.demo.mypage.notification.util;

import com.example.demo.mypage.order.domain.entity.Order;
import lombok.Getter;

@Getter
public class OrderCompletedEvent {
    private final Order order;

    public OrderCompletedEvent(Order order) {
        this.order = order;
    }
}
