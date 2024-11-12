package com.example.backend.roomdetails.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Transaction {
    private Timestamp time;         // 거래 시간
    private String accountNumber;   // 계좌 번호
    private String information;     // 거래 정보
    private int amount;             // 거래 금액
}
