package com.example.backend.roomdetails.service;

import com.example.backend.roomdetails.dto.*;

import com.example.backend.roomdetails.mapper.RoomDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomDetailsServiceImpl implements RoomDetailsService {

    private final RoomDetailsMapper roomDetailsMapper;

    @Autowired
    public RoomDetailsServiceImpl(RoomDetailsMapper roomDetailsMapper) {
        this.roomDetailsMapper = roomDetailsMapper;
    }

    @Override
    public RoomDetailsDto getRoomBasicInfo(int roomNum) {
        return roomDetailsMapper.getRoomBasicInfo(roomNum);
    }

    @Override
    public List<ParticipantDto> getParticipantList(int roomNum) {
        return roomDetailsMapper.getParticipantList(roomNum);
    }

    @Override
    public SubscriptionAccountDto getSubscriptionAccountInfo(int roomNum) {
        return roomDetailsMapper.getSubscriptionAccountInfo(roomNum);
    }

    @Override
    public List<PaymentStatusDto> getMonthlyPaymentStatus(int roomNum) {
        return roomDetailsMapper.getMonthlyPaymentStatus(roomNum);
    }

    @Override
    public List<TransactionDto> getAccountTransactionHistory(String accountNumber) {
        return roomDetailsMapper.getAccountTransactionHistory(accountNumber);
    }

    @Override
    public void insertTransaction(String accountNumber, String transactionDetails, int amount) {
        roomDetailsMapper.insertTransaction(accountNumber, transactionDetails, amount);
    }
}
