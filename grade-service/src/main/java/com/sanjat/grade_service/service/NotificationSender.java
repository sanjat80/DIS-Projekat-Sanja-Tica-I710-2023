package com.sanjat.grade_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sanjat.grade_service.dtos.Notification;

@Service
public class NotificationSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.notification.exchange}")
    private String exchange;

    @Value("${app.notification.routing-key}")
    private String routingKey;

    public NotificationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void sendNotification(Notification notificationMessage) {
        System.out.println(
                "Koristi se message converter: " + rabbitTemplate.getMessageConverter().getClass().getSimpleName());

        rabbitTemplate.convertAndSend(exchange, routingKey, notificationMessage);

    }

}
