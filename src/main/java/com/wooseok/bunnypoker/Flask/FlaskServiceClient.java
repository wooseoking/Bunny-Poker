package com.wooseok.bunnypoker.Flask;

import com.wooseok.bunnypoker.Flask.FlaskDTO.Request.PlayerCardReq;
import com.wooseok.bunnypoker.Flask.FlaskDTO.Response.PlayerWinnerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "flaskService", url = "http://localhost:8081")
public interface FlaskServiceClient{
    @PostMapping("/evaluate")
    PlayerWinnerResponse evaluate(@RequestBody PlayerCardReq request);
}
