package com.example.backend.roomdetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionAccountDto {
    private String accountId;
    private String accountPassword;
}
