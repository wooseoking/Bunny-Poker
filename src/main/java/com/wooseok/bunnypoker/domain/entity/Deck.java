package com.wooseok.bunnypoker.domain.entity;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

@Getter
public class Deck {
    private Stack<Card> cards;
    private String[] suits = {"diamonds","clubs","hearts","spades"};
    private String[] values = {"ace","2","3","4","5","6","7","8","9","10","jack","queen","king"};
    public Deck(){
        initialize();
    }

    public void initialize(){
        this.cards = new Stack<>();

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
