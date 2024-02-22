package com.wooseok.bunnypoker.Controller;

import com.wooseok.bunnypoker.Service.GameService;
import com.wooseok.bunnypoker.domain.entity.GameRoom;
import com.wooseok.bunnypoker.domain.entity.GameState;
import com.wooseok.bunnypoker.domain.enums.GameStatus;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            GameState gameState = gameService.returnGameState(gameRoomName);

            if (gameRoom == null) {
                // 적절한 상태 코드와 함께 오류 메시지를 반환
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
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

            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while folding : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //플랍
    @PostMapping("/preFlop/{gameRoomName}")
    public ResponseEntity<?>  preFlop(@PathVariable String gameRoomName) {
        try {

            gameService.preFlop(gameRoomName);
            GameState gameState = gameService.returnGameState(gameRoomName);

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

            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while trying to river : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게임 끝
    @PostMapping("/end/{gameRoomName}")
    public ResponseEntity<?>  end(@PathVariable String gameRoomName) {
        try {
            gameService.river(gameRoomName);
            GameState gameState = gameService.returnGameState(gameRoomName);

            return ResponseEntity.ok(gameState);
        }catch (Exception e){
            logger.error("Exception occurred while trying to river : " + gameRoomName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
