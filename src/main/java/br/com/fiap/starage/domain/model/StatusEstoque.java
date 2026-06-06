package br.com.fiap.starage.domain.model;

public enum StatusEstoque {
    NORMAL,
    ATENCAO, // Quando começa a baixar
    CRITICO  // Abaixo de 20% (Regra RN01)
}
