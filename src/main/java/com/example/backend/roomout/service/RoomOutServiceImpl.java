package com.example.backend.roomout.service;

import com.example.backend.roomout.mapper.RoomOutMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RoomOutServiceImpl implements RoomOutService {
    @Autowired
    private RoomOutMapper roomOutMapper;

    // 1. 사용자 목록에서 삭제
    @Override
    public void deletePerson(int roomNum, String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomNum", roomNum);
        params.put("userId", userId);

        roomOutMapper.deletePerson(params);
    }

    // 2. 현재 인원 수 수정
    @Override
    public void updateCurrentMemberNum(int memberCurrent, int roomNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberCurrent", memberCurrent);
        params.put("roomNum", roomNum);

        roomOutMapper.updateCurrentMemberNum(params);
    }
}
