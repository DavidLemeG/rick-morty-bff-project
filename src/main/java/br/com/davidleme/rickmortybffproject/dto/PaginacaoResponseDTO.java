package br.com.davidleme.rickmortybffproject.dto;

import java.util.List;

public record PaginacaoResponseDTO(
        Integer page,
        List<PersonagemResumoDTO> results
) {}