package com.wooseok.bunnypoker.DTO.Request;

import lombok.Getter;

@Getter
public class SignInReq {
    String playerId;
    String nickName;
    String password;
}
