package com.example.LibraryManagement.Controller.StudentBorrow;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Service.BorrowIndexService;
import com.example.LibraryManagement.Service.BorrowIndexServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/library")
@AllArgsConstructor
public class LoanController {

    private BorrowIndexService borrowIndexService;
    @GetMapping("/LoanBorrowing")
    public String getCurrentLoans(HttpServletRequest request, Model model) {
        List<BorrowIndex> currentLoans = borrowIndexService.findCurrentBorrowIndex(request);
        model.addAttribute("currentLoans", currentLoans);
        return "/StudentBorrow/LoanBorrowing";
    }
    @GetMapping("/HistoryBorrowing")
    public String getLoanHistory(HttpServletRequest request, Model model) {
        List<BorrowIndex> loanHistory = borrowIndexService.findBorrowIndexHistory(request);
        model.addAttribute("loanHistory", loanHistory);
        return "/StudentBorrow/HistoryBorrowing";
    }

}
