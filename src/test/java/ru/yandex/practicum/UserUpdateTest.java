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

public class UserUpdateTest {

    private User user;
    private User existingUser;
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
        existingUser = new User(email, password, name);
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPostRequest(URLS.USER_REGISTER, existingUser);
        existingExtendedUser = responseHelper.deserializeResponseAsExtendedUserObject(response, password);

    }

    @Test
    @DisplayName("Authorized user could update own name")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_RUD + " with new value in \"name\" field and with " +
            "actual Access Token and check that user name is updated")
    public void authorizedUserCouldUpdateOwnNameTest() {
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10).toLowerCase());
        user.setEmail(existingExtendedUser.getUser().getEmail());
        user.setPassword(existingExtendedUser.getUser().getPassword());
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPatchWAuthRequest(URLS.USER_RUD, user, existingExtendedUser.getValidAccessToken());
        responseHelper.checkResponseCode(response, SC_OK);
        ExtendedUser actual = responseHelper.deserializeResponseAsExtendedUserObject(response, password);
        Assert.assertEquals(user, actual.getUser());
    }

    @Test
    @DisplayName("Authorized user could update own email")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_RUD + " with new value in \"email\" field and with " +
            "actual Access Token and check that user email is updated")
    public void authorizedUserCouldUpdateOwnEmailTest() {
        user = new User();
        user.setName(existingExtendedUser.getUser().getName());
        user.setEmail(RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@" + RandomStringUtils
                .randomAlphabetic(7).toLowerCase() + ".com");
        user.setPassword(existingExtendedUser.getUser().getPassword());
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPatchWAuthRequest(URLS.USER_RUD, user, existingExtendedUser.getValidAccessToken());
        responseHelper.checkResponseCode(response, SC_OK);
        ExtendedUser actual = responseHelper.deserializeResponseAsExtendedUserObject(response, password);
        Assert.assertEquals(user, actual.getUser());
    }

    @Test
    @DisplayName("Authorized user could update own password")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_RUD + " with new value in \"password\" field and with " +
            "actual Access Token and check that user password is updated")
    public void authorizedUserCouldUpdateOwnPasswordTest() {
        user = new User();
        user.setName(existingExtendedUser.getUser().getName());
        user.setEmail(existingExtendedUser.getUser().getEmail());
        user.setPassword(RandomStringUtils.random(10, true, true));
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPatchWAuthRequest(URLS.USER_RUD, user, existingExtendedUser.getValidAccessToken());
        responseHelper.checkResponseCode(response, SC_OK);
        ExtendedUser actual = responseHelper.deserializeResponseAsExtendedUserObject(response, user.getPassword());
        Assert.assertEquals(user, actual.getUser());
    }

    @Test
    @DisplayName("Unauthorized user could not update own name")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_RUD + " with new value in \"name\" field and without " +
            "Access Token and check that user name is not updated")
    public void unauthorizedUserCouldNotUpdateOwnNameTest() {
        user = new User();
        user.setName(RandomStringUtils.randomAlphabetic(10).toLowerCase());
        user.setEmail(existingExtendedUser.getUser().getEmail());
        user.setPassword(existingExtendedUser.getUser().getPassword());
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPatchRequest(URLS.USER_RUD, user);
        responseHelper.checkResponseCode(response, SC_UNAUTHORIZED);
        ResponseMessage expected = new ResponseMessage(false, "You should be authorised");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Unauthorized user could not update own email")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_RUD + " with new value in \"email\" field and without " +
            "Access Token and check that user email is not updated")
    public void unauthorizedUserCouldNotUpdateOwnEmailTest() {
        user = new User();
        user.setName(existingExtendedUser.getUser().getName());
        user.setEmail(RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@" + RandomStringUtils
                .randomAlphabetic(7).toLowerCase() + ".com");
        user.setPassword(existingExtendedUser.getUser().getPassword());
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPatchRequest(URLS.USER_RUD, user);
        responseHelper.checkResponseCode(response, SC_UNAUTHORIZED);
        ResponseMessage expected = new ResponseMessage(false, "You should be authorised");
        ResponseMessage actual = responseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Unauthorized user could not update own password")
    @Description("Send POST request to {{baseURI}}" + URLS.USER_RUD + " with new value in \"password\" field and without " +
            "Access Token and check that user password is not updated")
    public void unauthorizedUserCouldNotUpdateOwnDataTest() {
        user = new User();
        user.setName(existingExtendedUser.getUser().getName());
        user.setEmail(existingExtendedUser.getUser().getEmail());
        user.setPassword(RandomStringUtils.random(10, true, true));
        UserRequestHelper requestHelper = new UserRequestHelper();
        UserResponseHelper responseHelper = new UserResponseHelper();
        Response response = requestHelper.sendPatchRequest(URLS.USER_RUD, user);
        responseHelper.checkResponseCode(response, SC_UNAUTHORIZED);
        ResponseMessage expected = new ResponseMessage(false, "You should be authorised");
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
