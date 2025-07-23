package com.sanjat.notification_service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import com.sanjat.notification_service.model.Notification;
import com.sanjat.notification_service.model.NotificationType;
import com.sanjat.notification_service.service.EmailService;

@Configuration
public class NotificationConsumers {

    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumers.class);

    public NotificationConsumers(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${app.notification.queue}")
    public void consume(Notification notification) {
        logger.info("Primljena notifikacija: {}", notification);

        String email = notification.getEmail();

        if (email == null || email.isEmpty()) {
            logger.warn("Email nije dostupan!");
            return;
        }
        String course = notification.getCourseName();
        System.out.println("Naziv kursa: " + course);
        if (notification.getType() == NotificationType.USPJESNO_UPISAN) {
            String subject = "Upis na kurs";
            String message = "Uspjesno ste upisani na kurs: " + course;

            emailService.sendEmail(email, subject, message);
        } else if (notification.getType() == NotificationType.OCJENJEN) {
            String subject = "Nova ocjena";
            String message = "Vasa ocjena na kursu: " + course + " je objavljena.";

            emailService.sendEmail(email, subject, message);
        } else {
            logger.warn("Nepoznat status notifikacije!");

        }
        ;
    }
}
