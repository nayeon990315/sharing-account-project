package com.example.backend.roominfo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomInfoMapper {
    // 1. 참여 중 통장 목록 출력
    List<String> participationAccount();

    // 2. 참여 중 아닌 통장 목록 출력
    List<String> notParticipationAccount();

    // 3. 시작 전 : 지금까지 모인 인원 수 출력
    List<Map<String, Object>> nowMemberNum();

    // 4. 진행 중 : 회비 납부 여부 체크

    // 5. 완료 : 보증금 이체
    void transfer(Map<String, Object> params);
}
