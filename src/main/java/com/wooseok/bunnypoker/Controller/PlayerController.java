package com.wooseok.bunnypoker.Controller;

import com.wooseok.bunnypoker.DTO.Request.LogInReq;
import com.wooseok.bunnypoker.DTO.Request.SignInReq;
import com.wooseok.bunnypoker.DTO.Response.LogInResponse;
import com.wooseok.bunnypoker.Service.GameService;
import com.wooseok.bunnypoker.Service.PlayerService;
import com.wooseok.bunnypoker.domain.entity.Player;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.AttributeNotFoundException;
import javax.management.BadAttributeValueExpException;

@RestController
@RequestMapping("/api")
public class PlayerController {
    private final GameService gameService;
    private final PlayerService playerService;

    public PlayerController(GameService gameService,PlayerService playerService){
        this.gameService = gameService;
        this.playerService = playerService;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LogInReq logInReq ){

        try {
            Player player = playerService.findByPlayerId(logInReq.getPlayerId());

            if(player == null){
                throw new AttributeNotFoundException("User Not Found");
            }
            if(!player.getPassword().equals(logInReq.getPassword())){
                throw new BadAttributeValueExpException("Password not match");
            }
            LogInResponse logInResponse = LogInResponse.builder()
                    .playerId(player.getPlayerId())
                    .nickName(player.getNickName())
                    .money(player.getMoney())
                    .success(true)
                    .build();

            return ResponseEntity.ok(logInResponse);

        } catch (AttributeNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (BadAttributeValueExpException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    // 회원가입
    @PostMapping("/signIn")
    public ResponseEntity<?> logIn(@RequestBody SignInReq signInReq ) {

        try {
            if (playerService.findByPlayerId(signInReq.getPlayerId()) != null)
                throw new EntityExistsException("이미 유저가 존재합니다.");

            Player enrolledPlayer = playerService.enrollPlayer(signInReq.getPlayerId(), signInReq.getPassword(), signInReq.getNickName());

            if (enrolledPlayer == null) {
                throw new AttributeNotFoundException("Cannot enroll User");
            }
            return ResponseEntity.ok(LogInResponse.builder()
                    .playerId(enrolledPlayer.getPlayerId())
                    .money(enrolledPlayer.getMoney())
                    .nickName(enrolledPlayer.getNickName())
                    .success(true)
                    .build());


        } catch (AttributeNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(HttpStatus.IM_USED).body("User Already exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }

    }
}
