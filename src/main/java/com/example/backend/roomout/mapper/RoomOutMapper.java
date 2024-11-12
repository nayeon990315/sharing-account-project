package com.example.backend.roomout.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RoomOutMapper {
    // 1. 사용자 목록에서 삭제
    void deletePerson(Map<String, Object> params);

    // 2. 현재 인원 수 수정
    void updateCurrentMemberNum(Map<String, Object> params);
}
