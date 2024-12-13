package com.example.backend.roominout.service;

import com.example.backend.roomlist.vo.RoomListVO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface RoomInOutService {
    // 10. 방 현재 구성원 수 증가
    void increaseCurrntNumber(int roomNum);

    // 11. 사용자 목록에 정보 추가
    String insertPersonList(Map<String, Object> params);

    // 18. 방 현재 구성원 수 감소
    void decreaseCurrntNumber(int roomNum);

    // 19. 사용자 목록에서 삭제
    String deletePersonList(Map<String, Object> params);
}
