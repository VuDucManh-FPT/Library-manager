package com.example.LibraryManagement.Controller.Fragment;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Response.BorrowIndexNotifyResponse;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Service.BorrowIndexService;
import com.example.LibraryManagement.Service.Service;
import com.example.LibraryManagement.Service.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class NavbarController {
    private final ServiceImpl serviceImpl;
    private final Service service;
    private final JwtProvider jwtProvider;
    private final BorrowIndexService borrowIndexService;
    @GetMapping("/admin/navbar")
    public String getNavbarAdmin(Model model, HttpServletRequest request) {
        String token = jwtProvider.getJwtFromCookies(request);
        String email = jwtProvider.getEmail(token);
        List<BorrowIndex> borrowIndices = borrowIndexService.findBorrowIndexNearEstimateTime();
        List<BorrowIndexNotifyResponse> borrowIndexResponses = borrowIndices.stream()
                .map(borrowIndex -> {
                    Date estimatedDate = borrowIndex.getEstimateDate();
                    LocalDate currentDate = LocalDate.now();
                    long daysDifference = ChronoUnit.DAYS.between((Temporal) currentDate, (Temporal) estimatedDate);

                    String statusColor = "black";
                    if (daysDifference == 1) {
                        statusColor = "yellow";
                    } else if (daysDifference < 0) {
                        statusColor = "red";
                    }

                    return BorrowIndexNotifyResponse.builder()
                            .estimatedReturnDate(estimatedDate)
                            .statusColor(statusColor)
                            .daysRemaining(Math.abs(daysDifference))
                            .bookName(borrowIndex.getBook().getBookName())
                            .studentName(borrowIndex.getStudent().getStudentName())
                            .build();
                })
                .collect(Collectors.toList());
        int numberOfBorrowedIndexes = borrowIndexResponses.size();
        model.addAttribute("email", email);
        model.addAttribute("borrowIndexResponses", borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", numberOfBorrowedIndexes);
        return "fragments/navbarAdmin";
    }
}
