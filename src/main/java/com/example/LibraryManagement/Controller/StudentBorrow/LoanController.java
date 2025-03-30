package com.example.LibraryManagement.Controller.StudentBorrow;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.BorrowFine;
import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Repository.BorrowFineRepository;
import com.example.LibraryManagement.Response.HistoryLoanResponse;
import com.example.LibraryManagement.Response.NavbarResponse;
import com.example.LibraryManagement.Response.VNPayResponse;
import com.example.LibraryManagement.Service.BorrowIndexService;
import com.example.LibraryManagement.Service.BorrowIndexServiceImpl;
import com.example.LibraryManagement.Service.PaymentService;
import com.example.LibraryManagement.Service.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/library")
@AllArgsConstructor
public class LoanController {

    private final BorrowIndexService borrowIndexService;
    private final BorrowFineRepository borrowFineRepository;
    private final PaymentService paymentService;
    private final ServiceImpl serviceImpl;
    @GetMapping("/LoanBorrowing")
    public String getCurrentLoans(HttpServletRequest request, Model model) {
        List<BorrowIndex> currentLoans = borrowIndexService.findCurrentBorrowIndex(request);
        NavbarResponse navbarData = serviceImpl.getNavbarStudent(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("currentLoans", currentLoans);
        return "/StudentBorrow/LoanBorrowing";
    }
    @GetMapping("/HistoryBorrowing")
    public String getLoanHistory(HttpServletRequest request, Model model) {
        List<BorrowIndex> borrowIndices = borrowIndexService.findBorrowIndexHistory(request);
        List<HistoryLoanResponse> loanHistory = new ArrayList<>();

        borrowIndices.forEach(borrowIndex -> {
            BorrowFine borrowFine = borrowFineRepository.getBorrowFineByBorrowIndex(borrowIndex);

            if (borrowFine != null) {
                borrowIndex.setFines(List.of(borrowFine));
            }

            Book book = borrowIndex.getBook();
            if (book != null) {
                if (book.getBookImages() != null && !book.getBookImages().isEmpty()) {
                    book.setFirstImageName(book.getBookImages().get(0).getImageURL());
                } else {
                    book.setFirstImageName("nullI.png");
                }
            }
            String paymentUrl = null;
            if (borrowFine != null && "Unpaid".equals(borrowFine.getStatus())) {
                VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request, (int) borrowFine.getValue(), (long) borrowFine.getBorrowFineId());
                paymentUrl = vnPayResponse.getPaymentUrl();
            }

            loanHistory.add(new HistoryLoanResponse(borrowIndex, paymentUrl));
        });
        NavbarResponse navbarData = serviceImpl.getNavbarStudent(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("loanHistory", loanHistory);
        return "/StudentBorrow/HistoryBorrowing";
    }

}
