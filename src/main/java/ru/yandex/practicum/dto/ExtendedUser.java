package ru.yandex.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class ExtendedUser {

    private Boolean success;
    private String accessToken;
    private String refreshToken;
    private User user;

    public void setUserPassword(String password) {
        this.user.setPassword(password);
    }

    public String getValidAccessToken() {
        String[] validToken = accessToken.split(" ");
        return validToken[1];
    }
}
