package com.example.LibraryManagement.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowIndexNotifyResponse {
    private Date estimatedReturnDate;
    private long daysRemaining;
    private String bookName;
    private String studentName;
    private String studentEmail;
}
