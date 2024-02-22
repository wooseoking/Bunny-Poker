package com.wooseok.bunnypoker.Service;

import com.wooseok.bunnypoker.domain.entity.*;
import com.wooseok.bunnypoker.domain.enums.GameStatus;
import com.wooseok.bunnypoker.domain.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {
    private Map<String,GameRoom> gameRoomMap = new HashMap<>();

    @Autowired
    private PlayerRepository playerRepository;

    public GameRoom JoinGameRoom(String gameRoomName,String playerId){

        int NUM_OF_PLAYERS_CARD = 2;

        Player player = playerRepository.findByPlayerId(playerId);

        if(player == null) return null;

        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .playerId(player.getPlayerId())
                .money(player.getMoney())
                .nickName(player.getNickName())
                .cards(new Card[NUM_OF_PLAYERS_CARD])
                .fold(false)
                .build();

        GameRoom gameRoom;

        if(gameRoomMap.containsKey(gameRoomName)){
            gameRoom = gameRoomMap.get(gameRoomName);
        }else{
            gameRoom = new GameRoom(gameRoomName);
            gameRoomMap.put(gameRoomName,gameRoom);
        }

        gameRoom.addPlayer(inGamePlayer);

        return gameRoom;
    }


    //게임시작 = 룸 초기화, 덱 섞기 , 카드 나누어 주기
    public void startGame(String gameRoomName){

        GameRoom gameRoom = gameRoomMap.get(gameRoomName);

        gameRoom.Initialize();

        // 덱 섞기
        gameRoom.shuffle();
        // 카드 나누어 주기
        gameRoom.handCardToPlayers();

    }

    public void fold(String gameRoomName,String playerId){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        gameRoom.fold(playerId);

    }

    public GameState returnGameState(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);

        List<InGamePlayer> inGamePlayers = new ArrayList<>();

        for(InGamePlayer player : gameRoom.getInGamePlayers()){
            if(player.isFold())continue;
            inGamePlayers.add(player);
        }

        return GameState.builder()
                .inGamePlayerList(inGamePlayers)
                .waitingPlayerList(gameRoom.getWaitingPlayers())
                .cardsOnBoard(gameRoom.getCardOnBoard())
                .pot(gameRoom.getPot())
                .build();
    }

    public void end(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        gameRoom.setStatus(GameStatus.WAITING_FOR_PLAYERS.getDescription());
    }

    public void preFlop(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        gameRoom.preFlop();
    }

    public void turn(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        gameRoom.turn();
    }

    public void river(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        gameRoom.river();
    }

    public void showDown(String gameRoomName){

    }
    public void betting(String gameRoomName,String playerId,int betMoney){

    }

    public boolean leaveRoom(String gameRoomName, String playerId){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        if(gameRoom == null) return false;

        return gameRoom.leavePlayer(playerId);


    }

}
