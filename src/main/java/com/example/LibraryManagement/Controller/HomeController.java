package com.example.LibraryManagement.Controller;

import com.example.LibraryManagement.Service.AuthorService;
import com.example.LibraryManagement.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library")
public class HomeController {
    private final BookService bookService;
    private final AuthorService authorService;
    @Autowired
    public HomeController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("books", bookService.getTop5Book());
        model.addAttribute("author", authorService.findTop5());
        return "home";
    }
    @GetMapping("/regulation")
    public String Regulation(Model model) {
        return "regulation";
    }
    @GetMapping("/introduce")
    public String Introduce(Model model) {
        return "introduce";
    }
}


