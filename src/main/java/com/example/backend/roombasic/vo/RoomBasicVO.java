package com.example.backend.roombasic.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RoomBasicVO {
    private int roomNum;
    private String accountNumber;
    private String title;
    private String category;
    private int memberTotal;
    private int period;
    private int dueDate;
    
    // 팀장 ID는 따로 담지 않음

    // RoomComplete
    private int memberCurrent; // 현재 구성원 수 -> 1로 설정(팀장)

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getMemberTotal() {
        return memberTotal;
    }

    public void setMemberTotal(Integer memberTotal) {
        this.memberTotal = memberTotal;
    }
}
