package br.com.fiap.starage.application.service;

import br.com.fiap.starage.application.dto.DashboardResumoDTO;
import br.com.fiap.starage.application.dto.PredicaoProdutoDTO;
import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final PredicaoConsumoService predicaoService;

    public DashboardService(ProdutoRepository produtoRepository, PredicaoConsumoService predicaoService) {
        this.produtoRepository = produtoRepository;
        this.predicaoService = predicaoService;
    }

    public DashboardResumoDTO gerarResumo() {
        // 1. Busca todos os produtos do banco
        List<Produto> todosProdutos = produtoRepository.findAll();

        // 2. Calcula a predição para cada um (reaproveitando o seu algoritmo)
        List<PredicaoProdutoDTO> predicoes = todosProdutos.stream()
                .map(p -> predicaoService.preverEsgotamento(p.getId()))
                .toList();

        // 3. Conta os indicadores de risco
        long criticos = predicoes.stream()
                .filter(p -> "CRITICO".equals(p.nivelAlerta()))
                .count();

        long atencao = predicoes.stream()
                .filter(p -> "ATENCAO".equals(p.nivelAlerta()))
                .count();

        // 4. Filtra a lista para mostrar no painel apenas o que precisa de ação
        List<PredicaoProdutoDTO> alertas = predicoes.stream()
                .filter(p -> "CRITICO".equals(p.nivelAlerta()) || "ATENCAO".equals(p.nivelAlerta()))
                .toList();

        // Placeholder para o armazém mais ativo (podemos evoluir essa lógica depois)
        String armazemMaisAtivo = "Consolidado Geral";

        return new DashboardResumoDTO(criticos, atencao, armazemMaisAtivo, alertas);
    }
}