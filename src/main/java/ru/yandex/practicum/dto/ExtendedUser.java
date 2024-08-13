package ru.yandex.practicum.dto;

import java.util.Objects;

public class ExtendedUser {

    private Boolean success;
    private String accessToken;
    private String refreshToken;
    private User user;

    public ExtendedUser() {}

    public ExtendedUser(Boolean success, String accessToken, String refreshToken, User user) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
       this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserPassword(String password) {
        this.user.setPassword(password);
    }

    public String getValidAccessToken() {
        String[] validToken = accessToken.split(" ");
        return validToken[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtendedUser that = (ExtendedUser) o;
        return Objects.equals(success, that.success) && Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, accessToken, refreshToken, user);
    }
}
