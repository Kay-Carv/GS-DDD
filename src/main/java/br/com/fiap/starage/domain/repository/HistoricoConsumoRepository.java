package br.com.fiap.starage.domain.repository;

import br.com.fiap.starage.domain.model.HistoricoConsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoConsumoRepository extends JpaRepository<HistoricoConsumo, Long> {

    // Filtra histórico completo combinando produto, armazém e intervalo de tempo (Período)
    List<HistoricoConsumo> findByProdutoIdAndArmazemIdAndDataHoraBetween(
            Long idProduto,
            Long idArmazem,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

    // Filtra histórico por armazém e período
    List<HistoricoConsumo> findByArmazemIdAndDataHoraBetween(Long idArmazem, LocalDateTime dataInicio, LocalDateTime dataFim);

    // Filtra histórico por produto e período
    List<HistoricoConsumo> findByProdutoIdAndDataHoraBetween(Long idProduto, LocalDateTime dataInicio, LocalDateTime dataFim);
}