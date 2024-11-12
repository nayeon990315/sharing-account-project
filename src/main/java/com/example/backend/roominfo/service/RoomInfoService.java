package com.example.backend.roominfo.service;

import java.util.List;
import java.util.Map;

public interface RoomInfoService {
    // 1. 참여 중 통장 목록 출력
    List<String> participationAccount();

    // 2. 참여 중 아닌 통장 목록 출력
    List<String> notParticipationAccount();

    // 3. 시작 전 : 지금까지 모인 인원 수 출력
    List<Map<String, Object>> nowMemberNum();

    // 4. 진행 중 : 회비 납부 여부 체크

    // 5. 완료 : 보증금 이체
    void transfer(String accountNumber, String userName, Integer price);
}
