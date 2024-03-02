package com.wooseok.bunnypoker.domain.enums;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;

@Getter
public enum GameStatus {
    WAITING_FOR_PLAYERS("Waiting for players"),
    START("Just Started"),
    PRE_FLOP("Pre-flop"), // 게임 시작 전, 플레이어에게 2장의 카드가 분배된 상태
    FLOP("Flop"), // 3장의 커뮤니티 카드가 오픈된 상태
    TURN("Turn"), // 4번째 커뮤니티 카드가 오픈된 상태
    RIVER("River"), // 마지막, 5번째 커뮤니티 카드가 오픈된 상태
    SHOWDOWN("Showdown"), // 플레이어들이 카드를 공개하고 승자를 결정하는 단계
    FINISHED("Finished"); // 게임 종료

    private final String description;

    GameStatus(String description) {
        this.description = description;
    }

}
