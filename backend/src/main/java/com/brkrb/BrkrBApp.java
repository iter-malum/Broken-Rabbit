package com.brkrb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.brkrb.mdl")
@EnableJpaRepositories(basePackages = "com.brkrb.rpo")
@EnableTransactionManagement
public class BrkrBApp {
    public static void main(String[] a) {
        SpringApplication.run(BrkrBApp.class, a);
    }
}