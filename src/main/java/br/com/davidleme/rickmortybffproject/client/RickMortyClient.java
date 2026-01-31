package br.com.davidleme.rickmortybffproject.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RickMortyClient {

    private final RestTemplate restTemplate;

    @Value("${rickmorty.api.url}")
    private String baseUrl;

    public RickMortyClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Map buscarPersonagens(int page) {
        return restTemplate.getForObject(
                baseUrl + "/character?page=" + page,
                Map.class
        );
    }

    public Map buscarPersonagemPorNome(String nome) {
        return restTemplate.getForObject(
                baseUrl + "/character?name=" + nome,
                Map.class
        );
    }
}