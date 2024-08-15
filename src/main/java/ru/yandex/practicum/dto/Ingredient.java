package ru.yandex.practicum.dto;

import com.google.gson.annotations.SerializedName;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Ingredient {

    @SerializedName("_id")
    private String id;
    private String name;
    private String type;
    private Integer proteins;
    private Integer fat;
    private Integer carbohydrates;
    private Integer calories;
    private Integer price;
    private String image;
    private String image_mobile;
    private String image_large;
    @SerializedName("__v")
    private Integer v;
}
