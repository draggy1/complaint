package com.complaint.controller;

import com.complaint.service.ComplaintService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
class MockServiceConfig {

    @Bean
    public ComplaintService complaintService() {
        return mock(ComplaintService.class);
    }

}