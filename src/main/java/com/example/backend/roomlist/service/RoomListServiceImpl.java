package com.example.backend.roomlist.service;

import com.example.backend.roomlist.mapper.RoomListMapper;
import com.example.backend.roomlist.vo.RoomListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomListServiceImpl implements RoomListService {
    @Autowired
    private RoomListMapper roomListMapper;

    // 5. 구독 카테고리 별 방 목록 출력
    @Override
    public List<RoomListVO> selectRoomListByCategory(String category) {
        return roomListMapper.selectRoomListByCategory(category);
    }

    // 6. 검색 내용이 포함된 방 목록 출력
    @Override
    public List<RoomListVO> selectRoomListByTitle(String keyword) {
        return roomListMapper.selectRoomListByTitle(keyword);
    }
}
