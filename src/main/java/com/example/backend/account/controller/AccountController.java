package com.example.backend.account.controller;

import com.example.backend.account.service.AccountService;
import com.example.backend.account.vo.AccountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 1. 모임통장 개설 (새로운 모임 통장 계좌번호 추가)
    @PostMapping("/sharingAccount/create")
    public ResponseEntity<AccountVO> createAccount(@RequestParam String userId) {
        return ResponseEntity.ok(accountService.createSharingAccount(userId));
    }
    // 2.
}
