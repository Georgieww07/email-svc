package com.emailsvc.web;

import com.emailsvc.model.Email;
import com.emailsvc.service.EmailService;
import com.emailsvc.web.dto.EmailRequest;
import com.emailsvc.web.dto.EmailResponse;
import com.emailsvc.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {

        Email email = emailService.sendEmail(emailRequest);

        EmailResponse emailResponse = DtoMapper.fromEmail(email);

        return ResponseEntity.status(HttpStatus.CREATED).body(emailResponse);
    }

    @GetMapping
    public ResponseEntity<List<EmailResponse>> getAllEmails() {

        List<Email> allEmails = emailService.getAllEmails();

        List<EmailResponse> response = allEmails.stream().map(DtoMapper::fromEmail).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
