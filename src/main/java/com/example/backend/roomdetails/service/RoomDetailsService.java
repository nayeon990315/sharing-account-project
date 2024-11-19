package com.example.backend.roomdetails.service;

import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.roomlist.vo.RoomListVO;
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

    // 22. 비밀번호 랜덤 값 생성
    String randomPassword();

    // 27. 내가 참여 중인 방 목록 출력
    List<RoomListVO> selectMyRoomList(String id);
}
