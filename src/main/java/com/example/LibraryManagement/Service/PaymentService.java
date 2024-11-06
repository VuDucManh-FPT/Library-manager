package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Config.VNPAYConfig;
import com.example.LibraryManagement.Response.VNPayResponse;
import com.example.LibraryManagement.Utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPAYConfig vnPayConfig;
    public VNPayResponse createVnPayPayment(HttpServletRequest request, int fineValue, Long borrowFineId) {
        long amount = fineValue * 100L;  // Tính giá trị bằng cách nhân với 100 (giả sử VNPay yêu cầu giá trị này)
        String bankCode = request.getParameter("bankCode");

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));  // Sử dụng giá trị `amount` đã tính toán
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toán khoản phạt. ID: " + borrowFineId);  // Đưa ID vào nội dung đơn hàng

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }

        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        // Build query URL
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }

}
