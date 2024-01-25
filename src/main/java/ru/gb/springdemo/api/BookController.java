package ru.gb.springdemo.api;

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
    private List<Book> getBooks() {
        return service.getBooks();
    }

    @GetMapping("/{id}")
    private Book getBook(@PathVariable long id) {
        return service.getBookById(id);
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
         return service.saveBook(book);
    }

    @DeleteMapping
    public String deleteAllBooks() {
        service.deleteAllBooks();
        return "All books have been deleted successfully.";
    }

    @DeleteMapping("/{id}")
    public String deleteBookById(@PathVariable long id) {
        if (service.deleteBookById(id)) {
            return "Book with ID " + id + " has been deleted successfully.";
        } else {
            return "Book with ID " + id + " not found.";
        }
    }

}
