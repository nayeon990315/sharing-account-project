package com.example.backend.roomdetails.controller;

import com.example.backend.exception.CustomNotFoundException;
import com.example.backend.roomdetails.service.RoomDetailsService;
import com.example.backend.roomlist.vo.RoomListVO;
import com.example.backend.user.vo.UserVO;
import com.example.backend.roomdetails.vo.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/roomdetails")
public class RoomDetailsController {
    @Autowired
    private RoomDetailsService roomDetailsService;
    // 1.참여자 목록 조회
    @GetMapping("/participants/{roomNum}")
    public ResponseEntity<List<UserVO>> getParticipantsByRoomId(@PathVariable int roomNum) {
        List<UserVO> participants = roomDetailsService.getParticipantsByRoomId(roomNum);
        return ResponseEntity.ok(participants);
    }
    // 2.회비 제출 날짜 조회
    @GetMapping("/duedate/{roomNum}")
    public ResponseEntity<Date> getDueDateByRoomId(@PathVariable int roomNum) {
        Date dueDate = roomDetailsService.getDueDateByRoomId(roomNum);
        return ResponseEntity.ok(dueDate); //상태코드 200 응답객체 생성
    }
    // 3.모임통장 거래 내역 조회
    @GetMapping("/transaction-history/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountNumber) {
        List<Transaction> transactions = roomDetailsService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }
    // 4.구독 계정 정보 조회
    @GetMapping("/subscription/{roomNum}")
    public ResponseEntity<String> getSubscriptionAccount(@PathVariable int roomNum) {
        String subscriptionAccount = roomDetailsService.getSubscriptionAccount(roomNum);
        return ResponseEntity.ok(subscriptionAccount);
    }

    // 22. 비밀번호 랜덤 값 생성
    @GetMapping("/random/password")
    public ResponseEntity<String> randomPassword() {
        try {
            String password = roomDetailsService.randomPassword();
            return ResponseEntity.ok(password);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate random password.");
        }
    }

    // 27. 내가 참여 중인 방 목록 출력
    @GetMapping("/myrooms")
    public ResponseEntity<List<RoomListVO>> selectMyRoomList(@RequestParam String id) {
        try {
            List<RoomListVO> roomList = roomDetailsService.selectMyRoomList(id);
            return ResponseEntity.ok(roomList);
        } catch (CustomNotFoundException e) {
            log.info("404 Error : Roomlist not found by {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            log.info("500 Error : An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
