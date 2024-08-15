package ru.yandex.practicum.constants;

public class URLS {

    //Base URL
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    //Ingredients
    public static final String INGREDIENTS_GET = "/api/ingredients";

    //Orders
    public static final String ORDER_CREATE_READ = "/api/orders";
    public static final String ORDERS_GET = "/api/orders/all";


    //Passwords
    public static final String PASSWORD_RESET_REQUEST = "/api/password-reset";
    public static final String PASSWORD_RESET_CONFIRM = "/api/password-reset/reset";

    //Authorization
    public static final String USER_REGISTER = "/api/auth/register";
    public static final String USER_LOGIN = "/api/auth/login";
    public static final String USER_LOGOUT = "/api/auth/logout";
    public static final String TOKEN_REFRESH = "/api/auth/token";
    public static final String USER_RUD = "/api/auth/user";
}
