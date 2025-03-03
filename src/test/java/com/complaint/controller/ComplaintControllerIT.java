package com.complaint.controller;

import com.complaint.common.ErrorResult;
import com.complaint.controller.dto.CreateComplaintRequest;
import com.complaint.controller.dto.UpdateComplaintContentRequest;
import com.complaint.service.dto.ComplainerDto;
import com.complaint.service.dto.ComplaintDto;
import com.complaint.service.dto.ProductDto;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static io.restassured.RestAssured.given;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = "spring.profiles.active=test")
class ComplaintControllerIT extends IT {

    private static final String COUNTRY_JSON = """
            {
              "country": "Poland"
            }
            """;
    private static final ComplaintDto COMPLAINT_DTO_1 = new ComplaintDto(
            1,
            new ProductDto(1, "Laptop Pro X"),
            new ComplainerDto(1, "John", "Smith"),
            "The laptop overheats after 30 minutes of usage.",
            OffsetDateTime.parse("2023-03-15T10:15:30Z"),
            "US",
            1
    );
    private static final ComplaintDto COMPLAINT_DTO_2 = new ComplaintDto(
            2,
            new ProductDto(2, "Smartphone Ultra Y"),
            new ComplainerDto(2, "Alice", "Johnson"),
            "The smartphone battery drains too fast.",
            OffsetDateTime.parse("2023-03-16T11:20:00Z"),
            "UK",
            2
    );
    private static final ComplaintDto COMPLAINT_DTO_3 = new ComplaintDto(
            3,
            new ProductDto(3, "Wireless Headphones Z"),
            new ComplainerDto(3, "Michael", "Brown"),
            "The wireless headphones disconnect randomly.",
            OffsetDateTime.parse("2023-03-17T12:30:15Z"),
            "Canada",
            1
    );
    private  static final ComplaintDto COMPLAINT_DTO_4 = new ComplaintDto(
            4,
            new ProductDto(2, "Smartphone Ultra Y"),
            new ComplainerDto(1, "John", "Smith"),
            "New complaint content",
            OffsetDateTime.now(),
            "Poland",
            1
    );

    @Test
    void shouldGetAllComplaintsSuccessfully() {
        // when
        List<ComplaintDto> complaints = given()
                .contentType("application/json")
                .when()
                .get("/complaint/all")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        assertThat(complaints)
                .isNotNull()
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        COMPLAINT_DTO_1,
                        COMPLAINT_DTO_2,
                        COMPLAINT_DTO_3
                );
    }

    @Test
    void shouldGetComplaintByIdSuccessfully() {
        // when
        ComplaintDto complaint = given()
                .contentType("application/json")
                .when()
                .get("/complaint/1")
                .then()
                .statusCode(200)
                .extract()
                .as(ComplaintDto.class);

        // then
        assertThat(complaint).isEqualTo(COMPLAINT_DTO_1);
    }

    @Test
    void shouldReturn404WhenComplaintNotFound() {
        // when
        ErrorResult errorResult = given()
                .contentType("application/json")
                .when()
                .get("/complaint/999")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResult.class);

        // then
        assertThat(errorResult.statusCode()).isEqualTo(404);
        assertThat(errorResult.message()).isEqualTo("Complaint not found");
    }

    @Test
    void shouldUpdateComplaintContentSuccessfully() {
        // given
        String newContent = "Updated complaint content";

        // when
        ComplaintDto updatedComplaint = given()
                .contentType("application/json")
                .body(new UpdateComplaintContentRequest(newContent))
                .when()
                .patch("/complaint/1/content")
                .then()
                .statusCode(200)
                .extract()
                .as(ComplaintDto.class);

        // then
        assertThat(updatedComplaint).isNotNull();
        assertThat(updatedComplaint.id()).isEqualTo(1);
        assertThat(updatedComplaint.content()).isEqualTo(newContent);
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingComplaint() {
        // given
        String newContent = "Some updated content";

        // when
        ErrorResult errorResult = given()
                .contentType("application/json")
                .body(new UpdateComplaintContentRequest(newContent))
                .when()
                .patch("/complaint/999/content")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResult.class);

        // then
        assertThat(errorResult.statusCode()).isEqualTo(404);
        assertThat(errorResult.message()).isEqualTo("Complaint not found");
    }

    @Test
    void shouldAddNewComplaintSuccessfully() {
        // given
        stubIpLocationApi();
        CreateComplaintRequest request =
                new CreateComplaintRequest(2, 1, "New complaint content");

        // when
        ComplaintDto newComplaint = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/complaint")
                .then()
                .statusCode(201)
                .extract()
                .as(ComplaintDto.class);

        // then

        assertThat(newComplaint)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(COMPLAINT_DTO_4);

        assertThat(newComplaint.createdAt())
                .isCloseTo(COMPLAINT_DTO_4.createdAt(), within(1, MINUTES));
    }

    @Test
    void shouldReturn404WhenAddingComplaintForNonExistingProduct() {
        // given
        CreateComplaintRequest request = new CreateComplaintRequest(999, 1, "Content");

        // when
        ErrorResult errorResult = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/complaint")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResult.class);

        // then
        assertThat(errorResult.statusCode()).isEqualTo(404);
        assertThat(errorResult.message()).isEqualTo("Product not found");
    }

    @Test
    void shouldReturn404WhenAddingComplaintForNonExistingComplainer() {
        // given
        CreateComplaintRequest request = new CreateComplaintRequest(1, 999, "Content");

        // when
        ErrorResult errorResult = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/complaint")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResult.class);

        // then
        assertThat(errorResult.statusCode()).isEqualTo(404);
        assertThat(errorResult.message()).isEqualTo("Complainer not found");
    }

    @Test
    void shouldReturn404WhenIpLocationApiFails() {
        // given
        stubIpLocationApiError();
        CreateComplaintRequest request =
                new CreateComplaintRequest(2, 1, "New complaint content");

        // when
        ErrorResult errorResult = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/complaint")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResult.class);

        // then
        assertThat(errorResult.statusCode()).isEqualTo(404);
        assertThat(errorResult.message()).isEqualTo("Country not found");
    }

    private void stubIpLocationApiError() {
        configureFor(wiremock.getHost(), wiremock.getMappedPort(8080));
        stubFor(get(urlPathEqualTo("/"))
                .willReturn(aResponse().withStatus(500)));
    }


    private void stubIpLocationApi() {
        configureFor(wiremock.getHost(), wiremock.getMappedPort(8080));
        stubFor(get(urlPathEqualTo("/"))
                .willReturn(okJson(COUNTRY_JSON)));
    }
}
