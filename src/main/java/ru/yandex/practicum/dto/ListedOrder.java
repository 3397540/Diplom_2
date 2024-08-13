package ru.yandex.practicum.dto;

import com.google.gson.annotations.SerializedName;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class ListedOrder {

    @SerializedName("_id")
    private String id;
    private String[] ingredients;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private Integer number;
}
