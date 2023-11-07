package com.example.springdoc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringDocApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDocApplication.class, args);
    }

    @Value("${server.port}")
    private String port;


    @Bean
    CommandLineRunner getRunner() {
        return (args)->{
            log.info("http://localhost:"+port);
        };
    }
}
