package com.example.backend.roomend.service;

import java.util.Map;

public interface RoomEndService {
    // 1. 사용 기간 연장 시 기간 정보 수정
    void updatePeriod(int roomNum, int period);

    // 2. 사용 기간 연장 안할 시 해당 방 삭제
    void deleteRoom(int roomNum);
}
