package com.example.backend.roominfo.service;

import com.example.backend.roominfo.mapper.RoomInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    private RoomInfoMapper roomInfoMapper;

    // 1. 참여 중 통장 목록 출력
    @Override
    public List<String> participationAccount() {
        return roomInfoMapper.participationAccount();
    }

    // 2. 참여 중 아닌 통장 목록 출력
    @Override
    public List<String> notParticipationAccount() {
        return roomInfoMapper.notParticipationAccount();
    }

    // 3. 시작 전 : 지금까지 모인 인원 수 출력
    @Override
    public List<Map<String, Object>> nowMemberNum() {
        return roomInfoMapper.nowMemberNum();
    }

    // 4. 진행 중 : 회비 납부 여부 체크

    // 5. 완료 : 보증금 이체
    @Override
    public void transfer(String accountNumber, String userName, Integer price) {
    }
}
