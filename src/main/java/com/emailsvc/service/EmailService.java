package com.emailsvc.service;

import com.emailsvc.model.Email;
import com.emailsvc.model.EmailStatus;
import com.emailsvc.repository.EmailRepository;
import com.emailsvc.web.dto.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    @Autowired
    public EmailService(EmailRepository emailRepository, MailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }

    public Email sendEmail(EmailRequest emailRequest) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getReceiver());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());

        Email email = Email.builder()
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .createdOn(LocalDateTime.now())
                .receiver(emailRequest.getReceiver())
                .build();

        try {
            mailSender.send(message);
            email.setStatus(EmailStatus.SUCCEEDED);
        } catch (Exception e) {
            email.setStatus(EmailStatus.FAILED);
            log.warn("There was an error sending an email to [%s]. Reason: [%s].".formatted(emailRequest.getReceiver(), e.getMessage()));
        }

        return emailRepository.save(email);
    }

    public List<Email> getAllEmails() {
        return emailRepository.findAllByOrderByCreatedOnDesc();
    }
}
