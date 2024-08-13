package ru.yandex.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderConfirmation {

    private Boolean success;
    private String name;
    private Order order;
}
