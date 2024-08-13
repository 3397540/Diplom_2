package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

import ru.yandex.practicum.constants.URLS;
import ru.yandex.practicum.dto.ExtendedUser;
import ru.yandex.practicum.dto.ResponseMessage;
import ru.yandex.practicum.dto.User;
import ru.yandex.practicum.utils.UserRequestHelper;
import ru.yandex.practicum.utils.UserResponseHelper;

public class UserLoginTest {

    private User user;
    private ExtendedUser existingExtendedUser;

    //Set user data for tests
    private final String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@" + RandomStringUtils
            .randomAlphabetic(7).toLowerCase() + ".com";
    private final String password = RandomStringUtils.random(10, true, true);
    private final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase() ;

    @Before
    @DisplayName("Creating user for test")
    @Description("Creating user for test")
    public void setup() {
        user = new User(email, password, name);
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        existingExtendedUser = responseHelper.deserializeResponseAsExtendedUserObject(response, password);
    }

    @Test
    @DisplayName("User could login with existing credentials")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_LOGIN + " with existing email and password " +
            "and check that user is logged in")
    public void userCouldLoginWithExistingCredentialsTest() {
        user = new User();
        user.setEmail(existingExtendedUser.getUser().getEmail());
        user.setPassword(existingExtendedUser.getUser().getPassword());
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_LOGIN, user);
        responseHelper.checkResponseCode(response, SC_OK);
        ExtendedUser actual = responseHelper.deserializeResponseAsExtendedUserObject(response, password);
        Assert.assertEquals(existingExtendedUser.getUser(), actual.getUser());
    }

    @Test
    @DisplayName("User could not login with wrong password")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_LOGIN + " with existing email and wrong password " +
            "and check that user is not logged in")
    public void userCouldNotLoginWithWrongPasswordTest() {
        user = new User();
        user.setEmail(existingExtendedUser.getUser().getEmail());
        user.setPassword(RandomStringUtils.random(10, true, true));
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_LOGIN, user);
        responseHelper.checkResponseCode(response, SC_UNAUTHORIZED);
        ResponseMessage expected = new ResponseMessage(false, "email or password are incorrect");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("User could not login with wrong email")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_LOGIN + " with existing password and wrong email " +
            "and check that user is not logged in")
    public void userCouldNotLoginWithWrongEmailTest() {
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@" + RandomStringUtils
                .randomAlphabetic(7).toLowerCase() + ".com");
        user.setPassword(existingExtendedUser.getUser().getPassword());
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_LOGIN, user);
        responseHelper.checkResponseCode(response, SC_UNAUTHORIZED);
        ResponseMessage expected = new ResponseMessage(false, "email or password are incorrect");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @After
    @DisplayName("Deleting created user")
    @Description("Deleting user created for test")
    public void deleteCreatedUser() {
        try {
            UserRequestHelper requestHelper = new UserRequestHelper();
            System.out.println(existingExtendedUser.getValidAccessToken());
            requestHelper.sendDeleteWAuthRequest(URLS.USER_RUD, existingExtendedUser.getValidAccessToken());
        } catch (Exception e){
            System.out.println("User could not be deleted");
        }
    }

}
