package com.example.backend.roomin.controller;

import com.example.backend.roomin.service.RoomInService;
import com.example.backend.roomin.vo.Person;
import com.example.backend.roomin.vo.RoomInVO;
import com.example.backend.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/roomin")
public class RoomInController {
    @Autowired
    private RoomInService roomInService;

    // 1.빠른 매칭 (RoomList 중 가장 빨리 입장 가능한 방 확인)
    @GetMapping("/fast-match")
    public ResponseEntity<RoomInVO> getFastestAvailableRoom() {
        RoomInVO room = roomInService.findFastestAvailableRoom();
        return ResponseEntity.ok(room);
    }
    // 2.선택 매칭 (AI에 전달하기 위한 개인정보 출력)
    @GetMapping("/user-info/{userId}")
    public ResponseEntity<UserVO> getUserPersonalInfo(@PathVariable int userId) {
        UserVO user = roomInService.getUserPersonalInfo(userId);
        return ResponseEntity.ok(user);
    }

    // 3.선택 매칭 (AI에 전달하기 위한 방 목록 출현)
    @GetMapping("/room-list")
    public ResponseEntity<List<RoomInVO>> getRoomListForAi() {
        List<RoomInVO> roomList = roomInService.getRoomListForAi();
        return ResponseEntity.ok(roomList);
    }
    // 4.사용자 추가(새로운 방에 입ㅇ장하면 역할이 팀원으로 설정되어 personList에 추가)
    @PostMapping("/add-user")
    public ResponseEntity<String> addUserToRoom(@RequestBody Person person) {
        boolean isAdded = roomInService.addUserToRoom(person);
        if (isAdded) {
            return ResponseEntity.ok("성공적으로 방에 들어갔음.");
        } else {
            return ResponseEntity.badRequest().body("실패.");
        }
    }
}
