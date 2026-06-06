package br.com.fiap.starage.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GESTOR_MUNICIPAL")
public class GestorMunicipal extends Usuario {
    @Override
    public boolean podeAcessarArmazem(Long armazemId) {
        return true; // Gestor acessa todos os armazéns do município (RN05)
    }
}