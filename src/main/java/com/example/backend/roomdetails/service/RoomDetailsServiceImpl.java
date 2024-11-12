package com.example.backend.roomdetails.service;

import com.example.backend.roomdetails.mapper.RoomDetailsMapper;
import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RoomDetailsServiceImpl implements RoomDetailsService {

    @Autowired
    private RoomDetailsMapper roomDetailsMapper;

    //1. 참여자 목록 조회
    @Override
    public List<UserVO> getParticipantsByRoomId(int roomNum) {
        return roomDetailsMapper.getParticipantsByRoomId(roomNum);
    }
    // 2. 회비 제출 날짜 조회
    @Override
    public Date getDueDateByRoomId(int roomNum) {
        return roomDetailsMapper.getDueDateByRoomId(roomNum);
    }
    // 3. 모임통장 거래 내역 조회
    @Override
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return roomDetailsMapper.getTransactionHistory(accountNumber);
    }
    // 4. 구독 계정 정보 조회
    @Override
    public String getSubscriptionAccount(int roomNum) {
        return roomDetailsMapper.getSubscriptionAccount(roomNum);
    }
}
