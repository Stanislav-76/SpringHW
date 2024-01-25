package ru.gb.springdemo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    public List<Reader> getReaders() {
        return readerRepository.findAll();
    }

    public Reader getReaderById(long id) {
        return readerRepository.findById(id).get();
    }

    public List<Issue> getReaderIssues(long id) {
        return issueRepository.findAll().stream().filter(it -> Objects.equals(it.getReaderId(), id)).toList();
    }

    public Reader saveReader(Reader reader) {
        return readerRepository.save(reader);
    }

    public void deleteAllReaders() {
        readerRepository.deleteAll();
    }

    public boolean deleteReaderById(Long id) {
        Optional<Reader> readerOptional = readerRepository.findById(id);
        if (readerOptional.isPresent()) {
            readerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @PostConstruct
    public void generateData() {
        readerRepository.saveAll(List.of(
                new ru.gb.springdemo.model.Reader("Игорь"),
                new ru.gb.springdemo.model.Reader("Виктор"),
                new ru.gb.springdemo.model.Reader("Петр")
        ));
    }


}
