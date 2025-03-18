package com.emailsvc.web.mapper;

import com.emailsvc.model.Email;
import com.emailsvc.web.dto.EmailResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EmailResponse fromEmail(Email email) {

        return EmailResponse.builder()
                .subject(email.getSubject())
                .createdOn(email.getCreatedOn())
                .status(email.getStatus())
                .receiver(email.getReceiver())
                .build();

    }
}
