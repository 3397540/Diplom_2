package ru.yandex.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ResponseMessage {

    private Boolean success;
    private String message;
}
