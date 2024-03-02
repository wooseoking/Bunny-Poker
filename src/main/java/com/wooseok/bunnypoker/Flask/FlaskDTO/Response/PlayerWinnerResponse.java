package com.wooseok.bunnypoker.Flask.FlaskDTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerWinnerResponse {
    private String playerId;
}
