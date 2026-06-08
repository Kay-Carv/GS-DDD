package br.com.fiap.starage.application.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ProdutoRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        @NotNull @Positive
        Integer quantidadeAtual,

        @NotNull @Positive
        Integer capacidadeMaxima,

        @FutureOrPresent(message = "A data de validade não pode estar no passado.")
        LocalDate dataValidade,

        @NotNull
        Long armazemId
) {}