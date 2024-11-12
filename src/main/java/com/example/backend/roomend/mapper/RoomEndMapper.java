package com.example.backend.roomend.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RoomEndMapper {
    // 1. 사용 기간 연장 시 기간 정보 수정
    void updatePeriod(Map<String, Object> params);

    // 2. 사용 기간 연장 안할 시 해당 방 삭제
    void deleteRoom(int roomNum);
}
