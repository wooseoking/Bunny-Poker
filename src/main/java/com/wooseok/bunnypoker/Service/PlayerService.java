package com.wooseok.bunnypoker.Service;

import com.wooseok.bunnypoker.domain.entity.Player;
import com.wooseok.bunnypoker.domain.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final int INIT_MONEY = 1000;

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player findByPlayerId(String playerId){
        return playerRepository.findByPlayerId(playerId);
    }

    public Player enrollPlayer(String playerId,String password,String nickName){
        Player enrollPlayer = Player.builder()
                .playerId(playerId)
                .money(INIT_MONEY)
                .nickName(nickName)
                .password(password)
                .build();
        return playerRepository.save(enrollPlayer);
    }
}
