package br.com.fiap.starage.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoConsumoResponseDTO(
        Long id,
        Long idProduto,
        String nomeProduto,
        Long idArmazem,
        String nomeArmazem,
        BigDecimal quantidadeConsumida,
        LocalDateTime dataHora
) {}