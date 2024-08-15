package ru.yandex.practicum.dto;

import com.google.gson.annotations.SerializedName;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Order {

    @SerializedName("_id")
    private String id;
    private Ingredient[] ingredients;
    private User owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private Integer number;
    private Integer price;
}
