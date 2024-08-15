package ru.yandex.practicum.utils;

import io.qameta.allure.Step;
import ru.yandex.practicum.dto.Ingredient;
import ru.yandex.practicum.dto.Ingredients;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderRequestHelper extends BaseHTTPClient {

    @Step("Get random Ingredient from massive of Ingredients with type \"{type}\"")
    public Ingredient getRandomIngredientWithType(Ingredients ingredients, String type) {
        List<Ingredient> ingredientsList = new ArrayList<>();
        for (Ingredient ingredient : ingredients.getData()) {
            if (ingredient.getType().equals(type)) {
                ingredientsList.add(ingredient);
            }
        }
        if (ingredientsList.size() > 0) {
            return ingredientsList.get(new Random().nextInt(ingredientsList.size()));
        } else {
            System.out.println("No ingredients with selected type are found");
            return null;
        }
    }

    @Step("Creating array of hash sums for selected Ingredients for Order create request")
    public String[] createArrayOfHashSumsForOrderCreateRequest(Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3) {
        String[] selectedIngredientsHashSums = new String[] {ingredient1.getId(), ingredient2.getId(), ingredient3.getId()};
        return selectedIngredientsHashSums;
    }

}
