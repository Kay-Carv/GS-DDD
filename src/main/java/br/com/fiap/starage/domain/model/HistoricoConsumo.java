package br.com.fiap.starage.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // Exigido pela JPA para criar a entidade em tempo de execução
@Entity
@Table(name = "tb_historico_consumo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true) // Protege contra queries indesejadas nos relacionamentos
public class HistoricoConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    // Usamos FetchType.LAZY por boa prática para não sobrecarregar o banco de dados
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto", nullable = false)
    @NotNull(message = "O produto é obrigatório no histórico.")
    private Produto produto; // Sem ToString.Include para manter o log leve e performático

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_armazem", nullable = false)
    @NotNull(message = "O armazém é obrigatório no histórico.")
    private Armazem armazem; // Sem ToString.Include para evitar loops ou carregamentos pesados

    @Column(nullable = false)
    @NotNull(message = "A quantidade consumida é obrigatória.")
    @ToString.Include
    private BigDecimal quantidadeConsumida;

    @Column(nullable = false)
    @NotNull(message = "A data e hora do registro são obrigatórias.")
    @ToString.Include
    private LocalDateTime dataHora;

    // Construtor auxiliar customizado (ajuda muito na hora de salvar novos eventos vindo dos sensores)
    public HistoricoConsumo(Produto produto, Armazem armazem, BigDecimal quantidadeConsumida) {
        this.produto = produto;
        this.armazem = armazem;
        this.quantidadeConsumida = quantidadeConsumida;
        this.dataHora = LocalDateTime.now(); // Define o timestamp exato do evento automaticamente
    }
}