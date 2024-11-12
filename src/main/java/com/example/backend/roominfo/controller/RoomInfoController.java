package com.example.backend.roominfo.controller;

import com.example.backend.roominfo.service.RoomInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RoomInfoController {
    private final RoomInfoService roomInfoService;

    public RoomInfoController(RoomInfoService roomInfoService) {
        this.roomInfoService = roomInfoService;
    }

    // 1. 참여 중 통장 목록 출력
    @GetMapping("/account/participation")
    public List<String> participationAccount() {
        return roomInfoService.participationAccount();
    }

    // 2. 참여 중 아닌 통장 목록 출력
    @GetMapping("/account/not/participation")
    public List<String> notParticipationAccount() {
        return roomInfoService.notParticipationAccount();
    }

    // 3. 시작 전 : 지금까지 모인 인원 수 출력
    @GetMapping("/current/person/num")
    public List<Map<String, Object>> nowMemberNum() {
        return roomInfoService.nowMemberNum();
    }

    // 4. 진행 중 : 회비 납부 여부 체크

    // 5. 완료 : 보증금 이체
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam String accountNumber, @RequestParam String userName, @RequestParam int price) {
        try {
            roomInfoService.transfer(accountNumber, userName, price);
            return ResponseEntity.ok("[Success] Room period update completed");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("[Error] Room period update failed");
        }
    }
}
