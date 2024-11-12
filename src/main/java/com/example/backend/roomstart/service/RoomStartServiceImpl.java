package com.example.backend.roomstart.service;

import com.example.backend.roomdetails.vo.Transaction;
import com.example.backend.roomstart.mapper.RoomStartMapper;
import com.example.backend.roomstart.vo.RoomStartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomStartServiceImpl implements RoomStartService {

    @Autowired
    private RoomStartMapper roomStartMapper;

    //설정 인원 모두 모였는지 확인
    @Override
    public RoomStartVO checkParticipantsCount(int roomNum) {
        return roomStartMapper.checkParticipantsCount(roomNum);
    }
    //보증금 이체 내역 확인
    @Override
    public List<Transaction> getDepositTransactions(String accountNumber) {
        return roomStartMapper.getDepositTransactions(accountNumber);
    }
    //구독 계정 정보 저장
    @Override
    public boolean updateSubscriptionAccountInfo(RoomStartVO roomStartVo) {
        try {
            roomStartMapper.updateSubscriptionAccountInfo(roomStartVo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
