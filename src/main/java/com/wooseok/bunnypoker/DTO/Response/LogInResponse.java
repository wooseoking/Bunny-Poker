package com.wooseok.bunnypoker.DTO.Response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LogInResponse {
    String playerId;
    String nickName;
    int money;
    boolean success;
}
