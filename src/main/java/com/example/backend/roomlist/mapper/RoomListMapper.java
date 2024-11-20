package com.example.backend.roomlist.mapper;

import com.example.backend.roomlist.vo.RoomListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomListMapper {
    // 5. 구독 카테고리 별 방 목록 출력
    List<RoomListVO> selectRoomListByCategory(String category);

    // 6. 검색 내용이 포함된 방 목록 출력
    List<RoomListVO> selectRoomListByTitle(String keyword);
}
