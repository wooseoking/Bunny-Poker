package com.wooseok.bunnypoker.DTO.Request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BettingReq {
    private String playerId;
    private String gameRoomName;
    private int bettingAmount;
}
