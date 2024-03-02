package com.wooseok.bunnypoker.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Card {
    private String suit; // "diamonds","clubs","hearts","spades"
    private String value; // "ace","2","3","4","5","6","7","8","9","10","jack","queen","king"
}
