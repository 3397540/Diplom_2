package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.dto.Ingredients;

public class IngredientResponseHelper extends BaseResponseValidator{

    @Step("Deserialize response as Ingredients object")
    public Ingredients deserializeResponseAsIngredientsObject(Response response) {
        return response
                .then()
                .extract()
                .body().as(Ingredients.class);
    }

}
