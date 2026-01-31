package br.com.davidleme.rickmortybffproject.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class RickMortyClientTest {

    private RickMortyClient client;
    private MockRestServiceServer server;

    @BeforeEach
    void setup() {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        server = MockRestServiceServer.bindTo(restTemplate).build();

        RestTemplateBuilder builder = new RestTemplateBuilder() {
            @Override
            public RestTemplate build() {
                return restTemplate;
            }
        };

        client = new RickMortyClient(builder);

        // evita depender de application.yml
        ReflectionTestUtils.setField(client, "baseUrl", "http://fake/api");
    }

    @Test
    void buscarPersonagens_deveChamarUrlCorreta_eRetornarResultados() {
        server.expect(requestTo("http://fake/api/character?page=1"))
                .andRespond(withSuccess("""
                        {
                          "results": [
                            { "id": 1, "name": "Rick Sanchez" }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        Map response = client.buscarPersonagens(1);
        List<Map> results = (List<Map>) response.get("results");

        assertEquals(1, results.size());
        assertEquals(1, results.get(0).get("id"));
        assertEquals("Rick Sanchez", results.get(0).get("name"));

        server.verify();
    }

    @Test
    void buscarPersonagemPorNome_deveChamarUrlCorreta_eRetornarResultados() {
        server.expect(requestTo("http://fake/api/character?name=rick"))
                .andRespond(withSuccess("""
                        {
                          "results": [
                            { "id": 1, "name": "Rick Sanchez" }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        Map response = client.buscarPersonagemPorNome("rick");
        List<Map> results = (List<Map>) response.get("results");

        assertEquals(1, results.size());
        assertEquals("Rick Sanchez", results.get(0).get("name"));

        server.verify();
    }
}
