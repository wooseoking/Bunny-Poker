package com.wooseok.bunnypoker.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int money;

    String nickName;

    String password;

    String playerId;
}
