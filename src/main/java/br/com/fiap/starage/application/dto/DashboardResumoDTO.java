package br.com.fiap.starage.application.dto;

import java.util.List;

public record DashboardResumoDTO(
        long totalProdutosCriticos,
        long totalProdutosAtencao,
        String nomeArmazemMaisAtivo,
        List<PredicaoProdutoDTO> alertasImediatos
) {
}