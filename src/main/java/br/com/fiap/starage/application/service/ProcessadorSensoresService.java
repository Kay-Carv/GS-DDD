package br.com.fiap.starage.application.service;

import br.com.fiap.starage.application.dto.DadoSensorDTO;
import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.model.StatusEstoque;
import br.com.fiap.starage.domain.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessadorSensoresService {

    @Autowired
    private ProdutoRepository produtoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper(); // Instância gerenciada automaticamente pelo Spring

    @Transactional
    public void processarMensagemMqtt(String payloadJson) {
        try {
            // 1. Converte o texto JSON que chegou do MQTT para o nosso Record (DTO)
            DadoSensorDTO dado = objectMapper.readValue(payloadJson, DadoSensorDTO.class);

            // 2. Busca o produto no banco
            Produto produto = produtoRepository.findById(dado.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado via sensor."));

            // 3. Atualiza a quantidade baseada no peso/leitura do sensor
            produto.setQuantidadeAtual(dado.quantidadeAtualizada());

            // 4. Salva no banco relacional
            produtoRepository.save(produto);

            System.out.println("✅ Estoque atualizado via MQTT: Produto " + produto.getNome() + " | Qtd: " + produto.getQuantidadeAtual());

            // 5. Verifica a Regra de Negócio de Nível Crítico (RN01)
            if (produto.verificarStatusEstoque() == StatusEstoque.CRITICO) {
                dispararAlertaCritico(produto);
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar dado do sensor: " + e.getMessage());
        }
    }

    private void dispararAlertaCritico(Produto produto) {
        System.out.println("🚨 ALERTA CRÍTICO: O produto ( " + produto.getNome() +
                " ) no armazém ( " + produto.getArmazem().getNome() +
                " ) atingiu nível crítico! (Abaixo de 20%)");
    }
}