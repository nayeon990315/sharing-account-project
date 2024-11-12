package com.example.backend.roomin.vo;

import lombok.Data;

@Data
public class RoomInVO {
    private int roomNum;               // 방 번호
    private String accountNumber;       // 계좌 번호
    private String title;               // 방 제목
    private String category;            // 카테고리 (구독 종류)
    private int numberTotal;            // 전체 구성원 수
    private int numberCurrent;          // 현재 참여 인원 수
    private String period;              // 사용 기간
    private String dueDate;             // 회비 제출 기한
    private String privateOrPublic;     // 공개 여부 (public/private)
}
