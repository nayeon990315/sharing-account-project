package com.example.backend.roomdetails.vo;

import com.example.backend.user.vo.UserVO;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class RoomDetailsVO {
    // 1. 방 번호
    private int roomNum;
    // 2. 회비 제출 날짜
    private Date dueDate;
    // 3. 참여자 목록
    private List<UserVO> participants;
    // 4. 거래 내역(모임통장)
    private List<Transaction> transactions;
}
