package br.com.fiap.starage.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArmazemRequestDTO(
        @NotBlank(message = "O nome do armazém é obrigatório.")
        String nome,

        @NotBlank(message = "A localização é obrigatória.")
        String localizacao,

        @NotNull(message = "Informe se é uma região vulnerável.")
        Boolean regiaoVulneravel
) {}