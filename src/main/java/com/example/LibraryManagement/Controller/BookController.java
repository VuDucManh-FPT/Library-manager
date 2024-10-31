package com.example.LibraryManagement.Controller;

import com.example.LibraryManagement.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "/view-book/{bookId}", method = RequestMethod.GET)
    public String detailBook(Model model,
                             @PathVariable("bookId") Integer bookId) {
        model.addAttribute("book", bookService.getById(bookId));
        model.addAttribute("similarBooks", bookService.findBooksInSameGenre(bookId, 9));
        model.addAttribute("trendyBooks", bookService.getTop12Book());
        return "viewBook";
    }
    @GetMapping("/books")
    public String getAllBooks(Model model){
        model.addAttribute("books", bookService.getAll());
        return "booklist";
    }

    @PostMapping("/books")
    public String searchBooks(@RequestParam("bookName") String bookName, Model model){
        model.addAttribute("books", bookService.searchBook(bookName));
        return "booklist";
    }
}
