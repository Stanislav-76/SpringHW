package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.service.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {

    private BookService service;

    public BookController(BookService bookService) {
        this.service = bookService;
    }

    @GetMapping
    @Operation(summary = "get all books", description = "Загружает все книги")
    private List<Book> getBooks() {
        return service.getBooks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get book by id", description = "Загружает книгу по id")
    private Book getBook(@PathVariable long id) {
        return service.getBookById(id);
    }

    @PostMapping
    @Operation(summary = "add book", description = "Добавляет книгу")
    public Book addBook(@RequestBody Book book) {
         return service.saveBook(book);
    }

    @DeleteMapping
    @Operation(summary = "delete all books", description = "Удаляет все книги")
    public String deleteAllBooks() {
        service.deleteAllBooks();
        return "All books have been deleted successfully.";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete book by id", description = "Удаляет книгу по id")
    public String deleteBookById(@PathVariable long id) {
        if (service.deleteBookById(id)) {
            return "Book with ID " + id + " has been deleted successfully.";
        } else {
            return "Book with ID " + id + " not found.";
        }
    }

}
