package com.wooseok.bunnypoker.domain.repository;

import com.wooseok.bunnypoker.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
    Player findByPlayerId(String playerId);


}
