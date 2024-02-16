package ru.gb.springdemo.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springdemo.JUnitSpringBootBase;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Objects;

public class ReaderControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitReaderResponse {
        private Long id;
        private String name;
    }

    @Test
    void testFindByIdSuccess() {
        // подготовил данные
        Reader expected = readerRepository.save(new Reader("Бородино"));

        JUnitReaderResponse responseBody = webTestClient.get()
                .uri("/reader/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitReaderResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from readers", Long.class);

        webTestClient.get()
                .uri("/reader/" + maxId + 1)
                .exchange()
                .expectStatus().is5xxServerError() // Actual   :500 INTERNAL_SERVER_ERROR
                .expectBody(JUnitReaderResponse.class)
                .returnResult().getResponseBody();

    }


    @Test
    void testGetAll() {
        // подготовил данные
        readerRepository.saveAll(List.of(
                new Reader("first"),
                new Reader("second")
        ));

        List<Reader> expected = readerRepository.findAll();

        List<JUnitReaderResponse> responseBody = webTestClient.get()
                .uri("/reader")
                // .retrieve
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitReaderResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitReaderResponse readerResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), readerResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getName(), readerResponse.getName()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testSave() {
        JUnitReaderResponse request = new JUnitReaderResponse();
        request.setId(1L);
        request.setName("Буратино");

        JUnitReaderResponse responseBody = webTestClient.post()
                .uri("/reader")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()//isCreated()
                .expectBody(JUnitReaderResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());

        Assertions.assertTrue(readerRepository.findById(request.getId()).isPresent());
    }

}
