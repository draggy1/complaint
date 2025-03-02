package com.complaint.service;

import com.complaint.infrastructure.client.IpLocationClient;
import com.complaint.infrastructure.client.dto.CountryDto;
import com.complaint.infrastructure.client.exception.CountryNotFoundException;
import com.complaint.infrastructure.client.exception.IpLocationClientException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class IpLocationService {
    private final IpLocationClient client;

    public IpLocationService(IpLocationClient client) {
        this.client = client;
    }

    public String getCountryByIp() {
        try {
            return client.getCountryFromIpApi()
                    .map(CountryDto::country)
                    .orElseThrow(() -> new CountryNotFoundException("Country not found"));
        } catch (URISyntaxException | IOException e) {
            throw new IpLocationClientException("IP location client failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IpLocationClientException("Thread was interrupted while fetching IP location", e);
        }
    }
}
