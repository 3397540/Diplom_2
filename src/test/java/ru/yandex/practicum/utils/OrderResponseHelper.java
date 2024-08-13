package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.dto.OrderConfirmation;
import ru.yandex.practicum.dto.Orders;

public class OrderResponseHelper extends BaseResponseValidator{

    @Step("Deserialize response as OrderConfirmation object")
    public OrderConfirmation deserializeResponseAsOrderConfirmationObject(Response response) {
        return response
                .then()
                .extract()
                .body().as(OrderConfirmation.class);
    }

    @Step("Deserialize response as Orders object")
    public Orders deserializeResponseAsOrdersObject(Response response) {
        return response
                .then()
                .extract()
                .body().as(Orders.class);
    }

}
