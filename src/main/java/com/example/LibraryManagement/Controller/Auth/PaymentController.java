package com.example.LibraryManagement.Controller.Auth;

import com.example.LibraryManagement.Model.BorrowFine;
import com.example.LibraryManagement.Repository.BorrowFineRepository;
import com.example.LibraryManagement.Response.ResponseObject;
import com.example.LibraryManagement.Response.VNPayResponse;
import com.example.LibraryManagement.Service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

@Controller
@RequestMapping("${spring.application.api-prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final BorrowFineRepository borrowFineRepository;
    @GetMapping("/vn-pay")
    public ResponseObject<VNPayResponse> pay(HttpServletRequest request, int amount, Long borrowFine) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request, amount, borrowFine));
    }
    @GetMapping("/vn-pay-callback")
    public String payCallbackHandler(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String status = request.getParameter("vnp_ResponseCode");
        String transactionId = request.getParameter("vnp_TxnRef");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String borrowFineIdParam = null;
        if (orderInfo != null && orderInfo.contains("ID: ")) {
            borrowFineIdParam = orderInfo.substring(orderInfo.indexOf("ID: ") + 4);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isStaff = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("STAFF"));
        boolean isStudent = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("STUDENT"));

        if (status != null && status.equals("00")) {  // Kiểm tra trạng thái thanh toán thành công
            Optional<BorrowFine> fineOpt = borrowFineRepository.findById(Integer.parseInt(borrowFineIdParam));
            if (fineOpt.isPresent()) {
                BorrowFine fine = fineOpt.get();
                fine.setStatus("Paid");
                borrowFineRepository.save(fine);
            }
            if (isStaff) {
                redirectAttributes.addFlashAttribute("success", "Fine marked as Paid successfully!");
                return "redirect:/staff/rentals";
            }
            if (isStudent) {
                redirectAttributes.addFlashAttribute("success", "Fine marked as Paid successfully!");
                return "redirect:/library/HistoryBorrowing";
            }
//            return new ResponseObject<>(HttpStatus.OK, "Payment Successful, Fine status updated to Paid", new VNPayResponse("00", "Success", ""));
        } else {
            // Nếu thanh toán thất bại
//            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Payment failed", null);
            if (isStaff) {
                redirectAttributes.addFlashAttribute("error", "Payment failed!");
                return "redirect:/staff/rentals";
            }
            if (isStudent) {
                redirectAttributes.addFlashAttribute("error", "Payment failed!");
                return "redirect:/library/HistoryBorrowing";
            }
        }
        return "redirect:/library/login";
    }


}