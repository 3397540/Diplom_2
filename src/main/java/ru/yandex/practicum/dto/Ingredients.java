package ru.yandex.practicum.dto;

public class Ingredients {

    private Boolean success;
    private Ingredient[] data;

    public Ingredients() {}

    public Ingredients(Boolean success, Ingredient[] data) {
        this.success = success;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Ingredient[] getData() {
        return data;
    }

    public void setData(Ingredient[] data) {
        this.data = data;
    }
}
