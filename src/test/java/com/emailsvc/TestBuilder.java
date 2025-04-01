package com.emailsvc;

import com.emailsvc.model.Email;
import com.emailsvc.model.EmailStatus;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestBuilder {

    public static List<Email> aRandomListOfEmails() {
        Email firstEmail = Email.builder()
                .id(UUID.randomUUID())
                .subject("Test Subject")
                .body("Test Body")
                .createdOn(LocalDateTime.now())
                .status(EmailStatus.SUCCEEDED)
                .receiver("Test Receiver")
                .build();

        Email secondEmail = Email.builder()
                .id(UUID.randomUUID())
                .subject("Test Subject 2")
                .body("Test Body 2")
                .createdOn(LocalDateTime.now())
                .status(EmailStatus.SUCCEEDED)
                .receiver("Test Receiver 2")
                .build();

        return List.of(firstEmail, secondEmail);
    }

    public static Email aRandomEmail() {

        return Email.builder()
                .id(UUID.randomUUID())
                .subject("Test Subject")
                .body("Test Body")
                .createdOn(LocalDateTime.now())
                .status(EmailStatus.SUCCEEDED)
                .receiver("Test Receiver")
                .build();
    }
}
