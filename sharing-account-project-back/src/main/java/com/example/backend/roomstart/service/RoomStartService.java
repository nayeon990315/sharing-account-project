package com.example.backend.roomstart.service;

import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.roomstart.vo.RoomStartVO;

import java.util.List;

public interface RoomStartService {

    //설정 인원 모두 모였는지 확인
    RoomStartVO checkParticipantsCount(int roomNum);
    //보증금 이체 내역 확인
    List<Transaction> getDepositTransactions(String accountNumber);
    //구독 계정 정보 저장
    boolean updateSubscriptionAccountInfo(RoomStartVO roomStartVo);
}
