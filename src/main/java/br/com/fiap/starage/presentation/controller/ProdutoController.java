package br.com.fiap.starage.presentation.controller;

import br.com.fiap.starage.application.dto.ProdutoRequestDTO;
import br.com.fiap.starage.application.dto.ProdutoResponseDTO;
import br.com.fiap.starage.domain.model.Armazem;
import br.com.fiap.starage.domain.model.Produto;
import br.com.fiap.starage.domain.repository.ArmazemRepository;
import br.com.fiap.starage.domain.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ArmazemRepository armazemRepository;

    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO dto) {

        Armazem armazem = armazemRepository.findById(dto.armazemId())
                .orElseThrow(() -> new IllegalArgumentException("Armazém não encontrado."));

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setQuantidadeAtual(dto.quantidadeAtual());
        produto.setCapacidadeMaxima(dto.capacidadeMaxima());
        produto.setDataValidade(dto.dataValidade());
        produto.setArmazem(armazem);

        Produto salvo = produtoRepository.save(produto);

        // Mapeando a entidade de volta para o DTO de resposta
        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getQuantidadeAtual(),
                salvo.getCapacidadeMaxima(),
                salvo.getDataValidade(),
                salvo.verificarStatusEstoque(), // Dispara a lógica da RN01
                salvo.isVencido()               // Dispara a lógica da RN03
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Iterable<ProdutoResponseDTO>> listarTodos() {
        var lista = produtoRepository.findAll().stream()
                .map(p -> new ProdutoResponseDTO(
                        p.getId(),
                        p.getNome(),
                        p.getQuantidadeAtual(),
                        p.getCapacidadeMaxima(),
                        p.getDataValidade(),
                        p.verificarStatusEstoque(),
                        p.isVencido()
                )).toList();

        return ResponseEntity.ok(lista);
    }
}