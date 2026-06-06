package br.com.fiap.starage.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {
    @Override
    public boolean podeAcessarArmazem(Long armazemId) {
        return true; // Admin acessa tudo (RN05)
    }
}