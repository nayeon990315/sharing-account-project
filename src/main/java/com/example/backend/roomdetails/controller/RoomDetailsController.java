package com.example.backend.roomdetails.controller;

import com.example.backend.roomdetails.dto.*;
import com.example.backend.roomdetails.service.RoomDetailsService;
import com.example.backend.roomdetails.vo.RoomDetailsVO;
import com.example.backend.user.vo.UserVO;
import com.example.backend.roomdetails.vo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/roomdetails")
public class RoomDetailsController {

    private final RoomDetailsService roomDetailsService;

    @Autowired
    public RoomDetailsController(RoomDetailsService roomDetailsService) {
        this.roomDetailsService = roomDetailsService;
    }
    //1. 방 기본 정보 보여주기
    @GetMapping("/basic-info")
    public RoomDetailsDto getRoomBasicInfo(@RequestParam int roomNum) {
        return roomDetailsService.getRoomBasicInfo(roomNum);
    }
    //참여자 목록 출력
    @GetMapping("/participants")
    public List<ParticipantDto> getParticipantList(@RequestParam int roomNum) {
        return roomDetailsService.getParticipantList(roomNum);
    }
    //구독 계정 정보 출력
    @GetMapping("/subscription-account")
    public SubscriptionAccountDto getSubscriptionAccountInfo(@RequestParam int roomNum) {
        return roomDetailsService.getSubscriptionAccountInfo(roomNum);
    }
    //이번 달 납부 현황
    @GetMapping("/monthly-payment-status")
    public List<PaymentStatusDto> getMonthlyPaymentStatus(@RequestParam int roomNum) {
        return roomDetailsService.getMonthlyPaymentStatus(roomNum);
    }
    //모임통장 거래 내역 보여주기
    @GetMapping("/account-transactions")
    public List<TransactionDto> getAccountTransactionHistory(@RequestParam String accountNumber) {
        return roomDetailsService.getAccountTransactionHistory(accountNumber);
    }
    //이번 달 회비 납부하기 버튼 -> 모임통장 거래내역 추가
    @PostMapping("/add-transaction")
    public void insertTransaction(@RequestParam String accountNumber,
                                  @RequestParam String transactionDetails,
                                  @RequestParam int amount) {
        roomDetailsService.insertTransaction(accountNumber, transactionDetails, amount);
    }
}
