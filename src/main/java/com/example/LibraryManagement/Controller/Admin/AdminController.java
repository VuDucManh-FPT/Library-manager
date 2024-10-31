package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Service.*;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
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

    @GetMapping({"/add-book", "/edit-book/{id}"})
    public String showBookForm(@PathVariable(value = "id", required = false) Integer id,
                               Model model) {
        Book book = (id != null) ? bookService.getById(id) : new Book();
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("publishers", publisherService.getAllPublishers());
        model.addAttribute("book", book);
        if (id == null){
            model.addAttribute("formAction", "/admin/add-book");
        }else {
            model.addAttribute("formAction", "/admin/edit-book");
        }
        return "addBook";
    }

    @PostMapping({"/add-book", "/edit-book"})
    public String addBook(@ModelAttribute Book book , @RequestParam("bookImagesRaw") List<MultipartFile> images){
        bookService.addNew(book, images);
        return "redirect:/admin/manageBooks";
    }

    @GetMapping("/manageBooks")
    public String manageBook(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "manageBook";
    }

    @GetMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable(value = "id", required = false) Integer id){
        bookService.delete(id);
        return "redirect:/admin/manageBooks";
    }
}
