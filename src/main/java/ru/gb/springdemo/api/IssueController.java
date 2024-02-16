package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.service.IssueService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/issue")
public class IssueController {

  @Autowired
  private IssueService service;

  @PostMapping
  @Operation(summary = "add issue", description = "Добавляет выдачу читателю книги")
  public ResponseEntity<Issue> issueBook(@RequestBody IssueRequest request) {
    log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", request.getReaderId(), request.getBookId());

    final Issue issue;
    try {
      issue = service.issue(request);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build(); //ResponseEntity.status(409).build()
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(issue);
  }

  @PutMapping("/{issueId}")
  @Operation(summary = "set time for return book in issue", description = "Проставляет время возврата книги в выдаче")
  public Issue returnBook(@PathVariable long issueId) {
    // найти в репозитории выдачу и проставить ей returned_at
    return service.returnBook(issueId);
  }

  @GetMapping("/{id}")
  @Operation(summary = "get issue by id", description = "Загружает выдачу по id")
  public Optional<Issue> getIssue(@PathVariable long id){
    return service.getIssuesById(id);
  }

}
