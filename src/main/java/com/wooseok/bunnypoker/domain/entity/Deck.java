package com.wooseok.bunnypoker.domain.entity;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

@Getter
public class Deck {
    private Stack<Card> cards;
    private String[] suits = {"diamonds","clover","heart","spade"};
    private String[] values = new String[13];
    public Deck(){
        initialize();
    }

    public void initialize(){
        this.cards = new Stack<>();

        for(int v = 1; v < 14;v++){
            values[v - 1] = Integer.toString(v);
        }

        for(String suit : suits){
            for(String value : values){
                cards.push(new Card(suit,value));
            }
        }
    }
    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card drawCard(){
        return cards.pop();
    }

}
