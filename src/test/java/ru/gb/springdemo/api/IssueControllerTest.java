package ru.gb.springdemo.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.HttpClientErrorException;
import ru.gb.springdemo.JUnitSpringBootBase;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;
import ru.gb.springdemo.service.IssueService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class IssueControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;


    @Data
    static class JUnitIssueResponse {
        private Long id;
        private Long bookId;
        private Long readerId;
        private LocalDateTime issued_at;
        private LocalDateTime returned_at;
    }


    @Test
    void testFindByIdSuccess() {
        // подготовил данные
        Issue expected = issueRepository.save(new Issue(1L, 2L)); // ! в БД h2 значение времени округляется

        JUnitIssueResponse responseBody = webTestClient.get()
                .uri("/issue/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getBookId(), responseBody.getBookId());
        Assertions.assertEquals(expected.getReaderId(), responseBody.getReaderId());
//        Assertions.assertEquals(expected.getIssued_at(), responseBody.getIssued_at());
        Assertions.assertEquals(expected.getReturned_at(), responseBody.getReturned_at());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from issues", Long.class);

        JUnitIssueResponse responseBody = webTestClient.get()
                .uri("/issue/" + maxId + 1)
                .exchange()
                .expectStatus().isOk() // Actual   :200 OK
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNull(responseBody);
    }

    @Test
    void testReturnBook() {
        Issue expected = issueRepository.save(new Issue(1L, 2L));

        JUnitIssueResponse responseBody = webTestClient.put()
                .uri("/issue/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getBookId(), responseBody.getBookId());
        Assertions.assertEquals(expected.getReaderId(), responseBody.getReaderId());
        Assertions.assertNotNull(responseBody.getReturned_at());
    }

    @Test
    void testAddIssue() {
        bookRepository.save(new Book("Ваня"));
        readerRepository.save(new Reader("Саня"));
        issueRepository.save(new Issue(1L, 1l));
        IssueRequest request = new IssueRequest();
        request.setBookId(1L);
        request.setReaderId(1L);

        // Проверка создания выдачи
        JUnitIssueResponse responseBody = webTestClient.post()
                .uri("/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());

        // Проверка превышкния лимита выдачи
        responseBody = webTestClient.post()
                .uri("/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();

        request.setReaderId(readerRepository.findAll().size() + 1);

        // Проверка на запрос при отсутствие читателя с запрашиваемым Id в базе
        responseBody = webTestClient.post()
                .uri("/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();

    }


//    @Test        // нет ручки в контроллере
//    void testGetAll() {
//        // подготовил данные
//        issueRepository.saveAll(List.of(
//                new Issue(1L,2L),
//                new Issue(2L, 1L)
//        ));
//
//        List<Issue> expected = issueRepository.findAll();
//        System.out.println(expected);
//
//        List<JUnitIssueResponse> responseBody = webTestClient.get()
//                .uri("/issue")
//                // .retrieve
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(new ParameterizedTypeReference<List<JUnitIssueResponse>>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        Assertions.assertEquals(expected.size(), responseBody.size());
//        for (JUnitIssueResponse issueResponse : responseBody) {
//            boolean found = expected.stream()
//                    .filter(it -> Objects.equals(it.getId(), issueResponse.getId()))
//                    .anyMatch(it -> Objects.equals(it.getBookId(), issueResponse.getBookId())
//                            && Objects.equals(it.getReaderId(), issueResponse.getReaderId())
////                            && Objects.equals(it.getIssued_at(), issueResponse.getIssued_at())
//                            && Objects.equals(it.getReturned_at(), issueResponse.getReturned_at()));
//            Assertions.assertTrue(found);
//        }
//    }

    @Test
    void testSave() {
        JUnitIssueResponse request = new JUnitIssueResponse();
        request.setId(1L);
        request.setBookId(1L);
        request.setReaderId(2L);

        JUnitIssueResponse responseBody = webTestClient.post()
                .uri("/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitIssueResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());

        Assertions.assertTrue(issueRepository.findById(request.getId()).isPresent());
    }
}
