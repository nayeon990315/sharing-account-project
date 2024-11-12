package com.example.backend.roomend.service;

import com.example.backend.roomend.mapper.RoomEndMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
public class RoomEndServiceImpl implements RoomEndService {
    @Autowired
    private RoomEndMapper roomEndMapper;

    // 1. 사용 기간 연장 시 기간 정보 수정
    @Override
    public void updatePeriod(int roomNum, int period) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomNum", roomNum);
        params.put("period", period);

        roomEndMapper.updatePeriod(params);
    }

    // 2. 사용 기간 연장 안할 시 해당 방 삭제
    @Override
    public void deleteRoom(int roomNum) {
        roomEndMapper.deleteRoom(roomNum);
    }
}
