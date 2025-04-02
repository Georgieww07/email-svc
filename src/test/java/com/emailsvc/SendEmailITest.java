package com.emailsvc;

import com.emailsvc.model.Email;
import com.emailsvc.model.EmailStatus;
import com.emailsvc.service.EmailService;
import com.emailsvc.web.dto.EmailRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class SendEmailITest {

    @Autowired
    private EmailService emailService;
    @MockitoBean
    private MailSender mailSender;


    @Test
    void sendEmail_happyPath(){

        //Given
        EmailRequest emailRequest = EmailRequest.builder()
                .receiver("test@gmail.com")
                .subject("testSubject")
                .body("testBody")
                .build();

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //When
        emailService.sendEmail(emailRequest);

        //Then
        Email email = emailService.getAllEmails().get(0);
        assertNotNull(email);
        assertEquals(emailRequest.getReceiver(), email.getReceiver());
        assertEquals(emailRequest.getSubject(), email.getSubject());
        assertEquals(emailRequest.getBody(), email.getBody());
        assertEquals(EmailStatus.SUCCEEDED, email.getStatus());
    }
}
