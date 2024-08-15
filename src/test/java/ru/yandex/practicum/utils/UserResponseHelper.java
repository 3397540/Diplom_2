package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.dto.ExtendedUser;

public class UserResponseHelper extends BaseResponseValidator{

    @Step("Deserialize response as ExtendedUser object")
    public ExtendedUser deserializeResponseAsExtendedUserObject(Response response) {
        return response
                .then()
                .extract()
                .body().as(ExtendedUser.class);
    }

    //Overloaded method to deserialize response with adding given password since it is not presented in response
    @Step("Deserialize response as ExtendedUser object and add password")
    public ExtendedUser deserializeResponseAsExtendedUserObject(Response response, String password) {
        ExtendedUser extendedUser = response
                .then()
                .extract()
                .body().as(ExtendedUser.class);
        extendedUser.setUserPassword(password);
        return extendedUser;
    }


}
