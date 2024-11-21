package com.example.backend.roomlist.controller;

import com.example.backend.exception.CustomNotFoundException;
import com.example.backend.roomlist.service.RoomListService;
import com.example.backend.roomlist.vo.RoomListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomListController {
    private final RoomListService roomListService;

    public RoomListController(RoomListService roomListService) {
        this.roomListService = roomListService;
    }

    // 5. 구독 카테고리 별 방 목록 출력
    @GetMapping("/category/{category}")
    public ResponseEntity<List<RoomListVO>> selectRoomListByCategory(@PathVariable String category) {
        try {
            List<RoomListVO> roomList = roomListService.selectRoomListByCategory(category);
            return ResponseEntity.ok(roomList); // 정상 응답
        } catch (CustomNotFoundException e) {
            log.info("404 Error : Category not found by {}", category);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            log.info("500 Error : An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // 6. 검색 내용이 포함된 방 목록 출력
    @GetMapping("/title/{keyword}")
    public ResponseEntity<List<RoomListVO>> selectRoomListByTitle(@PathVariable String keyword) {
        try {
            List<RoomListVO> roomList = roomListService.selectRoomListByTitle(keyword);
            return ResponseEntity.ok(roomList); // 정상 응답
        } catch (CustomNotFoundException e) {
            log.info("404 Error : Keyword not found by {}.", keyword);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            log.info("500 Error : An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<RoomListVO>> getRoomsByUser(@RequestParam("userId") String userId) {
        List<RoomListVO> rooms = roomListService.getRoomsByUserId(userId);
        return ResponseEntity.ok(rooms);
    }
}
