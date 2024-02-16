package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.api.IssueRequest;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueService {

  // спринг это все заинжектит
  private final BookRepository bookRepository;
  private final ReaderRepository readerRepository;
  private final IssueRepository issueRepository;
  @Value("${application.issue.max-allowed-books:1}")
  private int maxBooks;

  public Issue issue(IssueRequest request) {
    if (bookRepository.findById(request.getBookId()).get() == null) {
      throw new NoSuchElementException("Не найдена книга с идентификатором \"" + request.getBookId() + "\"");
    }
    if (readerRepository.findById(request.getReaderId()).get() == null) {
      throw new NoSuchElementException("Не найден читатель с идентификатором \"" + request.getReaderId() + "\"");
    }
    // можно проверить, что у читателя нет книг на руках (или его лимит не превышает в Х книг)
    long countBook = issueRepository.findAll().stream()
            .filter(it -> Objects.equals(it.getReaderId(), request.getReaderId()))
            .toList()
            .size();
    if (countBook >= maxBooks) {
      throw  new RuntimeException("Читателю с идентификатором \"" + request.getReaderId() + " уже выдано книг " + countBook + "\"");
    };

    Issue issue = new Issue(request.getBookId(), request.getReaderId());
    issueRepository.save(issue);
    return issue;
  }

  public Optional<Issue> getIssuesById(long id){
    return issueRepository.findById(id);
  }

  public List<Issue> getIssues() {
    return  issueRepository.findAll();
  }

  public Issue returnBook(long issueId) {
    // найти в репозитории выдачу и проставить ей returned_at
    Issue issue = getIssuesById(issueId).get();
    issue.setReturned_at(LocalDateTime.now());
    return issueRepository.save(issue);
  }

}
