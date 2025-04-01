package com.emailsvc.service;

import com.emailsvc.model.Email;
import com.emailsvc.model.EmailStatus;
import com.emailsvc.repository.EmailRepository;
import com.emailsvc.web.dto.EmailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceUTest {

    @Mock
    private EmailRepository emailRepository;
    @Mock
    private MailSender mailSender;

    @InjectMocks
    private EmailService emailService;


    @Test
    void givenEmailRequest_whenSendEmail_thenReturnEmailWithStatusSucceeded() {
        //Given
        EmailRequest emailRequest = EmailRequest.builder()
                .receiver("testReceiver@email.com")
                .subject("testSubject")
                .body("testBody")
                .build();

        Email savedEmail = Email.builder()
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .createdOn(LocalDateTime.now())
                .receiver(emailRequest.getReceiver())
                .status(EmailStatus.SUCCEEDED)
                .build();

        when(emailRepository.save(any())).thenReturn(savedEmail);

        //When
        Email email = emailService.sendEmail(emailRequest);

        //Then
        assertEquals(EmailStatus.SUCCEEDED, email.getStatus());

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(any());
    }

    @Test
    void givenEmailRequest_whenSendEmail_thenReturnEmailWithStatusFailed() {
        //Given
        EmailRequest emailRequest = EmailRequest.builder()
                .receiver("testReceiver@email.com")
                .subject("testSubject")
                .body("testBody")
                .build();

        Email savedEmail = Email.builder()
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .createdOn(LocalDateTime.now())
                .receiver(emailRequest.getReceiver())
                .status(EmailStatus.FAILED)
                .build();

        doThrow(new RuntimeException("Mail server down")).when(mailSender).send(any(SimpleMailMessage.class));
        when(emailRepository.save(any())).thenReturn(savedEmail);

        //When
        Email email = emailService.sendEmail(emailRequest);

        //Then
        assertEquals(EmailStatus.FAILED, email.getStatus());

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(any());
    }

    @Test
    void testGetAllEmails() {
        //Given
        List<Email> emails = List.of(new Email(), new Email(), new Email());
        when(emailRepository.findAllByOrderByCreatedOnDesc()).thenReturn(emails);

        //When
        List<Email> allEmails = emailService.getAllEmails();

        //Then
        assertEquals(emails.size(), allEmails.size());
        verify(emailRepository, times(1)).findAllByOrderByCreatedOnDesc();
    }
}
