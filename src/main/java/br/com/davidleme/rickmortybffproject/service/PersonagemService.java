package br.com.davidleme.rickmortybffproject.service;

import br.com.davidleme.rickmortybffproject.client.RickMortyClient;
import br.com.davidleme.rickmortybffproject.dto.PaginacaoResponseDTO;
import br.com.davidleme.rickmortybffproject.dto.PersonagemResumoDTO;
import br.com.davidleme.rickmortybffproject.exception.PersonagemNaoEncontradoException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Service
public class PersonagemService {

    private final RickMortyClient client;

    public PersonagemService(RickMortyClient client) {
        this.client = client;
    }

    @Cacheable(cacheNames = "personagensPaginados", key = "#page")
    public PaginacaoResponseDTO listar(int page) {
        Map response = client.buscarPersonagens(page);

        List<Map> results = (List<Map>) response.get("results");

        List<PersonagemResumoDTO> personagens = results.stream()
                .map(r -> new PersonagemResumoDTO(
                        (Integer) r.get("id"),
                        (String) r.get("name")
                ))
                .toList();

        return new PaginacaoResponseDTO(page, personagens);
    }

    @Cacheable(cacheNames = "personagemPorNome", key = "#nome.toLowerCase()")
    public PersonagemResumoDTO buscarPorNome(String nome) {
        try {
            Map response = client.buscarPersonagemPorNome(nome);
            List<Map> results = (List<Map>) response.get("results");

            if (results == null || results.isEmpty()) {
                throw new PersonagemNaoEncontradoException();
            }

            Map personagem = results.get(0);

            return new PersonagemResumoDTO(
                    (Integer) personagem.get("id"),
                    (String) personagem.get("name")
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new PersonagemNaoEncontradoException();
        }
    }
}
