package com.example.backend.roombasic.controller;

import com.example.backend.exception.CustomNotFoundException;
import com.example.backend.roombasic.service.RoomBasicService;
import com.example.backend.roombasic.vo.RoomBasicVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("room/basic")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RoomBasicController {

    private final RoomBasicService roomBasicService;

    // 7. 방 상세 정보 저장
    // 8. 방 현재 구성원 수 저장 (기본값 1)
    // 9. 사용자 목록 정보 추가 (기본값 팀장)
    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody RoomBasicVO roombasicVO, @RequestParam String creatorId) {
        try {
            roomBasicService.createRoom(roombasicVO, creatorId);
            return ResponseEntity.status(HttpStatus.OK).body("Create room successfully.");
        } catch (CustomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to create room.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create room.");
        }
    }

    // 3. 방 세부 내용 변경 - 방번호, 계좌번호, 팀장은 못 바꿈
    @PutMapping("/update")
    public String updateRoomDetails(@RequestBody RoomBasicVO roomBasicVO) {
        try {
            roomBasicService.updateRoomBasic(roomBasicVO);
            return "update room successfully";
        } catch (Exception e) {
            return "room update error: " + e.getMessage();
        }
    }
}
