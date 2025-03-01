package com.complaint.infrastructure.client;

import com.complaint.infrastructure.client.dto.CountryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class IpLocationClient {
    private final HttpClient httpClient;
    private final String ipClientUrl;
    private final ObjectMapper objectMapper;

    public IpLocationClient(HttpClient httpClient,
                            @Value("ip.client.url")
                            String ipClientUrl,
                            ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.ipClientUrl = ipClientUrl;
        this.objectMapper = objectMapper;
    }

    public Optional<CountryDto> getCountryFromIpApi() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = prepareHttpRequest();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(response);
    }

    private Optional<CountryDto> handleResponse(HttpResponse<String> response) throws JsonProcessingException {
        return response.statusCode() == 200 ?
                Optional.of(objectMapper.readValue(response.body(), CountryDto.class)) :
                Optional.empty();
    }

    private HttpRequest prepareHttpRequest() throws URISyntaxException {
        URI uri = new URI(ipClientUrl);
        return HttpRequest.newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.of(20, SECONDS))
                .GET()
                .build();
    }
}
