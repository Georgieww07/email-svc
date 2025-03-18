package com.emailsvc.web.dto;

import com.emailsvc.model.EmailStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailResponse {

    private String subject;

    private LocalDateTime createdOn;

    private EmailStatus status;

    private String receiver;
}
