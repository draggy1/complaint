package com.complaint.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public abstract class IT {
    @LocalServerPort
    private int port;

    @Container
    @SuppressWarnings("resource")
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("complaintsdb")
            .withUsername("myuser")
            .withPassword("mypassword")
            .withInitScript("init.sql");

    static DockerImageName myImage = DockerImageName.parse("wiremock/wiremock:2.35.0")
            .asCompatibleSubstituteFor("wiremock/wiremock");

    @SuppressWarnings("resource")
    static GenericContainer<?> wiremock = new GenericContainer<>(myImage)
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/__admin/mappings").forStatusCode(200));

    private static String baseUrl;

    public static void setUp() {
        wiremock.start();
        String host = wiremock.getHost();
        Integer mappedPort = wiremock.getMappedPort(8080);
        configureFor(host, mappedPort);
    }

    public static String getBaseUrl() {
        return "http://" + wiremock.getHost() + ":" + wiremock.getMappedPort(8080);
    }


    @BeforeAll
    static void beforeAll() {
        setUp();
        baseUrl = getBaseUrl();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
        registry.add("base.url", () -> baseUrl);
    }

    @BeforeEach
    void beforeEach() throws SQLException, IOException {
        RestAssured.port = port;
        try (Connection connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())) {
            Statement stmt = connection.createStatement();
            stmt.execute("TRUNCATE TABLE complaints, complainers, products RESTART IDENTITY CASCADE");
            String sql = Files.readString(Path.of("src/test/resources/init.sql"));
            stmt.execute(sql);
            stmt.close();
        }
    }
}
