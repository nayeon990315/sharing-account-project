package com.example.backend.roomdetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailsDto {
    private String title;
    private String subscriptionType;
    private String duration;
    private String fundraisingDate;
}

