package com.itau.bank.backend.itau_API_REST_challenge.model;

import java.time.OffsetDateTime;

public class Transaction {
    private double value;
    private OffsetDateTime dateHour;

    public Transaction() {}

    public Transaction(double value, OffsetDateTime dateHour) {
        this.value = value;
        this.dateHour = dateHour;
    }

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
