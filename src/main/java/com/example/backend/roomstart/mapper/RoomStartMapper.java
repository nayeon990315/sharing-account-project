package com.example.backend.roomstart.mapper;

import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.roomstart.vo.RoomStartVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomStartMapper {
    // 1.설정 인원 모두 모였는지 확인
    RoomStartVO checkParticipantsCount(int roomNum);
    // 2.보증금 이체 내역 확인
    List<Transaction> getDepositTransactions(String accountNumber);

    // 3.구독 계정 정보 저장
    void updateSubscriptionAccountInfo(RoomStartVO roomStartVO);

}
