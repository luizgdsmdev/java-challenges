package com.itau.bank.backend.itau_API_REST_challenge;
import com.itau.bank.backend.itau_API_REST_challenge.dto.TransactionalRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ItauApiRestChallengeTests {
    @Autowired
    private TestRestTemplate restTemplate;

    //-------------------------- creation transaction: succeeds
    @DisplayName("POST: /transacao -> (201 created) | Transaction succeeds with valid data.")
    @Test
    void shouldCreateTransacao_WhenValidData() {
        TransactionalRequest request = new TransactionalRequest();
        request.setDataHora(OffsetDateTime.now().minusSeconds(60));
        request.setValor(0.0);

        var response = restTemplate.postForEntity("/transacao", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNull();
    }

    //-------------------------- creation transaction: failure
    @DisplayName("POST: /transacao -> (400 Bad Request) | Transaction fails when valor value is less than 0.")
    @Test
    void transacaoFails_WhenValorIsLesserThan0(){
        TransactionalRequest request = new TransactionalRequest();
        request.setDataHora(OffsetDateTime.now().minusSeconds(60));
        request.setValor(-10.0);

        ResponseEntity<String> response = restTemplate.postForEntity("/transacao", request, String.class);
        String body = response.getBody();

        assertThat(body).contains("\"message\":\"Invalid data\"");
        assertThat(body).contains("\"details\":\"valor: deve ser maior que ou igual à 0\"");
        assertThat(body)
        .contains("\"timestamp\":\"")
        .matches(".*\"timestamp\":\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?([+-]\\d{2}:\\d{2}|Z)?\".*");
    }


    @DisplayName("POST: /transacao -> (409 Conflict) | Transaction fails when dataHora value is 'in future' considering current time.")
    @Test
    void transacaoFails_WhenDataHoraIsInFuture(){
        var dataHora = OffsetDateTime.now().plusSeconds(60);
        TransactionalRequest request = new TransactionalRequest();
        request.setDataHora(dataHora);
        request.setValor(0.0);

        ResponseEntity<String> response = restTemplate.postForEntity("/transacao", request, String.class);
        String body = response.getBody();

        String dataHoraAdjust = dataHora
        .atZoneSameInstant(ZoneOffset.UTC)
        .toOffsetDateTime()
        .toString();

        assertThat(body).contains("\"message\":\"Unprocessable Entity\"");
        assertThat(body).contains("\"details\":\"dataHora field can't be in future time: " + dataHoraAdjust + "\"");
        assertThat(body)
        .contains("\"timestamp\":\"")
        .matches(".*\"timestamp\":\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?([+-]\\d{2}:\\d{2}|Z)?\".*");
    }


}
