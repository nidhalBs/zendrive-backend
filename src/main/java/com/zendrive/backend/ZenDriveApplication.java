package com.zendrive.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZenDriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZenDriveApplication.class, args);
    }
}
