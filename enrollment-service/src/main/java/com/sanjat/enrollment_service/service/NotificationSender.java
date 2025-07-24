package com.sanjat.enrollment_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sanjat.enrollment_service.dtos.Notification;

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

    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @Async
    public void sendNotification(Notification notificationMessage) {
        rabbitTemplate.convertAndSend(exchange, routingKey, notificationMessage);

    }

}
