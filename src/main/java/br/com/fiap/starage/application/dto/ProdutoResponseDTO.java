package br.com.fiap.starage.application.dto;

import br.com.fiap.starage.domain.model.StatusEstoque;
import java.time.LocalDate;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        Integer quantidadeAtual,
        Integer capacidadeMaxima,
        LocalDate dataValidade,
        StatusEstoque statusEstoque, // Calculado via RN01
        boolean vencido // Calculado via RN03
) {}