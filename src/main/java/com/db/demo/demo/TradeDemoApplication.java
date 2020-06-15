package com.db.demo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@EnableAspectJAutoProxy
public class TradeDemoApplication {

    public static void main(String[] args) {
        new SpringApplication(TradeDemoApplication.class).run(args);
    }
}
