package com.wooseok.bunnypoker.domain.entity;

import com.wooseok.bunnypoker.DTO.Response.GameState;
import com.wooseok.bunnypoker.Flask.FlaskDTO.Request.CardReq;
import com.wooseok.bunnypoker.Flask.FlaskDTO.Request.PlayerCardReq;
import com.wooseok.bunnypoker.Flask.FlaskDTO.Response.PlayerWinnerResponse;
import com.wooseok.bunnypoker.Flask.FlaskDTO.Util.ChangeCardToRequestString;
import com.wooseok.bunnypoker.Flask.FlaskServiceClient;
import com.wooseok.bunnypoker.domain.enums.GameStatus;
import com.wooseok.bunnypoker.domain.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.*;

@Getter @Setter
public class GameRoom {

    private String gameState;
    private String roomName;
    private final Queue<InGamePlayer> waitingPlayers;
    private final List<InGamePlayer> inGamePlayers;
    private List<Card> cardOnBoard;
    private Deck deck; // Stack<Card> cards
    private String status;
    private int currentBettingPlayerIndex;
    private String whoseTurn;
    private int bigBlindTurnIdx;
    private int smallBlindTurnIdx;
    private final int bigBlind = 20;
    private final int smallBlind = 10;
    private int currentBettingAmount;
    private int minimumBettingAmount;
    private final FlaskServiceClient flaskServiceClient;

    public GameRoom(String roomName,FlaskServiceClient flaskServiceClient){
        this.roomName = roomName;
        this.flaskServiceClient = flaskServiceClient;
        this.waitingPlayers = new ArrayDeque<>();
        this.inGamePlayers = new ArrayList<>();
        this.cardOnBoard = new ArrayList<>();
        this.deck = new Deck();
        this.status = GameStatus.WAITING_FOR_PLAYERS.getDescription();
        this.currentBettingPlayerIndex = 0;
    }

    public void Initialize(){
        // status
        this.status = GameStatus.START.getDescription();

        // card On board
        this.cardOnBoard = new ArrayList<>();

        // Initialize deck
        deck.initialize();
        // Initialize waiting players
        while(!waitingPlayers.isEmpty()){
            inGamePlayers.add(waitingPlayers.poll());
        }

        // Initialize players
        for(InGamePlayer inGamePlayer : inGamePlayers){
            inGamePlayer.setCards(new Card[2]);

        }

        // Initialize turn
        this.currentBettingPlayerIndex++;
        currentBettingPlayerIndex %= inGamePlayers.size();

        // Initialize whose turn
        this.whoseTurn = inGamePlayers.get(currentBettingPlayerIndex).getPlayerId();

        // Initialize big blind turn
        this.bigBlindTurnIdx = (this.currentBettingPlayerIndex - 1) % inGamePlayers.size();

        // Initialize small blind turn
        this.smallBlindTurnIdx = (this.currentBettingPlayerIndex - 2) % inGamePlayers.size();

        // Initialize current betting amount
        this.currentBettingAmount = this.bigBlind + this.smallBlind;

        // Initialize minimum betting amount
        this.minimumBettingAmount = this.bigBlind;

        this.gameState = GameStatus.BEFORE_PRE_FLOP.getDescription();
    }

    public GameState getGameState(){

        return GameState.builder()
                .currentBettingAmount(this.currentBettingAmount)
                .inGamePlayerList(this.inGamePlayers)
                .waitingPlayerList(this.waitingPlayers)
                .cardsOnBoard(this.cardOnBoard)
                .whoseTurn(this.whoseTurn)
                .gameState(this.gameState)
                .build();
    }
    public void addPlayer(InGamePlayer player){
        if(player == null) return;

        if(GameStatus.WAITING_FOR_PLAYERS.getDescription().equals(status)){
            synchronized (inGamePlayers){
                inGamePlayers.add(player);
            }
        }else{
            synchronized (waitingPlayers){
                waitingPlayers.add(player);
            }
        }


    }

