package com.wooseok.bunnypoker.Flask.FlaskDTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCardReq{
    List<CardReq> players;
}
