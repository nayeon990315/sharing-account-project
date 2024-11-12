package com.example.backend.roomin.service;

import com.example.backend.roomin.vo.Person;
import com.example.backend.roomin.vo.RoomInVO;
import com.example.backend.user.vo.UserVO;

import java.util.List;

public interface RoomInService {

    // 1.빠른 매칭 (RoomList 중 가장 빨리 입장 가능한 방 확인)
    RoomInVO findFastestAvailableRoom();

    // 2.선택매칭시 개인정보 출력
    UserVO getUserPersonalInfo(int userId);

    // 3.선택 매칭 (AI에 전달하기 위한 방 목록 출현)
    List<RoomInVO> getRoomListForAi();

    // 4.새로운 방에 입장하면 역할이 팀원으로 설정되어 personList에 추가
    boolean addUserToRoom(Person person);
}
