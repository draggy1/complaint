package com.complaint.service;

import com.complaint.infrastructure.IpLocationClient;
import com.complaint.infrastructure.common.exception.CountryNotFoundException;
import com.complaint.infrastructure.common.exception.IpLocationClientException;
import com.complaint.service.model.Country;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class IpLocationService {
    private final IpLocationClient client;

    public IpLocationService(IpLocationClient client) {
        this.client = client;
    }

    public Country getSubmittersCountry() {
        try {
            return client.getCountryFromIpApi()
                    .map(dto -> new Country(dto.country()))
                    .orElseThrow(() -> new CountryNotFoundException("Country not found"));
        } catch (URISyntaxException | IOException e) {
            throw new IpLocationClientException("IP location client failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IpLocationClientException("Thread was interrupted while fetching IP location", e);
        }
    }
}
