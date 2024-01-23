package ru.gb.springdemo.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.service.BookService;
import ru.gb.springdemo.service.IssueService;
import ru.gb.springdemo.service.ReaderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ui")
public class UIController {

    private BookService bookService;
    private ReaderService readerService;
    private IssueService issueService;

    public UIController(BookService bookService, ReaderService readerService, IssueService issueService) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.issueService = issueService;
    }

    @GetMapping("/books")
    public String book(Model model) {
        model.addAttribute("books", bookService.getBooks());
        return "book";
    }

    @GetMapping("/reader")
    public String reader(Model model) {
        model.addAttribute("readers", readerService.getReaders());
        return "reader";
    }

    @GetMapping("/reader/{id}")
    public String reader(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("name", readerService.getReaderById(id).getName());
        model.addAttribute("issues", readerService.getReaderIssues(id));
        model.addAttribute("books", bookService.getBooks());
        return "readerIssue";
    }

    @GetMapping("/issues")
    public String issue(Model model) {
        model.addAttribute("issues", issueService.getIssues());
        model.addAttribute("books", bookService.getBooks());
        model.addAttribute("readers", readerService.getReaders());
        return "issue";
    }

}
