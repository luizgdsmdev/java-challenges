package com.itau.bank.backend.itau_API_REST_challenge;
import com.itau.bank.backend.itau_API_REST_challenge.dto.TransactionalRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ItauApiRestChallengeTests {
    @Autowired
    private TestRestTemplate restTemplate;

    //-------------------------- creation transaction: succeeds
    @DisplayName("POST: /transacao -> (201 created) | Creation succeeds with valid data.")
    @Test
    void shouldCreateTransacao_WhenValidData() {
        TransactionalRequest request = new TransactionalRequest();
        request.setDataHora(OffsetDateTime.now().minusSeconds(60));
        request.setValor(0.0);

        var response = restTemplate.postForEntity("/transacao", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNull();
    }


}
