package br.com.davidleme.rickmortybffproject.service;

import br.com.davidleme.rickmortybffproject.client.RickMortyClient;
import br.com.davidleme.rickmortybffproject.dto.PaginacaoResponseDTO;
import br.com.davidleme.rickmortybffproject.dto.PersonagemResumoDTO;
import br.com.davidleme.rickmortybffproject.exception.PersonagemNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonagemServiceTest {

    @Mock
    private RickMortyClient client;

    @InjectMocks
    private PersonagemService service;

    @Test
    void listar_deveRetornarSomenteIdENome_eManterPage() {
        Map<String, Object> responseMock = Map.of(
                "results", List.of(
                        Map.of("id", 1, "name", "Rick Sanchez", "status", "Alive"),
                        Map.of("id", 2, "name", "Morty Smith", "status", "Alive")
                )
        );

        when(client.buscarPersonagens(1)).thenReturn(responseMock);

        PaginacaoResponseDTO response = service.listar(1);

        assertEquals(1, response.page());
        assertEquals(2, response.results().size());

        PersonagemResumoDTO p1 = response.results().get(0);
        assertEquals(1, p1.id());
        assertEquals("Rick Sanchez", p1.nome());

        verify(client, times(1)).buscarPersonagens(1);
    }

    @Test
    void buscarPorNome_quandoExistir_deveRetornarSomenteUmPersonagem() {
        Map<String, Object> responseMock = Map.of(
                "results", List.of(
                        Map.of("id", 1, "name", "Rick Sanchez"),
                        Map.of("id", 8, "name", "Adjudicator Rick")
                )
        );

        when(client.buscarPersonagemPorNome("rick")).thenReturn(responseMock);

        PersonagemResumoDTO response = service.buscarPorNome("rick");

        assertEquals(1, response.id());
        assertEquals("Rick Sanchez", response.nome());
        verify(client, times(1)).buscarPersonagemPorNome("rick");
    }

    @Test
    void buscarPorNome_quandoResultsVazio_deveLancarNaoEncontrado() {
        Map<String, Object> responseMock = Map.of(
                "results", List.of()
        );

        when(client.buscarPersonagemPorNome("naoexiste")).thenReturn(responseMock);

        assertThrows(PersonagemNaoEncontradoException.class,
                () -> service.buscarPorNome("naoexiste"));

        verify(client, times(1)).buscarPersonagemPorNome("naoexiste");
    }

    @Test
    void buscarPorNome_quandoResultsNulo_deveLancarNaoEncontrado() {
        Map<String, Object> responseMock = Map.of(); // sem "results"

        when(client.buscarPersonagemPorNome("x")).thenReturn(responseMock);

        assertThrows(PersonagemNaoEncontradoException.class,
                () -> service.buscarPorNome("x"));
    }
}
