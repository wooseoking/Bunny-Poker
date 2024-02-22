package com.wooseok.bunnypoker.domain.entity;

import com.wooseok.bunnypoker.domain.enums.GameStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter @Setter
public class GameRoom {
    private String roomName;
    private final Queue<InGamePlayer> waitingPlayers;
    private final List<InGamePlayer> inGamePlayers;
    private List<Card> cardOnBoard;
    private Deck deck; // Stack<Card> cards
    private int pot;
    private String status;

    public GameRoom(String roomName){
        this.roomName = roomName;
        this.waitingPlayers = new ArrayDeque<>();
        this.inGamePlayers = new ArrayList<>();
        this.cardOnBoard = new ArrayList<>();
        this.deck = new Deck();
        this.pot = 0;
        this.status = GameStatus.WAITING_FOR_PLAYERS.getDescription();
    }

    public void Initialize(){
        // status
        this.status = GameStatus.START.getDescription();

        // card On board
        this.cardOnBoard = new ArrayList<>();
        // pot size
        this.pot = 0;
        // Initialize deck
        deck.initialize();
        // Initialize waiting players
        while(!waitingPlayers.isEmpty()){
            inGamePlayers.add(waitingPlayers.poll());
        }

        // Initialize players
        for(InGamePlayer inGamePlayer : inGamePlayers){
            inGamePlayer.setFold(false);
            inGamePlayer.setCards(new Card[2]);
        }
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
                if(inGamePlayer.isFold() && inGamePlayer.getPlayerId().equals(playerId)){
                    inGamePlayers.remove(inGamePlayer);
                    return true;
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
        return false;
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

    public void fold(String playerId){
        for(InGamePlayer inGamePlayer : inGamePlayers){
            System.out.println(" 리스트에서 찾은 inGamPlayer playerId : " + inGamePlayer.getPlayerId());
            System.out.println(" 호출된 playerId : " + playerId);
            System.out.println("이건 호출되노!!!!!!!!!!!!!!!!");
            if(inGamePlayer.getPlayerId().equals(playerId)){
                System.out.println("이거되냐!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                inGamePlayer.setFold(true);
            }
        }
    }
    public void preFlop(){
        this.status = GameStatus.PRE_FLOP.getDescription();
        // pre-flop 3 card
        int NUM_OF_PRE_FLOP_CARDS = 3;

        // 한장 버리기
        this.deck.drawCard();


        for(int i = 0 ;i <NUM_OF_PRE_FLOP_CARDS;i++){
            cardOnBoard.add(this.deck.drawCard());
        }

    }

    public void turn(){
        this.status = GameStatus.TURN.getDescription();
        // 한장 버리기
        deck.drawCard();

        cardOnBoard.add(this.deck.drawCard());

    }

    public void river(){
        this.status = GameStatus.RIVER.getDescription();
        // 한장 버리기
        deck.drawCard();

        cardOnBoard.add(this.deck.drawCard());
    }


}
