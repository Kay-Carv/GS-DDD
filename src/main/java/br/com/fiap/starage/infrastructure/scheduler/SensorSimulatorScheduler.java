package br.com.fiap.starage.infrastructure.scheduler;

import br.com.fiap.starage.domain.model.HistoricoConsumo;
import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.repository.HistoricoConsumoRepository;
import br.com.fiap.starage.domain.repository.ProdutoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
public class SensorSimulatorScheduler {

    private final ProdutoRepository produtoRepository;
    private final HistoricoConsumoRepository historicoConsumoRepository;
    private final Random random = new Random();

    public SensorSimulatorScheduler(ProdutoRepository produtoRepository, HistoricoConsumoRepository historicoConsumoRepository) {
        this.produtoRepository = produtoRepository;
        this.historicoConsumoRepository = historicoConsumoRepository;
    }

    // Roda automaticamente a cada 15 segundos (15000 milissegundos)
    @Scheduled(fixedRate = 15000)
    @Transactional
    public void simularLeituraSensores() {
        // Busca todos os produtos cadastrados no banco
        List<Produto> produtos = produtoRepository.findAll();

        // Se não houver produtos cadastrados, o robô não faz nada
        if (produtos.isEmpty()) {
            return;
        }

        // Escolhe um produto aleatório para simular a movimentação
        Produto produto = produtos.get(random.nextInt(produtos.size()));

        // Simula um consumo aleatório entre 1 e 5 unidades
        int valorConsumido = random.nextInt(5) + 1;
        Integer quantidadeAtual = produto.getQuantidadeAtual();

        // Impede que o estoque fique negativo (matemática simples de Integer)
        if (quantidadeAtual != null && quantidadeAtual >= valorConsumido) {

            // 1. Atualiza o estoque do produto com a subtração normal
            produto.setQuantidadeAtual(quantidadeAtual - valorConsumido);
            produtoRepository.save(produto);

            // 2. Grava a movimentação na tabela de auditoria/histórico
            // Convertemos o int para BigDecimal apenas para salvar no histórico
            BigDecimal consumoParaHistorico = BigDecimal.valueOf(valorConsumido);
            HistoricoConsumo evento = new HistoricoConsumo(produto, produto.getArmazem(), consumoParaHistorico);
            historicoConsumoRepository.save(evento);

            // 3. Log visual para você acompanhar no terminal
            System.out.println("📡 [SENSOR IoT SIMULADO] Consumo: " + valorConsumido +
                    " unidades de '" + produto.getNome() +
                    "' no '" + produto.getArmazem().getNome() +
                    "'. Estoque atual: " + produto.getQuantidadeAtual());
        }
    }
}