package ru.gb.springdemo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(long id) {
        return bookRepository.findById(id).get();
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }

    public boolean deleteBookById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @PostConstruct
    public void generateData() {
        bookRepository.saveAll(List.of(
                new ru.gb.springdemo.model.Book("Бородино"),
                new ru.gb.springdemo.model.Book("Мертвые души"),
                new ru.gb.springdemo.model.Book("Гарри Поттер")
        ));
    }


}
