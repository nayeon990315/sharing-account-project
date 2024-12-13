package com.example.backend.roomdetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String accountNumber;
    private String transactionDetails;
    private String transactionTime;
    private int amount;
}
