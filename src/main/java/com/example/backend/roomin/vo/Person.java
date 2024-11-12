package com.example.backend.roomin.vo;

import lombok.Data;

@Data
public class Person {
    private int roomNum;    // 방 번호
    private int userId;     // 사용자 ID
    private String role;    // 역할 (팀원)
}
