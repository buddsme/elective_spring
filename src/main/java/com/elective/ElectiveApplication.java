package com.elective;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.reactive.ApplicationContextServerWebExchangeMatcher;

@SpringBootApplication
public class ElectiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectiveApplication.class, args);
    }


}

