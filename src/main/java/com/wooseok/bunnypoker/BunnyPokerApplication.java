package com.wooseok.bunnypoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BunnyPokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BunnyPokerApplication.class, args);
    }

}
