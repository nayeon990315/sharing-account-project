package com.example.backend.roomlist.service;

import com.example.backend.roomlist.vo.RoomListVO;

import java.util.List;
import java.util.Map;

public interface RoomListService {
    // 5. 구독 카테고리 별 방 목록 출력
    List<RoomListVO> selectRoomListByCategory(String category);

    // 6. 검색 내용이 포함된 방 목록 출력
    List<RoomListVO> selectRoomListByTitle(String keyword);
}
