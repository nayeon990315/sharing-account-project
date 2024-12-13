package com.example.backend.roombasic.mapper;

import com.example.backend.roombasic.vo.RoomBasicVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RoomBasicMapper {
    // 1. 방 개설 (새로운 방 개설하면 상세 내용 추가) - RoomList
    int insertRoom(RoomBasicVO roomBasicVO);

    // 2. 사용자 추가 (새로운 방 개설하면 개설한 사람의 역할이 방장으로 설정되어 추가) - PersonList & RoomComplete
    public void insertTeamLeader(Map<String, Object> params);
    void insertRoomComplete(int roomNum);
//    public void insertRoomComplete(Map<String, Object> params);

    // 3. 방 세부 내용 변경
    public void updateRoomBasic(RoomBasicVO roomBasicVO);
}
