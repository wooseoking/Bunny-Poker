package com.wooseok.bunnypoker.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Queue;

@Getter
@Builder
public class GameState {
    private int pot;
    private List<InGamePlayer> inGamePlayerList;
    private Queue<InGamePlayer> waitingPlayerList;
    private List<Card> cardsOnBoard;
}
