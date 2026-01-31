package br.com.davidleme.rickmortybffproject.controller;

import br.com.davidleme.rickmortybffproject.dto.PaginacaoResponseDTO;
import br.com.davidleme.rickmortybffproject.dto.PersonagemResumoDTO;
import br.com.davidleme.rickmortybffproject.service.PersonagemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personagens")
public class PersonagemController {

    private final PersonagemService service;

    public PersonagemController(PersonagemService service) {
        this.service = service;
    }

    @GetMapping
    public PaginacaoResponseDTO listar(@RequestParam int page) {
        return service.listar(page);
    }

    @GetMapping("/{nome}")
    public PersonagemResumoDTO buscarPorNome(@PathVariable String nome) {
        return service.buscarPorNome(nome);
    }
}