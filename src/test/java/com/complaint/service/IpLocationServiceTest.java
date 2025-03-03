package com.complaint.service;

import com.complaint.infrastructure.client.IpLocationClient;
import com.complaint.infrastructure.client.exception.CountryNotFoundException;
import com.complaint.infrastructure.client.exception.IpLocationClientException;
import com.complaint.infrastructure.client.dto.CountryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpLocationServiceTest {

    @Mock
    private IpLocationClient client;

    @InjectMocks
    private IpLocationService ipLocationService;

    @Test
    void shouldGetSubmittersCountrySuccessfully() throws URISyntaxException, IOException, InterruptedException {

        //given
        when(client.getCountryFromIpApi()).thenReturn(Optional.of(new CountryDto("Poland")));

        //when
        String actual = ipLocationService.getCountryByIp();

        //then
        assertThat(actual)
                .isEqualTo("Poland");
    }

    @Test
    void shouldFailGetSubmittersCountryBecauseOfCountryNotBeingFound() throws URISyntaxException, IOException, InterruptedException {

        //given
        when(client.getCountryFromIpApi()).thenReturn(Optional.empty());

        //when
        CountryNotFoundException thrown =
                (CountryNotFoundException) catchThrowable(ipLocationService::getCountryByIp);

        // then
        assertThatThrownBy(ipLocationService::getCountryByIp)
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessage("Country not found");

        assertThat(thrown)
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessage("Country not found");
    }

    @Test
    void shouldFailGetSubmittersCountryBecauseOfURISyntaxException() throws URISyntaxException, IOException, InterruptedException {

        //given
        URISyntaxException givenException = new URISyntaxException("Some input", "Some reason");
        when(client.getCountryFromIpApi()).thenThrow(givenException);

        //when
        IpLocationClientException thrown =
                (IpLocationClientException) catchThrowable(ipLocationService::getCountryByIp);

        //then
        assertThat(thrown)
                .isInstanceOf(IpLocationClientException.class)
                .hasMessage("IP location client failed")
                .hasCause(givenException);
    }

    @Test
    void shouldFailGetSubmittersCountryBecauseOfIOException() throws URISyntaxException, IOException, InterruptedException {

        //given
        IOException givenException = new IOException("Some exception");
        when(client.getCountryFromIpApi()).thenThrow(givenException);

        //when
        IpLocationClientException thrown =
                (IpLocationClientException) catchThrowable(ipLocationService::getCountryByIp);

        //then
        assertThat(thrown)
                .isInstanceOf(IpLocationClientException.class)
                .hasMessage("IP location client failed")
                .hasCause(givenException);
    }

    @Test
    void shouldFailGetSubmittersCountryBecauseOfInterruptedException() throws URISyntaxException, IOException, InterruptedException {

        //given
        InterruptedException givenException = new InterruptedException();
        when(client.getCountryFromIpApi()).thenThrow(givenException);

        //when
        IpLocationClientException thrown =
                (IpLocationClientException) catchThrowable(ipLocationService::getCountryByIp);

        //then
        assertThat(thrown)
                .isInstanceOf(IpLocationClientException.class)
                .hasMessage("Thread was interrupted while fetching IP location")
                .hasCause(givenException);
    }
}
