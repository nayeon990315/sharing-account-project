package com.example.backend.roomstart.controller;

import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.roomstart.service.RoomStartService;
import com.example.backend.roomstart.vo.RoomStartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RoomStartController {

    @Autowired
    private RoomStartService roomStartService;

    // 1. 설정 인원 모두 모였는지 확인
    @GetMapping("/check-participants/{roomNum}")
    public ResponseEntity<RoomStartVO> checkParticipantsCount(@PathVariable int roomNum) {
        RoomStartVO roomStartVo = roomStartService.checkParticipantsCount(roomNum);
        return ResponseEntity.ok(roomStartVo);
    }

    // 2. 보증금 이체 내역 확인
    @GetMapping("/deposit-transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> getDepositTransactions(@PathVariable String accountNumber) {
        List<Transaction> depositTransactions = roomStartService.getDepositTransactions(accountNumber);
        return ResponseEntity.ok(depositTransactions);
    }
    // 3. 구독 계정 정보 저장
    @PostMapping("/subscription")
    public ResponseEntity<String> updateSubscriptionAccountInfo(@RequestBody RoomStartVO roomStartVo) {
        boolean isUpdated = roomStartService.updateSubscriptionAccountInfo(roomStartVo);
        if (isUpdated) {
            return ResponseEntity.ok("Subscription account information updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update subscription account information.");
        }
    }
}
