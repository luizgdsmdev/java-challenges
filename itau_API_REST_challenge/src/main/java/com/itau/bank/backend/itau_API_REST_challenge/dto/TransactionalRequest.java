package com.itau.bank.backend.itau_API_REST_challenge.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class TransactionalRequest {
    @NotNull
    @Min(0)
    private double valor;

    @NotNull(message = "dataHora não deve ser nulo")
    private OffsetDateTime dataHora;

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
