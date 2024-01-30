package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "get all readers", description = "Загружает всех читателей")
    private List<Reader> getReaders() {
        return service.getReaders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get reader by id", description = "Загружает читателя по id")
    private Reader getReader(@PathVariable long id) {
        return service.getReaderById(id);
    }

    @GetMapping("/{id}/issue")
    @Operation(summary = "get issues for reader by id", description = "Загружет все выдачи читателя по id")
    private List<Issue> getReaderIssues(@PathVariable long id) {
        return service.getReaderIssues(id);
    }

    @PostMapping
    @Operation(summary = "add reader", description = "Добавляет читателя")
    public Reader addReader(@RequestBody Reader reader) {
        return service.saveReader(reader);
    }

    @DeleteMapping
    @Operation(summary = "delete all readers", description = "Удаляет всех читателей")
    public String deleteAllReaders() {
        service.deleteAllReaders();
        return "All books have been deleted successfully.";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete reader by id", description = "Удаляет читателя по id")
    public String deleteReaderById(@PathVariable long id) {
        if (service.deleteReaderById(id)) {
            return "Reader with ID " + id + " has been deleted successfully.";
        } else {
            return "Reader with ID " + id + " not found.";
        }
    }


}
