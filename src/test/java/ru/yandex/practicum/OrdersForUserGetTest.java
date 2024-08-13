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

public class OrdersForUserGetTest {

    private Ingredients ingredients;
    private User user;
    private ExtendedUser existingExtendedUser;
    private OrderConfirmation orderConfirmation;
    private Order order;
    private Orders orders;
    private ListedOrder listedOrder;

    private Ingredient bun;
    private Ingredient main;
    private Ingredient sauce;

    //Set user data for tests
    private final String email = RandomStringUtils.randomAlphabetic(7).toLowerCase() + "@" + RandomStringUtils
            .randomAlphabetic(7).toLowerCase() + ".com";
    private final String password = RandomStringUtils.random(10, true, true);
    private final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase() ;

    @Before
    @DisplayName("Creating user, selecting ingredients and creating order for test")
    @Description("Creating user, selecting ingredients and creating order for test")
    public void setUp() {
        user = new User(email, password, name);
        UserRequestHelper userRequestHelper = new UserRequestHelper();
        UserResponseHelper userResponseHelper = new UserResponseHelper();
        Response userResponse = userRequestHelper.sendPostRequest(URLS.USER_REGISTER, user);
        existingExtendedUser = userResponseHelper.deserializeResponseAsExtendedUserObject(userResponse, password);
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        OrderResponseHelper orderResponseHelper = new OrderResponseHelper();
        IngredientResponseHelper ingredientResponseHelper = new IngredientResponseHelper();
        Response ingredientsResponse = orderRequestHelper.sendGetRequest(URLS.INGREDIENTS_GET);
        ingredients = ingredientResponseHelper.deserializeResponseAsIngredientsObject(ingredientsResponse);
        bun = orderRequestHelper.getRandomIngredientWithType(ingredients, "bun");
        main = orderRequestHelper.getRandomIngredientWithType(ingredients, "main");
        sauce = orderRequestHelper.getRandomIngredientWithType(ingredients, "sauce");
        String[] selectedIngredientsHashSums = orderRequestHelper.createArrayOfHashSumsForOrderCreateRequest(bun, main, sauce);
        Response response = orderRequestHelper.sendPostRequestWithBodyParamWAuth(URLS
                .ORDER_CREATE_READ, "ingredients", selectedIngredientsHashSums, existingExtendedUser.getValidAccessToken());
        orderConfirmation = orderResponseHelper.deserializeResponseAsOrderConfirmationObject(response);

    }

    @Test
    @DisplayName("Unauthorized user could not get orders list")
    @Description("Send GET request to {{baseURI}}" + URLS.ORDER_CREATE_READ + " without Access Token and check that " +
            "orders list is not returned")
    public void unauthorizedUserCouldNotGetOrdersListTest() {
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        OrderResponseHelper orderResponseHelper = new OrderResponseHelper();
        Response response = orderRequestHelper.sendGetRequest(URLS.ORDER_CREATE_READ);
        orderResponseHelper.checkResponseCode(response, SC_UNAUTHORIZED);
        ResponseMessage expected = new ResponseMessage(false, "You should be authorised");
        ResponseMessage actual = orderResponseHelper.deserializeResponseAsResponseMessageObject(response);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Authorized user could get his orders list")
    @Description("Send GET request to {{baseURI}}" + URLS.ORDER_CREATE_READ + " with Access Token and check that " +
            "order list for your User is returned")
    public void authorizedUserCouldGetHisOrdersListTest() {
        OrderRequestHelper orderRequestHelper = new OrderRequestHelper();
        OrderResponseHelper orderResponseHelper = new OrderResponseHelper();
        Response response = orderRequestHelper.sendGetWAuthRequest(URLS.ORDER_CREATE_READ, existingExtendedUser
                .getValidAccessToken());
        orderResponseHelper.checkResponseCode(response, SC_OK);
        String[] ingredientsHashArray = orderRequestHelper.createArrayOfHashSumsForOrderCreateRequest(bun, main, sauce);
        ListedOrder expected = new ListedOrder(orderConfirmation.getOrder().getId(), ingredientsHashArray, orderConfirmation
                .getOrder().getStatus(), orderConfirmation.getOrder().getName(), orderConfirmation.getOrder()
                .getCreatedAt(), orderConfirmation.getOrder().getUpdatedAt(), orderConfirmation.getOrder().getNumber());
        orders = orderResponseHelper.deserializeResponseAsOrdersObject(response);
        ListedOrder actual = orders.getOrders()[0];
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
