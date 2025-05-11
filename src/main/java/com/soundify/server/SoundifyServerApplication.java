package com.soundify.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SoundifyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoundifyServerApplication.class, args);
    }

}
