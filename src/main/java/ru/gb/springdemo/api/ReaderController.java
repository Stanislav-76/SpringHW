package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;
import ru.gb.springdemo.service.IssueService;
import ru.gb.springdemo.service.ReaderService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    @Autowired
    private ReaderService service;

    @GetMapping
    private List<Reader> getReaders() {
        return service.getReaders();
    }

    @GetMapping("/{id}")
    private Reader getReader(@PathVariable long id) {
        return service.getReaderById(id);
    }

    @GetMapping("/{id}/issue")
    private List<Issue> getReaderIssues(@PathVariable long id) {
        return service.getReaderIssues(id);
    }

    @PostMapping
    public Reader addReader(@RequestBody Reader reader) {
        return service.addReader(reader);
    }

    @DeleteMapping("/{id}")
    public void deleteReader(@PathVariable long id) {
        service.deleteReader(id);
    }

}
