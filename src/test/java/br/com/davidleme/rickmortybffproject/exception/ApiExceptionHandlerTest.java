package br.com.davidleme.rickmortybffproject.exception;

import br.com.davidleme.rickmortybffproject.controller.PersonagemController;
import br.com.davidleme.rickmortybffproject.service.PersonagemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonagemController.class)
@Import(ApiExceptionHandler.class)
class ApiExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonagemService service;

    @Test
    void quandoPersonagemNaoEncontrado_deveRetornar404ComMensagem() throws Exception {
        when(service.buscarPorNome("naoexiste"))
                .thenThrow(new PersonagemNaoEncontradoException());

        mockMvc.perform(get("/personagens/naoexiste"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem")
                        .value("Personagem não encontrado"));
    }
}
