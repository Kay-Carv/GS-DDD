package br.com.fiap.starage.presentation.controller;

import br.com.fiap.starage.application.dto.PredicaoProdutoDTO;
import br.com.fiap.starage.application.service.PredicaoConsumoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predicao")
@Tag(name = "Inteligência de Predição", description = "Endpoints de algoritmos para estimativa de esgotamento de estoque")
public class PredicaoController {

    private final PredicaoConsumoService service;

    public PredicaoController(PredicaoConsumoService service) {
        this.service = service;
    }

    @GetMapping("/produto/{idProduto}")
    @Operation(summary = "Prever esgotamento de um produto",
            description = "Analisa o histórico recente e calcula a data estimada para o estoque zerar.")
    public ResponseEntity<PredicaoProdutoDTO> preverEsgotamentoProduto(@PathVariable Long idProduto) {
        try {
            PredicaoProdutoDTO predicao = service.preverEsgotamento(idProduto);
            return ResponseEntity.ok(predicao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
