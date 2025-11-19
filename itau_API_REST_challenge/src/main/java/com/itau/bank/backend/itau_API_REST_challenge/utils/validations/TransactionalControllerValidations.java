package com.itau.bank.backend.itau_API_REST_challenge.utils.validations;
import com.itau.bank.backend.itau_API_REST_challenge.dto.TransactionalRequest;
import com.itau.bank.backend.itau_API_REST_challenge.exceptions.BusinessException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class TransactionalControllerValidations {

    public void validateRequestBodyDataHora(@Valid TransactionalRequest requestBody) {
        if (requestBody.getDataHora().isAfter(OffsetDateTime.now())) {
            throw new BusinessException(
                    "Unprocessable Entity",
                    "dataHora field can't be in future time: " + requestBody.getDataHora()
            );
        }
    }

    public void validateRequestBodyValor(@Valid TransactionalRequest requestBody) {
        if (requestBody.getValor() < 0) {
            throw new BusinessException(
                    "Unprocessable Entity",
                    "Valor field can't be less than 0: " + requestBody.getDataHora()
            );
        }
    }
}
