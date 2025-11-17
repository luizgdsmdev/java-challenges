package com.itau.bank.backend.itau_API_REST_challenge.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class TransationalRequest {
    @NotNull
    @Min(1)
    private double value;

    @NotNull
    private OffsetDateTime dateHour;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public OffsetDateTime getDateHour() {
        return dateHour;
    }

    public void setDateHour(OffsetDateTime dateHour) {
        this.dateHour = dateHour;
    }
}
