package com.example.backend.roominout.service;

import com.example.backend.exception.CustomNotFoundException;
import com.example.backend.roominout.mapper.RoomInOutMapper;
import com.example.backend.roomlist.mapper.RoomListMapper;
import com.example.backend.roomlist.service.RoomListService;
import com.example.backend.roomlist.vo.RoomListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomInOutServiceImpl implements RoomInOutService {
    @Autowired
    private RoomInOutMapper roomInOutMapper;

    // 10. 방 현재 구성원 수 증가
    @Override
    public void increaseCurrntNumber(int roomNum) {
        int result = roomInOutMapper.increaseCurrntNumber(roomNum);

        if (result == 0) {
            throw new CustomNotFoundException("RoomNum " + roomNum + " not found.");
        }
    }

    // 11. 사용자 목록에 정보 추가
    @Override
    public String insertPersonList(Map<String, Object> params) {
        try {
            roomInOutMapper.insertPersonList(params);
            return "Person added successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to add person");
        }
    }

    // 18. 방 현재 구성원 수 감소
    @Override
    public void decreaseCurrntNumber(int roomNum) {
        int result = roomInOutMapper.decreaseCurrntNumber(roomNum);

        if (result == 0) {
            throw new CustomNotFoundException("RoomNum " + roomNum + " not found.");
        }
    }

    // 19. 사용자 목록에서 삭제
    @Override
    public String deletePersonList(Map<String, Object> params) {
        int result = roomInOutMapper.deletePersonList(params);

        if (result == 0) {
            throw new CustomNotFoundException("Person not found in the room.");
        }

        return "Person deleted successfully";
    }
}
