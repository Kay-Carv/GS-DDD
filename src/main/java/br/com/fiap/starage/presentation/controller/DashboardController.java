package br.com.fiap.starage.presentation.controller;

import br.com.fiap.starage.application.dto.DashboardResumoDTO;
import br.com.fiap.starage.application.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard de Gestão", description = "Endpoints consolidados com KPIs para o painel inicial")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/resumo")
    @Operation(summary = "Obter resumo executivo", description = "Retorna métricas de risco e a lista de produtos em nível crítico/atenção.")
    public ResponseEntity<DashboardResumoDTO> obterResumo() {
        return ResponseEntity.ok(service.gerarResumo());
    }
}
