package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.ReaderService;

import java.util.List;
import java.util.Optional;

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
        return service.saveReader(reader);
    }

    @DeleteMapping
    public String deleteAllReaders() {
        service.deleteAllReaders();
        return "All books have been deleted successfully.";
    }

    @DeleteMapping("/{id}")
    public String deleteReaderById(@PathVariable long id) {
        if (service.deleteReaderById(id)) {
            return "Reader with ID " + id + " has been deleted successfully.";
        } else {
            return "Reader with ID " + id + " not found.";
        }
    }


}
