package com.example.LibraryManagement.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteRentalRequest {
    private int staffID;
    private int studentID;
    private int bookID;
    private int conditionAfterID;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estimateDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;
}
