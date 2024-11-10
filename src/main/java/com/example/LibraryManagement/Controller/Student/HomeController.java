package com.example.LibraryManagement.Controller.Student;

import com.example.LibraryManagement.Response.NavbarResponse;
import com.example.LibraryManagement.Service.AuthorService;
import com.example.LibraryManagement.Service.BookService;
import com.example.LibraryManagement.Service.ServiceImpl;
import com.example.LibraryManagement.Service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/library")
public class HomeController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final ServiceImpl serviceImpl;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        NavbarResponse navbarData = serviceImpl.getNavbarStudent(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("books", bookService.getTop12Book());
        model.addAttribute("author", authorService.findTop12());
        return "home";
    }
    @GetMapping("/regulation")
    public String Regulation(Model model, HttpServletRequest request) {
        NavbarResponse navbarData = serviceImpl.getNavbarStudent(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        return "regulation";
    }
    @GetMapping("/introduce")
    public String Introduce(Model model, HttpServletRequest request) {
        NavbarResponse navbarData = serviceImpl.getNavbarStudent(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        return "introduce";
    }

}


