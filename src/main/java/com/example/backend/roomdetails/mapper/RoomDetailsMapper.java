package com.example.backend.roomdetails.mapper;

import com.example.backend.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import com.example.backend.roomdetails.vo.Transaction;

import java.util.List;
import java.util.Date;

@Mapper
public interface RoomDetailsMapper {
    // 1.참여자 목록(현재 참여중인 사용자 목록 출력
    List<UserVO> getParticipantsByRoomId(int roomNum);

    // 2.회비 제출 날짜 (회비 제출 날짜 출력)
    Date getDueDateByRoomId(int roomNum);

    // 3.모임통장 거래 내역 (방 입장하면 모임통장 거래내역 출력)
    List<Transaction> getTransactionHistory(String accountNumber);
    // 4.구독 계정 정보 (사용자가 모두 모이면 구독 계정 정보 출력)
    String getSubscriptionAccount(int roomNum);
}
