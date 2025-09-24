package com.k40.authmicroservice.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String toEmail;
    private String subject;
    private String body;
    private String fromEmail;
    private String senderService;
}