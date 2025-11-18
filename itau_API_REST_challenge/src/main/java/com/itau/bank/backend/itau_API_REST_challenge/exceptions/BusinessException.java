package com.itau.bank.backend.itau_API_REST_challenge.exceptions;

public class BusinessException extends RuntimeException {
    private final String title;
    private final String message;

    public BusinessException(String title, String message) {
        super(message);
        this.title = title;
        this.message = message;
    }

    public String getTitle() { return title; }
    public String getMessageDetail() { return message; }
}
