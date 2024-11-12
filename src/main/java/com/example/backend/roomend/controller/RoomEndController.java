package com.example.backend.roomend.controller;

import com.example.backend.roomend.service.RoomEndService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rooms")
public class RoomEndController {
    private final RoomEndService roomEndService;

    public RoomEndController(RoomEndService roomEndService) {
        this.roomEndService = roomEndService;
    }

    // 1. 사용 기간 연장 시 기간 정보 수정
    @PutMapping("/update/period")
    public ResponseEntity<String> updatePeriod(@RequestParam int roomNum, @RequestParam int period) {
        try {
            roomEndService.updatePeriod(roomNum, period);
            return ResponseEntity.ok("[Success] Room period update completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("[Error] Room period update failed");
        }
    }

    // 2. 사용 기간 연장 안할 시 해당 방 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRoom(@RequestParam int roomNum) {
        try {
            roomEndService.deleteRoom(roomNum);
            return ResponseEntity.ok("[Success] Room deletion completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("[Error] Room deletion failed");
        }
    }
}
