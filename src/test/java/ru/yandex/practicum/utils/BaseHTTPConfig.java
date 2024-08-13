package ru.yandex.practicum.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import ru.yandex.practicum.constants.URLS;

public abstract class BaseHTTPConfig {

    RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig
                    .httpClientConfig()
                    .setParam("http.socket.timeout",30000)
                    .setParam("http.connection.timeout", 30000));

    protected static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URLS.BASE_URL)
                .addHeader("Content-Type", "application/json")
                //Off https validation
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }
}
