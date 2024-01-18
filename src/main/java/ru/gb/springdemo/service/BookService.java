package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    public List<Book> getBooks() {
        return bookRepository.getBooks();
    }

    public Book getBookById(long id) {
        return bookRepository.getBookById(id);
    }

    public Book addBook(Book book) {
        return bookRepository.addBook(book);
    }

    public void deleteBook(long id) {
        bookRepository.deleteBook(id);
    }

}
