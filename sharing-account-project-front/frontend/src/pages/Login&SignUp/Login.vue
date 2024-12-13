<template>
  <div class="login">
    <div class="wrap">

      <div class="user">

        
        <div class="form-wrap">

          <div v-if="isLogin">
            <div class="info">
              <div class="logo">
                <img src="@/assets/images/bee/bee4.png" alt="" style="width: 110px">
              </div>
              <h1>Login</h1>
              <p>소셜 계정으로 간편하게 시작하세요.<br>로그인 즉시 가입 및 이용이 가능합니다.</p>
            </div>

            <div class="socialLoginForm">
              <div class="socialLogin kakaoLogin"
              @click="kakaoLogin"><img src="@/assets/icons/kakaoIcon.png">카카오 계정으로 로그인</div>
              <div class="socialLogin googleLogin"
              @click="googleLogin"><img src="@/assets/icons/googleIcon.png">구글 계정으로 로그인</div>
              <div class="socialLogin naverLogin"
              @click="naverLogin"><img src="@/assets/icons/naverIcon.png">네이버 계정으로 로그인</div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>

<footer></footer>
</template>

<script setup>
import { ref, reactive } from 'vue';

const isLogin = ref(true);
const isPasswordRecovery = ref(false);
const activeTab = ref('id');  // 기본 탭은 아이디 찾기

const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false,
});

const recoveryForm = reactive({
  username: '',
  email: '',
});

const showPasswordRecovery = () => {
  isLogin.value = false;
  isPasswordRecovery.value = true;
};

const login = () => {
  console.log('Logging in with', loginForm);
};

const findId = () => {
  console.log('Finding ID for', recoveryForm.username, 'with email', recoveryForm.email);
};

const findPassword = () => {
  console.log('Finding password for', recoveryForm.username, 'with email', recoveryForm.email);
};
const googleLogin = () => {
  
  const callBackUrl = 'https://accounts.google.com/o/oauth2/v2/auth?client_id=' +
      '988888812178-qvlick1q80lcvlc56sdi50noiqd5n1r2.apps.googleusercontent.com' +
      '&redirect_uri=' +
      'http://localhost:8080/login/oauth2/code/google' +
      '&response_type=code' +
      '&scope=email profile';

      window.location.href = callBackUrl; // 백엔드의 구글 OAuth2 경로로 이동
};

const kakaoLogin = () =>{
  const callBackUrl = 'https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=598ab35b62d4a6a3684bd57b070cb398&redirect_uri=http://localhost:8080/login/oauth2/code/kakao&scope=profile_nickname,profile_image,account_email'

  window.location.href = callBackUrl; // 백엔드의 Kakao OAuth2 경로로 이동
}

const naverLogin = ()=>{
  const callBackUrl = 'https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=omEGJqXqex9ZirWPDyqC&state=STATE_STRING&redirect_uri=http://localhost:8080/login/oauth2/code/naver'

  window.location.href = callBackUrl;
}
</script>


<style scoped>
/* General resets */
@font-face {
  font-family: 'Wanted Sans';
  src: url('@/assets/fonts/WantedSans-Regular.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Wanted Sans';
}

/* template {
  background-color: #FFD565;
} */

body {
  
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  /* background-color:#FFD565; */
  color: #000000;
}

.form-wrap {
  /* border: solid 4px black; */
  /* padding: 80px; */
  width: 300px;
  /* height: 300px; */
  height: 100%;
  /* background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='%23333' stroke-width='4' stroke-dasharray='12%2c 15' stroke-dashoffset='0' stroke-linecap='round'/%3e%3c/svg%3e"); */
  display: flex;
  align-items: center;
  justify-content: center;
  /* background-color: #FFD565; */
  border-radius: 15px;
  background-color: #ffecb3;

  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.1);
}

.logo {
    /* margin-bottom: 70px; */
    text-align: center;
}




/* 전체 모듈 */
.info {
  padding-top: 30px;
  text-align: center;
}

.info h1{
  font-weight: 800;
  margin-top: 10px;
  margin-bottom: 30px;
  font-size: 2.5rem;
  
}

.info p {
  margin-bottom: 50px;
}
.login {
  /* background: url('@/assets/images/background/background.png'); */
  /* background-color: #ffd8154c; */
  /* width: 800px; */
  padding: 40px;
  margin: auto;
  border-radius: 8px;
  /* box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); */
}





h3 {
  color: #000000;
  margin-bottom: 20px;
}




/* 세부 폼 스타일 */
.form-wrap {
  text-align: left;
  width: 450px;
  margin: 0 auto;
}

input[type="text"],
input[type="password"],
input[type="email"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  /* background-color white; */
  border: solid 1px rgb(50, 50, 50);
  color:black;
}

input[type="checkbox"] {
  margin-right: 10px;
}

label {
  color: #ccc;
  font-size: 14px;
}

input[type="submit"] {
  width: 100%;
  padding: 10px;
  background-color:black;
  border: none;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

input[type="submit"]:hover {
  background-color: #FFD565;
}



/* 소셜 로그인 */

.socialLoginForm {
  margin-bottom: 60px;
}
.socialLogin {
    border: solid 2px rgb(126, 126, 126);
    text-align: center;
    padding: 6px;
    margin-top: 5px;
    background-color: white;
    /* background-color: #eeeeee; */

    width: 300px;
    margin: 10px auto;
}

.socialLogin img {
    width: 20px;
    margin-right: 5px;
}

.socialLogin:hover {
    background-color: #FFD565;
    transform: translateY(-2px); /* 버튼이 약간 위로 올라가는 효과 */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 살짝 눌리는 효과 */
}

.socialLogin:active {
    transform: translateY(2px); /* 버튼이 눌리는 효과 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 눌린 상태에서 그림자 변화 */
}

/* or 구분선 */
.divider {
  display: flex;
  align-items: center;
  text-align: center;
  margin: 20px 0;
}

.divider hr {
  flex-grow: 1;
  border: none;
  border-top: 1px solid #696969;
  margin: 0 10px;
}

.divider span {
  white-space: nowrap;
  padding: 0 10px;
  font-size: 14px;
  color: #666;
}







</style>
