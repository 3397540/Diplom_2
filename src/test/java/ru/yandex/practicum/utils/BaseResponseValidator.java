package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;
import ru.yandex.practicum.dto.ResponseMessage;

import static org.hamcrest.CoreMatchers.equalTo;

public abstract class BaseResponseValidator {

    @Step("Check that response code is equal to {responseCode}")
    public void checkResponseCode(Response response, int responseCode) {
        response.then()
                .assertThat()
                .statusCode(responseCode);
    }

    //Unified method for all types of values
    @Step("Validate that response body contains parameter \"{parameter}\" with value equals to \"{value}\"")
    public void validateResponseBody(Response response, String parameter, Object value) {
        response.then()
                .assertThat()
                .body(parameter, equalTo(value));
    }

    //Overloaded method for String values
    @Step("Validate that response body contains parameter \"{parameter}\" with value equals to \"{value}\"")
    public void validateResponseBody(Response response, String parameter, String value) {
        response.then()
                .assertThat()
                .body(parameter, equalTo(value));
    }

    //Overloaded method for boolean values
    @Step("Validate that response body contains parameter \"{parameter}\" with value equals to \"{value}\"")
    public void validateResponseBody(Response response, String parameter, Boolean value) {
        response.then()
                .assertThat()
                .body(parameter, equalTo(value));
    }

    //Overloaded method for Integer values
    @Step("Validate that response body contains parameter \"{parameter}\" with value equals to \"{value}\"")
    public void validateResponseBody(Response response, String parameter, Integer value) {
        response.then()
                .assertThat()
                .body(parameter, equalTo(value));
    }

    @Step("Deserialize response as ResponseMessage object")
    public ResponseMessage deserializeResponseAsResponseMessageObject(Response response) {
        return response
                .then()
                .extract()
                .body().as(ResponseMessage.class);
    }

    @Step("Get text value for payload parameter {param}")
    public String getTextValueForPayloadParameter(Response response, String param) {
        return response.then()
                .extract()
                .body()
                .jsonPath().get(param).toString();
    }

    @Step("Check that value is not NULL")
    public void checkValueNotNullForPayloadParameter(Object param) {
        String objName = param.getClass().getName();
        Assert.assertNotNull("Value for " + objName + " is not presented", param);
    }
}
