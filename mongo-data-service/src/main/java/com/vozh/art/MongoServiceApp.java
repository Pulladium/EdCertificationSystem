package com.vozh.art;

import com.vozh.art.service.CertGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MongoServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(MongoServiceApp.class);
    }


    @Autowired
    private CertGenerator certGenerator;
    @Bean
    public CommandLineRunner createCertificate(){
        return args -> {
            System.out.println("Hello world");
            certGenerator.generatePdfFromHtml(certGenerator.parseThymeleafTemplate());
        };
    }
}