package com.example.backend.user.service;

import com.example.backend.user.vo.SurveyVO;
import com.example.backend.user.vo.UserVO;

import java.util.Map;

public interface UserService {

    // 1. 로그인 (아이디, 비밀번호 확인)
    String findUserByIdPwd(String userId, String pwd);

    // 2. 회원가입 (인적사항 추가)
    void insertUser(UserVO userVO);

    // 3-1. 프로필 변경 (이름, 성별, 나이 한 번에 변경)
    void updateUserInfo(UserVO userVO);

    // 3-2. 비밀번호 변경
    public void updateUserPwd(String userId, String pwd);

    // 4. 회원 탈퇴
    public void deleteUser(String userId);

    // 5. 아이디 중복 확인
    public boolean isUserIdDuplicate(String userId);

    // 6. 설문조사 데이터 저장
    public void insertSurvey(SurveyVO surveyVO);

    // 7. 설문조사 데이터 조회
    public SurveyVO getSurvey(String userId);

    // 8. 개인정보 출력
    public UserVO selectInfo(String userId);
}
