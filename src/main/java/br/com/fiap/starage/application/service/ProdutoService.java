package br.com.fiap.starage.application.service;

import br.com.fiap.starage.application.dto.ProdutoRequestDTO;
import br.com.fiap.starage.application.dto.ProdutoResponseDTO;
import br.com.fiap.starage.domain.model.Armazem;
import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.repository.ArmazemRepository;
import br.com.fiap.starage.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ArmazemRepository armazemRepository;

    @Transactional
    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO dto) {
        Armazem armazem = armazemRepository.findById(dto.armazemId())
                .orElseThrow(() -> new IllegalArgumentException("Armazém com ID " + dto.armazemId() + " não encontrado."));

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setQuantidadeAtual(dto.quantidadeAtual());
        produto.setCapacidadeMaxima(dto.capacidadeMaxima());
        produto.setDataValidade(dto.dataValidade());
        produto.setArmazem(armazem);

        Produto salvo = produtoRepository.save(produto);

        // Verificações automáticas pós-cadastro (HU02 e HU03)
        // Aqui futuramente você pode injetar um NotificadorService para disparar emails caso já entre crítico

        return new ProdutoResponseDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getQuantidadeAtual(),
                salvo.getCapacidadeMaxima(),
                salvo.getDataValidade(),
                salvo.verificarStatusEstoque(),
                salvo.isVencido()
        );
    }
}