package com.example.LibraryManagement.Controller.Fragment;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Request.ForgotPassRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
                    Date currentDate = new Date();
                    long timeDifference = currentDate.getTime()-estimatedDate.getTime();
                    long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifference);
                    if(currentDate.after(estimatedDate)){
                        daysDifference = -daysDifference;
                    }
                    return BorrowIndexNotifyResponse.builder()
                            .estimatedReturnDate(estimatedDate)
                            .daysRemaining(daysDifference)
                            .bookName(borrowIndex.getBook().getBookName())
                            .studentName(borrowIndex.getStudent().getStudentName())
                            .studentEmail(borrowIndex.getStudent().getStudentEmail())
                            .build();
                })
                .collect(Collectors.toList());
        int numberOfBorrowedIndexes = borrowIndexResponses.size();
        model.addAttribute("email", email);
        model.addAttribute("borrowIndexResponses", borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", numberOfBorrowedIndexes);
        return "fragments/navbarAdmin";
    }
    @GetMapping("/notify/sendMailNotification")
    public String sendMailNotification(@RequestParam("studentEmail") String studentEmail,
                                       @RequestParam("bookName") String bookName,
                                       @RequestParam("daysRemaining") int daysRemaining,
                                       RedirectAttributes redirectAttributes) {
        ForgotPassRequest forgotPassRequest = new ForgotPassRequest();
        forgotPassRequest.setEmail(studentEmail);
        String subject = "Borrowing Deadline Notification";
        String message = "Dear student,\n\n"
                + "This is a reminder that the book '" + bookName + "' is due for return."
                + "\nYou have " + daysRemaining + " day(s) remaining.";

        if (daysRemaining <= 0) {
            message = "Dear student,\n\nYou have exceeded the return deadline for the book '" + bookName + "'. Please return it as soon as possible.";
        }

        try {
            serviceImpl.sendMail(forgotPassRequest, subject, message);
            redirectAttributes.addFlashAttribute("success", "Notification email sent successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to send notification email.");
        }

        return "redirect:/admin/staffs";
    }
}
