package com.example.backend.roomstart.vo;

import lombok.Data;

@Data
public class RoomStartVO {
    private int roomNum;          // 방 번호
    private int numberCurrent;     // 현재 참여 인원 수
    private int numberTotal;       // 설정된 인원 수
    private String subscribeAccount; // 구독 계정 정보
    private String subscribePwd;     // 구독 계정 비밀번호
}
