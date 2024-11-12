package com.example.backend.roomin.service;

import com.example.backend.roomin.mapper.RoomInMapper;
import com.example.backend.roomin.vo.Person;
import com.example.backend.roomin.vo.RoomInVO;
import com.example.backend.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomInServiceImpl implements RoomInService {

    @Autowired
    private RoomInMapper roomInMapper;


    @Override
    public RoomInVO findFastestAvailableRoom() {
        return roomInMapper.findFastestAvailableRoom();
    }

    @Override
    public UserVO getUserPersonalInfo(int userId) {
        return roomInMapper.getUserPersonalInfo(userId);
    }

    @Override
    public List<RoomInVO> getRoomListForAi() {
        return roomInMapper.getRoomListForAi();
    }

    @Override
    public boolean addUserToRoom(Person person) {
        try {
            roomInMapper.addUserToRoom(person);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