    public boolean leavePlayer(String playerId){

        synchronized (inGamePlayers){
            for(InGamePlayer inGamePlayer : this.inGamePlayers){
                if(inGamePlayer.getPlayerId().equals(playerId)) {
                    return false;
                }
            }
        }

        synchronized (waitingPlayers){
            for(InGamePlayer waitingPlayer : this.waitingPlayers){
                if(waitingPlayer.getPlayerId().equals(playerId)){
                   waitingPlayers.remove(waitingPlayer);
                   return true;
                }
            }
        }
        return true;
    }

    public void shuffle(){
        this.deck.shuffle();
    }

    public void handCardToPlayers(){

        for(int i = 0 ; i < 2 ;i ++){
            for(InGamePlayer inGamePlayer : inGamePlayers){
                Card handedCard = deck.drawCard();
                inGamePlayer.getCards()[i] = handedCard;
            }
        }
    }

    public void action(String playerId,String action,int bettingAmount){
        if(!inGamePlayers.get(currentBettingPlayerIndex).getPlayerId().equals(playerId)){
            return;
        }

        switch (action) {
            case "fold" -> fold(playerId);
            case "check","call", "raise" -> betting(playerId, bettingAmount);
        }

        this.currentBettingPlayerIndex++;
        this.currentBettingPlayerIndex %= inGamePlayers.size();
    }

    public void betting(String playerId,int bettingAmount){

        for(InGamePlayer inGamePlayer : inGamePlayers){
            if(inGamePlayer.getPlayerId().equals(playerId)){
                inGamePlayer.setMoney(inGamePlayer.getMoney() - bettingAmount);
                this.currentBettingAmount += bettingAmount;
                this.currentBettingPlayerIndex++;
                this.currentBettingPlayerIndex %= inGamePlayers.size();
                this.whoseTurn = inGamePlayers.get(currentBettingPlayerIndex).getPlayerId();
            }
        }

    }

    public void fold(String playerId){
        for(InGamePlayer inGamePlayer : inGamePlayers){
            if(inGamePlayer.getPlayerId().equals(playerId)){
                this.inGamePlayers.remove(inGamePlayer);
                this.currentBettingPlayerIndex %= inGamePlayers.size();
                this.whoseTurn = inGamePlayers.get(currentBettingPlayerIndex).getPlayerId();
                return;
            }
        }
    }
    public void preFlop(){
        this.gameState = GameStatus.PRE_FLOP.getDescription();
        // pre-flop 3 card
        int NUM_OF_PRE_FLOP_CARDS = 3;

        // 한장 버리기
        this.deck.drawCard();


        for(int i = 0 ;i <NUM_OF_PRE_FLOP_CARDS;i++){
            cardOnBoard.add(this.deck.drawCard());
        }

    }

    public void turn(){
        this.gameState = GameStatus.TURN.getDescription();
        // 한장 버리기
        deck.drawCard();

        cardOnBoard.add(this.deck.drawCard());

    }

    public void river(){
        this.gameState = GameStatus.RIVER.getDescription();
        // 한장 버리기
        deck.drawCard();

        cardOnBoard.add(this.deck.drawCard());
    }

    public boolean isEndOfGame(){
        return this.inGamePlayers.size() == 1;
    }

    public PlayerWinnerResponse getWinnerPlayerId(){


        List<CardReq> playerCardReqList = new ArrayList<>();

        List<String> cardsOnBoardStr = new ArrayList<>();

        for(Card card : cardOnBoard){
            cardsOnBoardStr.add(ChangeCardToRequestString.changeCardToRequestString(card));
        }

        for(InGamePlayer inGamePlayer : inGamePlayers){

            List<String> playerCards = new ArrayList<>();

            for(Card card : inGamePlayer.getCards()){
                playerCards.add(ChangeCardToRequestString.changeCardToRequestString(card));
            }

            playerCards.addAll(cardsOnBoardStr);

            // 닉네임으로 바꿔줌
            String playerId = inGamePlayer.getNickName();

            playerCardReqList.add(new CardReq(playerId,playerCards));
        }

        PlayerCardReq playerCardReq = new PlayerCardReq(playerCardReqList);

        return flaskServiceClient.evaluate(playerCardReq);
    }
}
