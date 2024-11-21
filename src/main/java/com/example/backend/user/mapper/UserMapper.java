package com.example.backend.user.mapper;

import com.example.backend.user.vo.SurveyVO;
import com.example.backend.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    // 1. 로그인 (아이디, 비밀번호 확인)
    public UserVO findUserByIdPwd(@Param("userId") String userId, @Param("pwd") String pwd);


    // 2. 회원가입 (인적사항 추가)
    // 신규 사용자 등록, 우선 회원가입 시 모든 내용 다 입력하는 것으로 해놓음
    public void insertUser(UserVO userVO);

    // 3. 프로필 변경 (인적사항 수정)
    // 3-1. 이름, 성별, 나이를 한 번에 변경
    public void updateUserInfo(UserVO userVO); // id, object(여러 수정 정보)

    // 3-2. 비밀번호 변경
    public void updateUserPwd(Map<String, String> params);


    // 4. 회원 탈퇴
    public void deleteUser(@Param("userId") String userId);


    // 5. 아이디 중복 확인
    public int countByUserId(String userId);


    // 설문조사
    // 6. 설문조사 데이터 저장
    void insertSurvey(SurveyVO surveyVO);

    // 7. 설문조사 데이터 조회
    public SurveyVO getSurvey(String userId);


    // 8. 개인정보 출력
    public UserVO selectInfo(String userId);

    //28 개인 경고 출력
    public UserVO showWarning(String userId);

    //29 개인 경고 추가
    void incrementWarningByName(@Param("name") String name);

}
