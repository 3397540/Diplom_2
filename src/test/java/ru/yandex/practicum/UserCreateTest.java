package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import ru.yandex.practicum.constants.URLS;
import ru.yandex.practicum.dto.ExtendedUser;
import ru.yandex.practicum.dto.ResponseMessage;
import ru.yandex.practicum.dto.User;
import ru.yandex.practicum.utils.UserRequestHelper;
import ru.yandex.practicum.utils.UserResponseHelper;

public class UserCreateTest {

    private User user;
    private ExtendedUser actualExtendedUser;

    //Set user data for tests
    private final String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@" + RandomStringUtils
            .randomAlphabetic(7).toLowerCase() + ".com";
    private final String password = RandomStringUtils.random(10, true, true);
    private final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase() ;

    @Test
    @DisplayName("Unique user could be created")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_REGISTER + " with unique email, password and name " +
            "and check that user is created")
    public void uniqueUserCouldBeCreatedTest() {
        user = new User(email, password, name);
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        responseHelper.checkResponseCode(response, SC_OK);
        actualExtendedUser = responseHelper.deserializeResponseAsExtendedUserObject(response, password);
        Assert.assertEquals(user, actualExtendedUser.getUser());
    }

    @Test
    @DisplayName("Non-unique user could not be created")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_REGISTER + " with non-unique email, password and name " +
            "and check that user is not created")
    public void nonUniqueUserCouldNotBeCreatedTest() {
        user = new User(email, password, name);
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        actualExtendedUser = responseHelper.deserializeResponseAsExtendedUserObject(response);
        User duplicatedUser = new User(email, password, name);
        Response response2 = requestHelper.sendPostRequest(URLS.USER_REGISTER, duplicatedUser);
        responseHelper.checkResponseCode(response2, SC_FORBIDDEN);
        ResponseMessage expected = new ResponseMessage(false, "User already exists");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("User could not be created without email")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_REGISTER + " without email and check that user is not created")
    public void userCouldNotBeCreatedWithoutEmailTest() {
        user = new User();
        user.setPassword(password);
        user.setName(name);
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        responseHelper.checkResponseCode(response, SC_FORBIDDEN);
        ResponseMessage expected = new ResponseMessage(false, "Email, password and name are required fields");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("User could not be created without password")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_REGISTER + " without password and check that user is not created")
    public void userCouldNotBeCreatedWithoutPasswordTest() {
        user = new User();
        user.setEmail(email);
        user.setName(name);
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        responseHelper.checkResponseCode(response, SC_FORBIDDEN);
        ResponseMessage expected = new ResponseMessage(false, "Email, password and name are required fields");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("User could not be created without name")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_REGISTER + " without name and check that user is not created")
    public void userCouldNotBeCreatedWithoutNameTest() {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        responseHelper.checkResponseCode(response, SC_FORBIDDEN);
        ResponseMessage expected = new ResponseMessage(false, "Email, password and name are required fields");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @After
    @DisplayName("Deleting created user")
    @Description("Deleting user created for test")
    public void deleteCreatedUser() {
        try {
            UserRequestHelper requestHelper = new UserRequestHelper();
            System.out.println(actualExtendedUser.getValidAccessToken());
            requestHelper.sendDeleteWAuthRequest(URLS.USER_RUD, actualExtendedUser.getValidAccessToken());
        } catch (Exception e){
            System.out.println("User could not be deleted");
        }
    }
}
