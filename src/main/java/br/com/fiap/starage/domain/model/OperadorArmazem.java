package br.com.fiap.starage.domain.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("OPERADOR_ARMAZEM")
public class OperadorArmazem extends Usuario {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "armazem_id")
    private Armazem armazemVinculado;

    @Override
    public boolean podeAcessarArmazem(Long armazemId) {
        // Acessa apenas o armazém vinculado (RN05)
        return armazemVinculado != null && armazemVinculado.getId().equals(armazemId);
    }
}