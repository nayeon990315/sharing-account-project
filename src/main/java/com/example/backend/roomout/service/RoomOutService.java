package com.example.backend.roomout.service;

import java.util.Map;

public interface RoomOutService {
    // 1. 사용자 목록에서 삭제
    void deletePerson(int roomNum, String userId);

    // 2. 현재 인원 수 수정
    void updateCurrentMemberNum(int memberCurrent, int roomNum);
}
