package ru.gb.springdemo.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.service.IssueService;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/issue")
public class IssueController {

  @Autowired
  private IssueService service;

  @PostMapping
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
  public void returnBook(@PathVariable long issueId) {
    // найти в репозитории выдачу и проставить ей returned_at
    service.returnBook(issueId);
  }

  @GetMapping("/{id}")
  public Issue getIssue(@PathVariable long id){
    return service.getIssuesById(id);
  }

}
