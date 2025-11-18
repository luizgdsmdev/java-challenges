package com.itau.bank.backend.itau_API_REST_challenge.exceptions.Records;
import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String details,
        LocalDateTime timestamp
) {}
