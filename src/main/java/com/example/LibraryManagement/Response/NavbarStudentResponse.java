package com.example.LibraryManagement.Response;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NavbarStudentResponse {
    public String email;
    public List<BorrowIndexNotifyResponse> borrowIndexResponses;
    public int numberOfBorrowedIndexes;
}