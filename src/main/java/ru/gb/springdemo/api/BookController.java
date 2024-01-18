package ru.gb.springdemo.api;

import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private BookService service;

    public BookController(BookService bookService) {
        this.service = bookService;
    }

    @GetMapping
    private List<Book> getBooks() {
        return service.getBooks();
    }

    @GetMapping("/{id}")
    private Book getBook(@PathVariable long id) {
        return service.getBookById(id);
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
         return service.addBook(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        service.deleteBook(id);
    }
}
