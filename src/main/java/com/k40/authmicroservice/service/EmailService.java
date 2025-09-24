package com.k40.authmicroservice.service;

import com.k40.authmicroservice.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-service", url = "${email.service.url}")
public interface EmailService {

    @PostMapping
    void sendEmail(@RequestBody EmailRequest emailRequest);
}
