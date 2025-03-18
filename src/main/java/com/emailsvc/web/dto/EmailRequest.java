package com.emailsvc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class EmailRequest {

    @NotBlank
    private String receiver;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
