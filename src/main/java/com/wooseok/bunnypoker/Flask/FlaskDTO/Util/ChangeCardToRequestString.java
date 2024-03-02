package com.wooseok.bunnypoker.Flask.FlaskDTO.Util;

import com.wooseok.bunnypoker.domain.entity.Card;

public class ChangeCardToRequestString {
    public static String changeCardToRequestString(Card card){

        StringBuilder sb = new StringBuilder();

        String suit = card.getSuit();
        String value = card.getValue();

        suit = switch (suit) {
            case "spades" -> "S";
            case "diamonds" -> "D";
            case "hearts" -> "H";
            case "clubs" -> "C";
            default -> suit;
        };

        value = switch (value) {
            case "ace" -> "A";
            case "10" -> "T";
            case "jack" -> "J";
            case "queen" -> "Q";
            case "king" -> "K";
            default -> value;
        };

        sb.append(value).append(suit);

        return sb.toString();
    }

}
