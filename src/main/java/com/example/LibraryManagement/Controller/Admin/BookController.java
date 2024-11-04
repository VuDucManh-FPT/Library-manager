package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Repository.BookRepository;
import com.example.LibraryManagement.Service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class BookController {
    private BookService bookService;
    private BookRepository bookRepository;
    @GetMapping("books")
    public String books(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "Admin/books";
    }
}
