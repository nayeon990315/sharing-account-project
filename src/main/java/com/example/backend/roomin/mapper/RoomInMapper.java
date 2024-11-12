package com.example.backend.roomin.mapper;

import com.example.backend.roomin.vo.Person;
import com.example.backend.roomin.vo.RoomInVO;
import com.example.backend.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomInMapper {
    // 1.빠른매칭 Room 객체 생성해야함
    RoomInVO findFastestAvailableRoom();

    // 2.선택매칭시 개인정보 출력
    UserVO getUserPersonalInfo(int userId);

    // 3.선택매칭시 방 목록 출력
    List<RoomInVO> getRoomListForAi();

    // 4.새로운 방에 입장하면 역할이 팀원으로 설정되어 personList에 추가
    void addUserToRoom(Person person);

}
