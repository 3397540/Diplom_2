package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseHTTPClient extends BaseHTTPConfig{

    @Step("Send GET request to \"{endpoint}\" without auth")
    public Response sendGetRequest(String endpoint) {
        return given()
                .config(config)
                .spec(requestSpec())
                .get(endpoint)
                .thenReturn();
    }

    @Step("Send POST request to \"{endpoint}\" without auth")
    public Response sendPostRequest(String endpoint, Object body) {
        return given()
                .config(config)
                .spec(requestSpec())
                .body(body)
                .post(endpoint)
                .thenReturn();
    }

    @Step("Send PATCH request to \"{endpoint}\" without auth")
    public Response sendPatchRequest(String endpoint, Object body) {
        return given()
                .config(config)
                .spec(requestSpec())
                .body(body)
                .patch(endpoint)
                .thenReturn();
    }

    @Step("Send GET request to \"{endpoint}\" with auth token")
    public Response sendGetWAuthRequest(String endpoint, String token) {
        return given()
                .config(config)
                .spec(requestSpec())
                .auth().oauth2(token)
                .get(endpoint)
                .thenReturn();
    }

    @Step("Send POST request to \"{endpoint}\" with auth token")
    public Response sendPostWAuthRequest(String endpoint, Object body, String token) {
        return given()
                .config(config)
                .spec(requestSpec())
                .auth().oauth2(token)
                .body(body)
                .post(endpoint)
                .thenReturn();
    }

    @Step("Send PATCH request to \"{endpoint}\" with auth token")
    public Response sendPatchWAuthRequest(String endpoint, Object body, String token) {
        return given()
                .config(config)
                .spec(requestSpec())
                .auth().oauth2(token)
                .body(body)
                .patch(endpoint)
                .thenReturn();
    }

    @Step("Send DELETE request to \"{endpoint}\" without auth")
    public Response sendDeleteRequest(String endpoint, Object body) {
        return given()
                .config(config)
                .spec(requestSpec())
                .body(body)
                .delete(endpoint)
                .thenReturn();
    }

    @Step("Send DELETE request to \"{endpoint}\" with auth token")
    public Response sendDeleteWAuthRequest(String endpoint, String token) {
        return given()
                .config(config)
                .spec(requestSpec())
                .auth().oauth2(token)
                .delete(endpoint)
                .thenReturn();
    }

    @Step("Send GET request to \"{endpoint}\" with query parameter {param} and value {value}")
    public Response sendGetRequestWithQueryParam(String endpoint, String param, String value) {
        return given()
                .config(config)
                .spec(requestSpec())
                .queryParam(param, value)
                .get(endpoint)
                .thenReturn();
    }

    @Step("Send POST request to \"{endpoint}\" with body parameter {param} and value {value}")
    public Response sendPostRequestWithBodyParam(String endpoint, String param, Object value) {
        Map<String, Object> body = new HashMap<>();
        body.put(param, value);
        return given()
                .config(config)
                .spec(requestSpec())
                .body(body)
                .post(endpoint)
                .thenReturn();
    }

    @Step("Send POST request to \"{endpoint}\" with body parameter {param} and value {value} and with auth token")
    public Response sendPostRequestWithBodyParamWAuth(String endpoint, String param, Object value, String token) {
        Map<String, Object> body = new HashMap<>();
        body.put(param, value);
        return given()
                .config(config)
                .spec(requestSpec())
                .auth().oauth2(token)
                .body(body)
                .post(endpoint)
                .thenReturn();
    }

    @Step("Send POST request to \"{endpoint}\" with body from object {body}")
    public Response sendPostRequestWithBody(String endpoint, Object body) {
        return given()
                .config(config)
                .spec(requestSpec())
                .body(body)
                .post(endpoint)
                .thenReturn();
    }
}
