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
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.utils.*;

public class OrderCreateTest {

    private Ingredients ingredients;
    private User user;
    private ExtendedUser existingExtendedUser;

    private Ingredient bun;
    private Ingredient main;
    private Ingredient sauce;

    //Set user data for tests
    private final String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@" + RandomStringUtils
            .randomAlphabetic(7).toLowerCase() + ".com";
    private final String password = RandomStringUtils.random(10, true, true);
    private final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase() ;

    @Before
    @DisplayName("Creating user and selecting ingredients for test")
    @Description("Creating user and selecting ingredients for test")
    public void setUp() {
        user = new User(email, password, name);
        UserRequestHelper userRequestHelper = new UserRequestHelper();
        UserResponseHelper userResponseHelper = new UserResponseHelper();
        Response userResponse = userRequestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        existingExtendedUser = userResponseHelper.deserializeResponseAsExtendedUserObject(userResponse, password);
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        IngredientResponseHelper ingredientResponseHelper = new IngredientResponseHelper();
        Response ingredientsResponse = orderRequestHelper.sendGetRequest(URLS.INGREDIENTS_GET);
        ingredients = ingredientResponseHelper.deserializeResponseAsIngredientsObject(ingredientsResponse);
        bun = orderRequestHelper.getRandomIngredientWithType(ingredients, "bun");
        main = orderRequestHelper.getRandomIngredientWithType(ingredients, "main");
        sauce = orderRequestHelper.getRandomIngredientWithType(ingredients, "sauce");
    }

    @Test
    @DisplayName("Authorized user could create an order")
    @Description("Send POST request to {{baseURI}}" + URLS.ORDER_CREATE_READ + " with Access Token and check that " +
            "order is created")
    public void createOrderAsAuthorizedUserTest() {
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        OrderResponseHelper orderResponseHelper = new OrderResponseHelper();
        String[] selectedIngredientsHashSums = orderRequestHelper.createArrayOfHashSumsForOrderCreateRequest(bun, main, sauce);
        Response response = orderRequestHelper.sendPostRequestWithBodyParamWAuth(URLS
                .ORDER_CREATE_READ, "ingredients", selectedIngredientsHashSums, existingExtendedUser.getValidAccessToken());
        orderResponseHelper.checkResponseCode(response,SC_OK);
        orderResponseHelper.validateResponseBody(response, "success", true);
    }

    @Test
    @DisplayName("Unauthorized user could create an order")
    @Description("Send POST request to {{baseURI}}" + URLS.ORDER_CREATE_READ + " without Access Token and check that " +
            "order is created")
    public void createOrderAsUnauthorizedUserTest() {
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        OrderResponseHelper orderResponseHelper = new OrderResponseHelper();
        String[] selectedIngredientsHashSums = orderRequestHelper.createArrayOfHashSumsForOrderCreateRequest(bun, main, sauce);
        Response response = orderRequestHelper.sendPostRequestWithBodyParam(URLS
                .ORDER_CREATE_READ, "ingredients", selectedIngredientsHashSums);

        orderResponseHelper.checkResponseCode(response,SC_OK);
        orderResponseHelper.validateResponseBody(response, "success", true);
    }

    @Test
    @DisplayName("Order could not be created without Ingredients")
    @Description("Send POST request to {{baseURI}}" + URLS.ORDER_CREATE_READ + " with Access Token and without Ingredients " +
            "and check that order is not created")
    public void orderWithoutIngredientsCouldNotBeCreatedTest() {
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        OrderResponseHelper orderResponseHelper = new OrderResponseHelper();
        String[] selectedIngredientsHashSums = new String[] {};
        Response response = orderRequestHelper.sendPostRequestWithBodyParamWAuth(URLS
                .ORDER_CREATE_READ, "ingredients", selectedIngredientsHashSums, existingExtendedUser.getValidAccessToken());
        orderResponseHelper.checkResponseCode(response,SC_BAD_REQUEST);
        ResponseMessage expected = new ResponseMessage(false, "Ingredient ids must be provided");
        ResponseMessage actual = orderResponseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Order could not be created with invalid Ingredients hash sums")
    @Description("Send POST request to {{baseURI}}" + URLS.ORDER_CREATE_READ + " with Access Token and with " +
            "invalid Ingredients hash sums and check that order is not created")
    public void orderWithInvalidIngredientsHashCouldNotBeCreatedTest() {
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        OrderResponseHelper orderResponseHelper = new OrderResponseHelper();
        String[] selectedIngredientsHashSums = new String[] {RandomStringUtils
                .random(25, true, true).toLowerCase(), RandomStringUtils
                .random(25, true, true).toLowerCase(), RandomStringUtils
                .random(25, true, true).toLowerCase()};
        Response response = orderRequestHelper.sendPostRequestWithBodyParamWAuth(URLS
                .ORDER_CREATE_READ, "ingredients", selectedIngredientsHashSums, existingExtendedUser.getValidAccessToken());
        orderResponseHelper.checkResponseCode(response,SC_INTERNAL_SERVER_ERROR);
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
