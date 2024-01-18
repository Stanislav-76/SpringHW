package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.gb.springdemo.api.IssueRequest;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.NoSuchElementException;
import java.util.Objects;

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
    if (bookRepository.getBookById(request.getBookId()) == null) {
      throw new NoSuchElementException("Не найдена книга с идентификатором \"" + request.getBookId() + "\"");
    }
    if (readerRepository.getReaderById(request.getReaderId()) == null) {
      throw new NoSuchElementException("Не найден читатель с идентификатором \"" + request.getReaderId() + "\"");
    }
    // можно проверить, что у читателя нет книг на руках (или его лимит не превышает в Х книг)
    long countBook = issueRepository.getIssues().stream()
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

  public Issue getIssuesById(long id){
    return issueRepository.getIssuesById(id);
  }

  public void returnBook(long issueId) {
    // найти в репозитории выдачу и проставить ей returned_at
    issueRepository.returnBook(issueId);
  }



}
