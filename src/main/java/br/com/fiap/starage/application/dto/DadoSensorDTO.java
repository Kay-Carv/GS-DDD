package br.com.fiap.starage.application.dto;

public record DadoSensorDTO(
        Long armazemId,
        Long produtoId,
        Integer quantidadeAtualizada,
        Double temperatura

) { }