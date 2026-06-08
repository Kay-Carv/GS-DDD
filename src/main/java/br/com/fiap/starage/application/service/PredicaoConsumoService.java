package br.com.fiap.starage.application.service;

import br.com.fiap.starage.application.dto.PredicaoProdutoDTO;
import br.com.fiap.starage.domain.model.HistoricoConsumo;
import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.repository.HistoricoConsumoRepository;
import br.com.fiap.starage.domain.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PredicaoConsumoService {

    private final ProdutoRepository produtoRepository;
    private final HistoricoConsumoRepository historicoConsumoRepository;
    private static final int DIAS_ANALISE = 7; // Baseia a média nos últimos 7 dias

    public PredicaoConsumoService(ProdutoRepository produtoRepository, HistoricoConsumoRepository historicoConsumoRepository) {
        this.produtoRepository = produtoRepository;
        this.historicoConsumoRepository = historicoConsumoRepository;
    }

    @Transactional(readOnly = true)
    public PredicaoProdutoDTO preverEsgotamento(Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        LocalDateTime dataInicio = LocalDateTime.now().minusDays(DIAS_ANALISE);
        LocalDateTime dataFim = LocalDateTime.now();

        // Busca o histórico recente do produto
        List<HistoricoConsumo> historicoRecente = historicoConsumoRepository
                .findByProdutoIdAndDataHoraBetween(idProduto, dataInicio, dataFim);

        // Se não tem histórico de consumo, não há como prever, considera seguro
        if (historicoRecente.isEmpty() || produto.getQuantidadeAtual() == 0) {
            return gerarPredicaoSemDados(produto);
        }

        // Soma todo o consumo dos últimos 7 dias
        BigDecimal consumoTotalPeriodo = historicoRecente.stream()
                .map(HistoricoConsumo::getQuantidadeConsumida)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcula a média diária
        BigDecimal mediaDiaria = consumoTotalPeriodo.divide(BigDecimal.valueOf(DIAS_ANALISE), 2, RoundingMode.HALF_UP);

        // Se a média for 0 (ex: consumos muito fragmentados ou estornados), evita divisão por zero
        if (mediaDiaria.compareTo(BigDecimal.ZERO) == 0) {
            return gerarPredicaoSemDados(produto);
        }

        // Calcula os dias restantes (Estoque Atual / Média Diária)
        BigDecimal estoqueAtual = BigDecimal.valueOf(produto.getQuantidadeAtual());
        int diasRestantes = estoqueAtual.divide(mediaDiaria, 0, RoundingMode.DOWN).intValue();

        // Projeta a data de esgotamento
        LocalDate dataEsgotamento = LocalDate.now().plusDays(diasRestantes);

        return new PredicaoProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getArmazem().getNome(),
                produto.getQuantidadeAtual(),
                mediaDiaria,
                diasRestantes,
                dataEsgotamento,
                definirNivelAlerta(diasRestantes)
        );
    }

    private PredicaoProdutoDTO gerarPredicaoSemDados(Produto produto) {
        return new PredicaoProdutoDTO(
                produto.getId(), produto.getNome(), produto.getArmazem().getNome(),
                produto.getQuantidadeAtual(), BigDecimal.ZERO, null, null, "ANALISE_INSUFICIENTE"
        );
    }

    // Regra de negócio para classificar a urgência (pode ser ajustada)
    private String definirNivelAlerta(int diasRestantes) {
        if (diasRestantes <= 3) return "CRITICO";
        if (diasRestantes <= 7) return "ATENCAO";
        return "SEGURO";
    }
}