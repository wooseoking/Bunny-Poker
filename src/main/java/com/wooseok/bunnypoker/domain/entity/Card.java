package com.wooseok.bunnypoker.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Card {
    private String suit; //Spade , Diamond , Heart , Clover
    private String value; // 1,2,3,4,5,6,7,8,9,10,11,12,13
}
