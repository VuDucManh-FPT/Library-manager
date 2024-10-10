package com.example.LibraryManagement.Controller.Staff;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import com.example.LibraryManagement.Service.BorrowIndexService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/staff")
@AllArgsConstructor
public class RentalManagementController {
    private final BorrowIndexRepository borrowIndexRepository;
    private final BorrowIndexService borrowIndexService;

    @GetMapping("rentals")
    public String getAllBorrowIndex(Model model) {
        List<BorrowIndex> borrowIndexList = borrowIndexService.getAllBorrowIndex();
        model.addAttribute("borrowIndexes", borrowIndexList);
        return "Staff/rentals";
    }
}
