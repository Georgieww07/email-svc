package com.emailsvc.web;

import com.emailsvc.service.EmailService;
import com.emailsvc.web.dto.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;

import static com.emailsvc.TestBuilder.aRandomEmail;
import static com.emailsvc.TestBuilder.aRandomListOfEmails;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
public class EmailControllerApiTest {

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getRequestToEmails_happyPath() throws Exception {

        when(emailService.getAllEmails()).thenReturn(aRandomListOfEmails());
        MockHttpServletRequestBuilder request = get("/api/v1/emails");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty())
                .andExpect(jsonPath("$[0].subject").isNotEmpty())
                .andExpect(jsonPath("$[0].createdOn").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty())
                .andExpect(jsonPath("$[0].receiver").isNotEmpty());

        verify(emailService, times(1)).getAllEmails();
    }

    @Test
    void getRequestToEmailsWhenThereAreNoEmailsInDb_shouldReturnEmptyList() throws Exception {

        when(emailService.getAllEmails()).thenReturn(Collections.emptyList());
        MockHttpServletRequestBuilder request = get("/api/v1/emails");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(emailService, times(1)).getAllEmails();
    }

    @Test
    void postRequestWithValidBodyToSendEmail_shouldReturn201AndCorrectDtoStructure() throws Exception {

        when(emailService.sendEmail(any())).thenReturn(aRandomEmail());
        EmailRequest emailRequest = EmailRequest.builder()
                .receiver("receiver@email.com")
                .subject("subject")
                .body("body")
                .build();

        MockHttpServletRequestBuilder request = post("/api/v1/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(emailRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("subject").isNotEmpty())
                .andExpect(jsonPath("createdOn").isNotEmpty())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("receiver").isNotEmpty());

        verify(emailService, times(1)).sendEmail(emailRequest);
    }

    @Test
    void postRequestWithInvalidBodyToSendEmail_shouldThrowException() throws Exception {

        EmailRequest emailRequest = EmailRequest.builder()
                .receiver("")
                .subject("")
                .body("")
                .build();

        MockHttpServletRequestBuilder request = post("/api/v1/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(emailRequest));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}