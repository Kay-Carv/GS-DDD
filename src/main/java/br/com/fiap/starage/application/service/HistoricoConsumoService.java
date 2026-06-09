package br.com.fiap.starage.application.service;



import br.com.fiap.starage.application.dto.HistoricoConsumoResponseDTO;
import br.com.fiap.starage.domain.model.HistoricoConsumo;
import br.com.fiap.starage.domain.repository.HistoricoConsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoConsumoService {

    private final HistoricoConsumoRepository repository;

    // Injeção via construtor (boa prática que dispensa o @Autowired)
    public HistoricoConsumoService(HistoricoConsumoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<HistoricoConsumoResponseDTO> listarComFiltros(
            Long idProduto,
            Long idArmazem,
            LocalDateTime dataInicio,
            LocalDateTime dataFim) {

        // Define um período padrão de 30 dias caso o usuário não envie datas
        LocalDateTime inicio = (dataInicio != null) ? dataInicio : LocalDateTime.now().minusDays(30);
        LocalDateTime fim = (dataFim != null) ? dataFim : LocalDateTime.now();

        List<HistoricoConsumo> resultados;

        // Decisão dos filtros dinâmicos com base nos parâmetros fornecidos
        if (idProduto != null && idArmazem != null) {
            resultados = repository.findByProdutoIdAndArmazemIdAndDataHoraBetween(idProduto, idArmazem, inicio, fim);
        } else if (idProduto != null) {
            resultados = repository.findByProdutoIdAndDataHoraBetween(idProduto, inicio, fim);
        } else if (idArmazem != null) {
            resultados = repository.findByArmazemIdAndDataHoraBetween(idArmazem, inicio, fim);
        } else {
            // Opcional: Adicionar findByDataHoraBetween no Repository se quiser buscar global por período
            resultados = repository.findAll().stream()
                    .filter(h -> !h.getDataHora().isBefore(inicio) && !h.getDataHora().isAfter(fim))
                    .collect(Collectors.toList());
        }

        // Mapeamento seguro da Entidade para o DTO dentro do escopo transacional
        return resultados.stream()
                .map(h -> new HistoricoConsumoResponseDTO(
                        h.getId(),
                        h.getProduto().getId(),
                        h.getProduto().getNome(),
                        h.getArmazem().getId(),
                        h.getArmazem().getNome(),
                        h.getQuantidadeConsumida(),
                        h.getDataHora()
                ))
                .collect(Collectors.toList());
    }
}
