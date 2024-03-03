package com.wooseok.bunnypoker.DTO.Response;

import com.wooseok.bunnypoker.domain.entity.Card;
import com.wooseok.bunnypoker.domain.entity.InGamePlayer;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Queue;

@Getter
@Builder
@ToString
public class GameState {
    private String gameState;
    private int currentBettingAmount;
    private List<InGamePlayer> inGamePlayerList;
    private Queue<InGamePlayer> waitingPlayerList;
    private List<Card> cardsOnBoard;
    private String whoseTurn;
}
