package com.example.backend.roomout.controller;

import com.example.backend.roomout.service.RoomOutService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rooms")
public class RoomoutController {
    private final RoomOutService roomOutService;

    public RoomoutController(RoomOutService roomOutService) {
        this.roomOutService = roomOutService;
    }

    // 1. 사용자 목록에서 삭제
    @DeleteMapping("/delete/person")
    public ResponseEntity<String> deletePerson(@RequestParam int roomNum, @RequestParam String userId) {
        try {
            roomOutService.deletePerson(roomNum, userId);
            return ResponseEntity.ok("[Success] Person deletion completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("[Error] Person deletion failed");
        }
    }

    // 2. 현재 인원 수 수정
    @PutMapping("/update/currentMember")
    public ResponseEntity<String> updateCurrentMember(@RequestParam int memberCurrent, @RequestParam int roomNum) {
        try {
            roomOutService.updateCurrentMemberNum(memberCurrent, roomNum);
            return ResponseEntity.ok("[Success] Current member number updated");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("[Error] Current member number failed");
        }
    }
}
