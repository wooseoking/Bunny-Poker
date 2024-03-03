package com.wooseok.bunnypoker.Controller;

import com.wooseok.bunnypoker.DTO.Request.BettingReq;
import com.wooseok.bunnypoker.Exception.ResourceNotFoundException;
import com.wooseok.bunnypoker.Flask.FlaskDTO.Response.PlayerWinnerResponse;
import com.wooseok.bunnypoker.Service.GameService;
import com.wooseok.bunnypoker.domain.entity.GameRoom;
import com.wooseok.bunnypoker.DTO.Response.GameState;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;


@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 방 참가하기
    @PostMapping("/join/{gameRoomName}/{playerId}")
    public ResponseEntity<?> joinGameRoom(@PathVariable String gameRoomName,@PathVariable String playerId){
        try{
            GameRoom gameRoom = gameService.JoinGameRoom(gameRoomName, playerId);

            if (gameRoom == null) {
                // 적절한 상태 코드와 함께 오류 메시지를 반환
                throw new ResourceNotFoundException("Game room not found: " + gameRoomName);
            }
            System.out.println("Game room joined: " + gameRoomName + " by " + playerId + "!");
            GameState gameState = gameService.returnGameState(gameRoomName);

            return ResponseEntity.ok(gameState);

        } catch (Exception e) {
            logger.error("Exception occurred while trying to join game room: " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 방 떠나기
    @PostMapping("/leave/{gameRoomName}/{playerId}")
    public ResponseEntity<?> leaveGameRoom(@PathVariable String gameRoomName,@PathVariable String playerId){

        try{
            boolean leaved = gameService.leaveRoom(gameRoomName, playerId);
            if(leaved){
                GameState gameState = gameService.returnGameState(gameRoomName);
                System.out.println("Game room leaved: " + gameRoomName + " by " + playerId + "!");
                return ResponseEntity.ok(gameState);

            }else{
                return ResponseEntity.status(HttpStatus.OK).body("Can't leave you must Fold");
            }

        } catch (Exception e) {
            logger.error("Exception occurred while leaving the game room: " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게임 시작하기
    @PostMapping("/start/{gameRoomName}")
    public ResponseEntity<?>  startGame(@PathVariable String gameRoomName) {
        try {
            gameService.startGame(gameRoomName);
            GameState gameState = gameService.returnGameState(gameRoomName);
            System.out.println("Game started: " + gameRoomName + "!");
            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while trying to start game : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 폴드
    @PostMapping("/fold/{gameRoomName}/{playerId}")
    public ResponseEntity<?>  fold(@PathVariable String gameRoomName,@PathVariable String playerId) {
        try {
            gameService.fold(gameRoomName,playerId);
            GameState gameState = gameService.returnGameState(gameRoomName);
            System.out.println("Player " + playerId + " folded in game room: " + gameRoomName + "!");
            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while folding : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 베팅
    @PostMapping("/betting")
    public ResponseEntity<?> betting(@RequestBody BettingReq bettingReq){
        try {
            boolean betResult = gameService.betting(bettingReq.getGameRoomName(),bettingReq.getPlayerId(),bettingReq.getBettingAmount());

            if(!betResult){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Betting failed");
            }

            GameState gameState = gameService.returnGameState(bettingReq.getGameRoomName());
            System.out.println("Player " + bettingReq.getPlayerId() + " bet " + bettingReq.getBettingAmount() + " in game room: " + bettingReq.getGameRoomName() + "!");
            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while betting : " + bettingReq.getGameRoomName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //플랍
    @PostMapping("/preFlop/{gameRoomName}")
    public ResponseEntity<?>  preFlop(@PathVariable String gameRoomName) {
        try {

            gameService.preFlop(gameRoomName);
            GameState gameState = gameService.returnGameState(gameRoomName);
            System.out.println("Pre flop in game room: " + gameRoomName + "!");
            System.out.println(gameState);
            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while trying to pre flop : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //턴
    @PostMapping("/turn/{gameRoomName}")
    public ResponseEntity<?>  turn(@PathVariable String gameRoomName) {
        try {

            gameService.turn(gameRoomName);
            GameState gameState = gameService.returnGameState(gameRoomName);
            System.out.println("Turn in game room: " + gameRoomName + "!");
            return ResponseEntity.ok(gameState);

        }catch (Exception e){
            logger.error("Exception occurred while trying to turn : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //리버
    @PostMapping("/river/{gameRoomName}")
    public ResponseEntity<?>  river(@PathVariable String gameRoomName) {
        try {

            gameService.river(gameRoomName);
            GameState gameState = gameService.returnGameState(gameRoomName);
            System.out.println( "River in game room: " + gameRoomName + "!");
            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while trying to river : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 현재 게임 상태 정보 요청
    @GetMapping("/status/{gameRoomName}")
    public ResponseEntity<?>  gameState(@PathVariable String gameRoomName) {
        try {
            GameState gameState = gameService.returnGameState(gameRoomName);
            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while trying to river : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // 게임 끝
    @GetMapping("/winner/{gameRoomName}")
    public ResponseEntity<?>  getWinnerFromRoomName(@PathVariable String gameRoomName) {
        try {
            PlayerWinnerResponse winnerPlayerId = gameService.getWinnerPlayerId(gameRoomName);
            System.out.println("Winner in game room: " + gameRoomName + " is " + winnerPlayerId.getPlayerId() + "!");
            return ResponseEntity.ok(winnerPlayerId);
        }catch (Exception e){
            logger.error("Exception occurred while trying to caculate winner : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
