package br.com.fiap.starage.domain.repository;

import br.com.fiap.starage.domain.model.Armazem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArmazemRepository extends JpaRepository<Armazem, Long> {

    // Suporte para buscar áreas periféricas que exigem prioridade (RN02)
    List<Armazem> findByRegiaoVulneravelTrueAndAtivoTrue();
}