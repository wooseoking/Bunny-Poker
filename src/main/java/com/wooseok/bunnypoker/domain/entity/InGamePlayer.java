package com.wooseok.bunnypoker.domain.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class InGamePlayer {
    String playerId;
    String nickName;
    Card[] cards;
    boolean fold;
    int money;

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;

        InGamePlayer other = (InGamePlayer) o;
        return playerId.equals(other.playerId);
    }


}
