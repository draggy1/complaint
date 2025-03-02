package com.complaint.controller;

import com.complaint.common.ErrorResult;
import com.complaint.common.Result;
import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.dto.ProductDto;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ComplaintControllerIT extends IT {

    private static final String COUNTRY_JSON = """
            {
              "country": "Poland"
            }
            """;

    @Test
    void shouldGetAllComplaintsSuccessfully() {
        // when
        Result<List<ComplaintDto>> result = given()
                .contentType("application/json")
                .when()
                .get("/complaint/all")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(result.statusCode()).isEqualTo(200);
        assertThat(result.data())
                .isNotNull()
                .hasSize(3);

        assertThat(result.data()).containsExactlyInAnyOrder(
                new ComplaintDto(
                        1,
                        new ProductDto(1, "Laptop Pro X"),
                        new ComplainerDto(1, "John", "Smith"),
                        "The laptop overheats after 30 minutes of usage.",
                        OffsetDateTime.parse("2023-03-15T10:15:30Z"),
                        "US",
                        1
                ),
                new ComplaintDto(
                        2,
                        new ProductDto(2, "Smartphone Ultra Y"),
                        new ComplainerDto(2, "Alice", "Johnson"),
                        "The smartphone battery drains too fast.",
                        OffsetDateTime.parse("2023-03-16T11:20:00Z"),
                        "UK",
                        2
                ),
                new ComplaintDto(
                        3,
                        new ProductDto(3, "Wireless Headphones Z"),
                        new ComplainerDto(3, "Michael", "Brown"),
                        "The wireless headphones disconnect randomly.",
                        OffsetDateTime.parse("2023-03-17T12:30:15Z"),
                        "Canada",
                        1
                )
        );
    }

    @Test
    void shouldReturn500AndProperMessageWhenDatabaseErrorOccurs() {
        // given
        POSTGRES.stop();

        // when
        ErrorResult errorResult = given()
                .contentType("application/json")
                .when()
                .get("/complaint/all")
                .then()
                .statusCode(500)
                .extract()
                .as(ErrorResult.class);

        // then
        assertThat(errorResult.statusCode()).isEqualTo(500);
        assertThat(errorResult.message()).isEqualTo("Database error occurred");

        POSTGRES.start();
    }

    private void stubIpLocationApi() {
        stubFor(get(urlEqualTo("http://ip-api.com/json/"))
                .willReturn(okJson(COUNTRY_JSON)));
    }
}