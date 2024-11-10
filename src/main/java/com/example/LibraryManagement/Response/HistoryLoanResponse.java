package com.example.LibraryManagement.Response;

import com.example.LibraryManagement.Model.BorrowIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryLoanResponse {
    private BorrowIndex borrowIndex;
    private String paymentUrl;
}
