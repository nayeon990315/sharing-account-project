package com.example.backend.user.service;

import com.example.backend.user.vo.SurveyVO;
import com.example.backend.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.user.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    // 1. 로그인 (아이디, 비밀번호 확인)
    @Override
    public UserVO findUserByIdPwd(String userId, String pwd) {
        return userMapper.findUserByIdPwd(userId, pwd);
    }

    // 2. 회원가입 (인적사항 추가)
    @Override
    public void insertUser(UserVO userVO) {
        userMapper.insertUser(userVO);
    }

    // 3-1. 프로필 변경 (이름, 성별, 나이 한 번에 변경)
    @Override
    public void updateUserInfo(UserVO userVO) {
        userMapper.updateUserInfo(userVO);
    }

    // 3-2. 비밀번호 변경
    @Override
    public void updateUserPwd(String userId, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pwd", pwd);

        userMapper.updateUserPwd(params);
    }

    // 4. 회원 탈퇴
    @Override
    public void deleteUser(String userId) {
        userMapper.deleteUser(userId);
    }

    // 5. 아이디 중복 확인
    @Override
    public boolean isUserIdDuplicate(String userId) {
        int count = userMapper.countByUserId(userId);
        return count > 0; // 중복이면 true
    }

    // 6. 설문조사 데이터 저장
    @Override
    public void insertSurvey(SurveyVO surveyVO) {
        userMapper.insertSurvey(surveyVO);
    }

    // 7. 설문조사 데이터 조회
    @Override
    public SurveyVO getSurvey(String userId) {
        return userMapper.getSurvey(userId);
    }

    // 8. 개인정보 출력
    @Override
    public UserVO selectInfo(String userId) {
        return userMapper.selectInfo(userId);
    }

    //28 경고 상태 보여주기
    @Override
    public UserVO showWarning(String userId) {
        return userMapper.showWarning(userId);
    }
    //29 경고 추가
    public void addWarningByName(String name) {
        userMapper.incrementWarningByName(name);
    }
}
