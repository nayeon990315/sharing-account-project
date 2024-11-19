package com.example.backend.roomdetails.service;

import com.example.backend.roomdetails.mapper.RoomDetailsMapper;
import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.roomlist.vo.RoomListVO;
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

    // 22. 비밀번호 랜덤 값 생성
    @Override
    public String randomPassword() {
        return roomDetailsMapper.randomPassword();
    }

    // 27. 내가 참여 중인 방 목록 출력
    @Override
    public List<RoomListVO> selectMyRoomList(String id) {
        return roomDetailsMapper.selectMyRoomList(id);
    }
}
