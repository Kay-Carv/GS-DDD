package br.com.fiap.starage.domain.repository;

import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.model.StatusEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Útil para a Dashboard carregar os níveis críticos rapidamente (RF04)
    List<Produto> findByArmazemId(Long armazemId);

    // Busca produtos próximos ao vencimento para disparar o alerta (RN03)
    List<Produto> findByDataValidadeBefore(LocalDate dataLimite);
}