package com.wooseok.bunnypoker.Flask.FlaskDTO.Request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CardReq {
    private String id;
    private List<String> cards;
}
