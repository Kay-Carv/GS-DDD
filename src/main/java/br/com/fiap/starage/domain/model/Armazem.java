package br.com.fiap.starage.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tb_armazem")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Armazem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String localizacao;

    private boolean regiaoVulneravel; // Define prioridade (RN02)

    private boolean ativo = true;

    @OneToMany(mappedBy = "armazem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos = new ArrayList<>();

    // Comportamento do domínio
    public void desativar() {
        if (!this.produtos.isEmpty()) {
            throw new IllegalStateException("Não é possível desativar um armazém com estoque ativo (RN08).");
        }
        this.ativo = false;
    }
}