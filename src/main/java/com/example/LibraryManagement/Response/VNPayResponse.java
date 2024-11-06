package com.example.LibraryManagement.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class VNPayResponse {
    public String code;
    public String message;
    public String paymentUrl;
}

