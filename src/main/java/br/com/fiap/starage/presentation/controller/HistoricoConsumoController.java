package br.com.fiap.starage.presentation.controller;

import br.com.fiap.starage.application.dto.HistoricoConsumoResponseDTO; // Imports corretos
import br.com.fiap.starage.application.service.HistoricoConsumoService;  // Imports corretos
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/historico")
@Tag(name = "Histórico de Consumo", description = "Endpoints para consulta de movimentações e auditoria de consumo")
public class HistoricoConsumoController {

    private final HistoricoConsumoService service;

    public HistoricoConsumoController(HistoricoConsumoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar histórico com filtros dinâmicos",
            description = "Retorna uma lista paginável ou filtrada de consumos por produto, armazém e intervalos de datas.")
    public ResponseEntity<List<HistoricoConsumoResponseDTO>> obterHistorico(
            @RequestParam(required = false) Long idProduto,
            @RequestParam(required = false) Long idArmazem,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        List<HistoricoConsumoResponseDTO> historico = service.listarComFiltros(idProduto, idArmazem, dataInicio, dataFim);
        return ResponseEntity.ok(historico);
    }
}
