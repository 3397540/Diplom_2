package ru.yandex.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Orders {

    private Boolean success;
    private ListedOrder[] orders;
    private Integer total;
    private Integer totalToday;
}
