package com.wooseok.bunnypoker.Service;

import com.wooseok.bunnypoker.DTO.Response.GameState;
import com.wooseok.bunnypoker.Flask.FlaskDTO.Response.PlayerWinnerResponse;
import com.wooseok.bunnypoker.Flask.FlaskServiceClient;
import com.wooseok.bunnypoker.domain.entity.*;
import com.wooseok.bunnypoker.domain.enums.GameStatus;
import com.wooseok.bunnypoker.domain.repository.PlayerRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Transactional(readOnly = true)
public class GameService {
    private final ConcurrentMap<String, GameRoom> gameRoomMap = new ConcurrentHashMap<>();
    private final PlayerRepository playerRepository;
    private final FlaskServiceClient flaskServiceClient;

    public GameService(PlayerRepository playerRepository,FlaskServiceClient flaskServiceClient) {
        this.playerRepository = playerRepository;
        this.flaskServiceClient = flaskServiceClient;
    }

    public GameRoom JoinGameRoom(String gameRoomName,String playerId){

        int NUM_OF_PLAYERS_CARD = 2;

        Player player = playerRepository.findByPlayerId(playerId);

        if(player == null) return null;

        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .playerId(player.getPlayerId())
                .money(player.getMoney())
                .nickName(player.getNickName())
                .cards(new Card[NUM_OF_PLAYERS_CARD])
                .build();

        GameRoom gameRoom;

        if(gameRoomMap.containsKey(gameRoomName)){
            gameRoom = gameRoomMap.get(gameRoomName);
        }else{
            gameRoom = new GameRoom(gameRoomName,flaskServiceClient);
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

        return gameRoom.getGameState();
    }

    public void action(String gameRoomName,String playerId,String action,int bettingAmount){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        gameRoom.action(playerId,action,bettingAmount);
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

    @Transactional(rollbackFor = Exception.class)
    public boolean betting(String gameRoomName,String playerId,int bettingAmount){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        if(gameRoom == null) return false;

        Player player = playerRepository.findByPlayerId(playerId);

        player.setMoney(player.getMoney() - bettingAmount);

        //transaction
        playerRepository.save(player);

        gameRoom.betting(playerId,bettingAmount);

        return true;
    }

    public boolean leaveRoom(String gameRoomName, String playerId){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        if(gameRoom == null) return false;

        return gameRoom.leavePlayer(playerId);

    }

    public int getCurrentBettingSize(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        return gameRoom.getCurrentBettingAmount();
    }

    public int getMinimumBettingSize(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        return gameRoom.getMinimumBettingAmount();
    }

    public boolean call(String gameRoomName,String playerId){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        if(gameRoom == null) return false;

        int minimumBettingAmount = gameRoom.getMinimumBettingAmount();
        int currentBettingAmount = gameRoom.getCurrentBettingAmount();

        int callAmount = minimumBettingAmount - currentBettingAmount;

        return betting(gameRoomName,playerId,callAmount);

    }


    public PlayerWinnerResponse getWinnerPlayerId(String gameRoomName){
        GameRoom gameRoom = gameRoomMap.get(gameRoomName);
        gameRoom.getWinnerPlayerId();
        return gameRoom.getWinnerPlayerId();
    }
}
