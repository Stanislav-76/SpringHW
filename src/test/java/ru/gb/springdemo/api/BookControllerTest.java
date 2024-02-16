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
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;
import java.util.Objects;


//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
class BookControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitBookResponse {
        private Long id;
        private String name;
    }

    @Test
    void testFindByIdSuccess() {
        // подготовил данные
        Book expected = bookRepository.save(new Book("Бородино"));

        JUnitBookResponse responseBody = webTestClient.get()
                .uri("/book/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitBookResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from books", Long.class);

        webTestClient.get()
                .uri("/book/" + maxId + 1)
                .exchange()
                .expectStatus().is5xxServerError(); // Actual   :500 INTERNAL_SERVER_ERROR
    }


    @Test
    void testGetAll() {
        // подготовил данные
        bookRepository.saveAll(List.of(
                new Book("first"),
                new Book("second")
        ));

        List<Book> expected = bookRepository.findAll();

        List<JUnitBookResponse> responseBody = webTestClient.get()
                .uri("/book")
                // .retrieve
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitBookResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitBookResponse bookResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), bookResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getName(), bookResponse.getName()));
            Assertions.assertTrue(found);
        }
    }

    @Test
//    @Disabled
    void testSave() {
        JUnitBookResponse request = new JUnitBookResponse();
        request.setId(1L);
        request.setName("Буратино");

        JUnitBookResponse responseBody = webTestClient.post()
                .uri("/book")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()//isCreated()
                .expectBody(JUnitBookResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());

        Assertions.assertTrue(bookRepository.findById(request.getId()).isPresent());
    }






}
