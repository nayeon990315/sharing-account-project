package com.example.backend.roomdetails.service;

import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.user.vo.UserVO;

import java.util.List;
import java.util.Date;

public interface RoomDetailsService {
    // 1.참여자 목록 조회
    List<UserVO> getParticipantsByRoomId(int roomNum);
    // 2. 회비 제출 날짜 조회
    Date getDueDateByRoomId(int roomNum);
    // 3. 모임통장 거래 내역 조회
    List<Transaction> getTransactionHistory(String accountNumber);
    // 4. 구독 계정 정보 조회
    String getSubscriptionAccount(int roomNum);
}
