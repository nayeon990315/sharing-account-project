package com.example.backend.roomlist.vo;

import lombok.Data;

@Data
public class RoomListVO {
    int roomNum;
    String accountNumber;
    String title;
    String category;
    int period;
    int dueDate;
    int memberTotal;
    int memberCurrent;
    String role;
}
