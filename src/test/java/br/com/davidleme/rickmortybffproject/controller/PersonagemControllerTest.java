package br.com.davidleme.rickmortybffproject.controller;

import br.com.davidleme.rickmortybffproject.dto.PaginacaoResponseDTO;
import br.com.davidleme.rickmortybffproject.dto.PersonagemResumoDTO;
import br.com.davidleme.rickmortybffproject.exception.ApiExceptionHandler;
import br.com.davidleme.rickmortybffproject.exception.PersonagemNaoEncontradoException;
import br.com.davidleme.rickmortybffproject.service.PersonagemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonagemController.class)
@Import(ApiExceptionHandler.class)
class PersonagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonagemService service;

    @Test
    void listar_deveRetornarFormatoObrigatorio() throws Exception {
        var response = new PaginacaoResponseDTO(
                1,
                List.of(
                        new PersonagemResumoDTO(1, "Rick Sanchez"),
                        new PersonagemResumoDTO(2, "Morty Smith")
                )
        );

        when(service.listar(1)).thenReturn(response);

        mockMvc.perform(get("/personagens").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.results[0].id").value(1))
                .andExpect(jsonPath("$.results[0].nome").value("Rick Sanchez"))
                .andExpect(jsonPath("$.results[1].id").value(2))
                .andExpect(jsonPath("$.results[1].nome").value("Morty Smith"));

        verify(service, times(1)).listar(1);
    }

    @Test
    void buscarPorNome_deveRetornarUmObjetoSemLista() throws Exception {
        when(service.buscarPorNome("rick"))
                .thenReturn(new PersonagemResumoDTO(1, "Rick Sanchez"));

        mockMvc.perform(get("/personagens/rick"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Rick Sanchez"))
                .andExpect(jsonPath("$.results").doesNotExist());

        verify(service, times(1)).buscarPorNome("rick");
    }

    @Test
    void buscarPorNome_quandoNaoEncontrar_deveRetornar404() throws Exception {
        when(service.buscarPorNome("naoexiste"))
                .thenThrow(new PersonagemNaoEncontradoException());

        mockMvc.perform(get("/personagens/naoexiste"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Personagem não encontrado"));

        verify(service, times(1)).buscarPorNome("naoexiste");
    }
}
