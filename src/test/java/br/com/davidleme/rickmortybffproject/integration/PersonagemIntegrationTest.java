package br.com.davidleme.rickmortybffproject.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "rickmorty.api.url=http://localhost:${wiremock.server.port}/api"
})
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
class PersonagemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarPersonagens_deveRetornarFormatoObrigatorio() throws Exception {
        WireMock.stubFor(WireMock.get(urlEqualTo("/api/character?page=1"))
                .willReturn(okJson("""
                        {
                          "info": { "count": 826, "pages": 42, "next": "x", "prev": null },
                          "results": [
                            { "id": 1, "name": "Rick Sanchez", "status": "Alive" },
                            { "id": 2, "name": "Morty Smith", "status": "Alive" }
                          ]
                        }
                        """)));

        mockMvc.perform(get("/personagens").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.results[0].id").value(1))
                .andExpect(jsonPath("$.results[0].nome").value("Rick Sanchez"))
                .andExpect(jsonPath("$.results[1].id").value(2))
                .andExpect(jsonPath("$.results[1].nome").value("Morty Smith"));
    }

    @Test
    void buscarPorNome_deveRetornarUmObjetoSemLista() throws Exception {
        WireMock.stubFor(WireMock.get(urlEqualTo("/api/character?name=rick"))
                .willReturn(okJson("""
                        {
                          "info": { "count": 2, "pages": 1, "next": null, "prev": null },
                          "results": [
                            { "id": 1, "name": "Rick Sanchez" },
                            { "id": 8, "name": "Adjudicator Rick" }
                          ]
                        }
                        """)));

        mockMvc.perform(get("/personagens/rick"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Rick Sanchez"))
                .andExpect(jsonPath("$.results").doesNotExist());
    }

    @Test
    void buscarPorNome_quandoApiExternaRetornar404_deveRetornar404NoBff() throws Exception {
        WireMock.stubFor(WireMock.get(urlEqualTo("/api/character?name=naoexiste"))
                .willReturn(aResponse().withStatus(404).withBody("""
                        { "error": "There is nothing here" }
                        """)));

        mockMvc.perform(get("/personagens/naoexiste"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Personagem não encontrado"));
    }
}
