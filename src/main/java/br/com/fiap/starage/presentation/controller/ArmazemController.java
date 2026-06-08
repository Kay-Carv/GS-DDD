package br.com.fiap.starage.presentation.controller;

import br.com.fiap.starage.application.dto.ArmazemRequestDTO;
import br.com.fiap.starage.domain.model.Armazem;
import br.com.fiap.starage.domain.repository.ArmazemRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/armazens")
public class ArmazemController {

    @Autowired
    private ArmazemRepository armazemRepository;

    @PostMapping
    public ResponseEntity<Armazem> cadastrarArmazem(@RequestBody @Valid ArmazemRequestDTO dto) {
        Armazem armazem = new Armazem();
        armazem.setNome(dto.nome());
        armazem.setLocalizacao(dto.localizacao());
        armazem.setRegiaoVulneravel(dto.regiaoVulneravel());

        Armazem salvo = armazemRepository.save(armazem);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Armazem>> listarTodos() {
        return ResponseEntity.ok(armazemRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarArmazem(@PathVariable Long id) {
        Optional<Armazem> armazemOpt = armazemRepository.findById(id);

        if (armazemOpt.isPresent()) {
            Armazem armazem = armazemOpt.get();
            // A regra de negócio RN08 é validada dentro do método desativar() da entidade
            armazem.desativar();
            armazemRepository.save(armazem);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}