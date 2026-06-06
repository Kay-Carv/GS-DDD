package br.com.fiap.starage.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Table(name = "tb_produto")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantidadeAtual;

    @Column(nullable = false)
    private Integer capacidadeMaxima;

    private LocalDate dataValidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "armazem_id", nullable = false)
    private Armazem armazem;

    // --- REGRAS DE NEGÓCIO EMBUTIDAS NA ENTIDADE (DDD) ---

    // RN01 - Limiar de Estoque Crítico (20% da capacidade)
    public StatusEstoque verificarStatusEstoque() {
        double limiteCritico = this.capacidadeMaxima * 0.20;

        if (this.quantidadeAtual <= limiteCritico) {
            return StatusEstoque.CRITICO;
        } else if (this.quantidadeAtual <= (this.capacidadeMaxima * 0.40)) {
            return StatusEstoque.ATENCAO;
        }
        return StatusEstoque.NORMAL;
    }

    // RN03 - Alerta de vencimento (10 dias de antecedência)
    public boolean isProximoVencimento() {
        if (dataValidade == null) return false;
        long diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
        return diasParaVencer > 0 && diasParaVencer <= 10;
    }

    // RN03 - Bloqueio automático de lote vencido
    public boolean isVencido() {
        if (dataValidade == null) return false;
        return LocalDate.now().isAfter(dataValidade) || LocalDate.now().isEqual(dataValidade);
    }
}