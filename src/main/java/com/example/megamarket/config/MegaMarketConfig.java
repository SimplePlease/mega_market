package com.example.megamarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class MegaMarketConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
