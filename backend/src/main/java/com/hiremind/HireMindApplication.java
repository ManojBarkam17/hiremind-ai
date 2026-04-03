package com.hiremind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = "com.hiremind")
public class HireMindApplication {

    public static void main(String[] args) {
        SpringApplication.run(HireMindApplication.class, args);
    }

}
