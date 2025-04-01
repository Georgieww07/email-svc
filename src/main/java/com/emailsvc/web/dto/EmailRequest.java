package com.emailsvc.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmailRequest {

    @NotBlank
    private String receiver;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
