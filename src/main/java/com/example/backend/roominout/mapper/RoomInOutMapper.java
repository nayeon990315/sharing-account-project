package com.example.backend.roominout.mapper;

import com.example.backend.roomlist.vo.RoomListVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomInOutMapper {
    // 10. 방 현재 구성원 수 증가
    int increaseCurrntNumber(int roomNum);

    // 11. 사용자 목록에 정보 추가
    void insertPersonList(Map<String, Object> params);

    // 18. 방 현재 구성원 수 감소
    int decreaseCurrntNumber(int roomNum);

    // 19. 사용자 목록에서 삭제
    int deletePersonList(Map<String, Object> params);
}
