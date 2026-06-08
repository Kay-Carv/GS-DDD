package br.com.fiap.starage.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PredicaoProdutoDTO(
        Long idProduto,
        String nomeProduto,
        String nomeArmazem,
        Integer quantidadeAtual,
        BigDecimal consumoMedioDiario,
        Integer diasRestantes,
        LocalDate dataEsgotamentoPrevista,
        String nivelAlerta // EX: "CRITICO", "ATENCAO", "SEGURO"
) {}