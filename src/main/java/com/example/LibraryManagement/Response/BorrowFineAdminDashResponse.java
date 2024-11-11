package com.example.LibraryManagement.Response;

import com.example.LibraryManagement.Model.BorrowFine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowFineAdminDashResponse {
    private BorrowFine borrowFine;
    private double lostFine;
    private double damageFine;
    private double lateFine;
}
