package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    public List<Reader> getReaders() {
        return readerRepository.getReaders();
    }

    public Reader getReaderById(long id) {
        return readerRepository.getReaderById(id);
    }

    public List<Issue> getReaderIssues(long id) {
        return issueRepository.getIssues().stream().filter(it -> Objects.equals(it.getReaderId(), id)).toList();
    }

    public Reader addReader(Reader reader) {
        return readerRepository.addReader(reader);
    }

    public void deleteReader(long id) {
        readerRepository.deleteReader(id);
    }

}
