package com.example.LibraryManagement.Controller;

import com.example.LibraryManagement.Service.AuthorService;
import com.example.LibraryManagement.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
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
        model.addAttribute("books", bookService.getTop12Book());
        model.addAttribute("author", authorService.findTop12());
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


