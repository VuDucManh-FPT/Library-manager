package com.example.LibraryManagement.Controller;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Service.*;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class AdminController {
    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    @Autowired
    public AdminController(BookService bookService, GenreService genreService, AuthorService authorService, PublisherService publisherService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    @GetMapping("/addBooks")
    public String addBookForm(Model model) {
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book , @RequestParam("bookImagesRaw") List<MultipartFile> images){
        bookService.addNew(book, images);
        return "redirect:/addBooks";
    }

    @GetMapping("/manageBooks")
    public String manageBook(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "manageBook";
    }
}
