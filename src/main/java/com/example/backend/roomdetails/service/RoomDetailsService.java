package com.example.backend.roomdetails.service;

import com.example.backend.roomdetails.dto.*;

import java.util.List;

public interface RoomDetailsService {
    RoomDetailsDto getRoomBasicInfo(int roomNum);

    List<ParticipantDto> getParticipantList(int roomNum);

    SubscriptionAccountDto getSubscriptionAccountInfo(int roomNum);

    List<PaymentStatusDto> getMonthlyPaymentStatus(int roomNum);

    List<TransactionDto> getAccountTransactionHistory(String accountNumber);

    void insertTransaction(String accountNumber, String transactionDetails, int amount);
}
