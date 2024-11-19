package com.example.backend.roomdetails.mapper;

import com.example.backend.roomdetails.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomDetailsMapper {

    RoomDetailsDto getRoomBasicInfo(@Param("roomNum") int roomNum);

    List<ParticipantDto> getParticipantList(@Param("roomNum") int roomNum);

    SubscriptionAccountDto getSubscriptionAccountInfo(@Param("roomNum") int roomNum);

    List<PaymentStatusDto> getMonthlyPaymentStatus(@Param("roomNum") int roomNum);

    List<TransactionDto> getAccountTransactionHistory(@Param("accountNumber") String accountNumber);

    void insertTransaction(@Param("accountNumber") String accountNumber,
                           @Param("transactionDetails") String transactionDetails,
                           @Param("amount") int amount);
}
