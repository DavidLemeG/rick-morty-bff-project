package br.com.davidleme.rickmortybffproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RickMortyBffProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RickMortyBffProjectApplication.class, args);
    }

}
