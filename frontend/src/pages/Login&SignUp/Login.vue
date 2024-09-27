<template>
  <div class="login">
    <div class="wrap">

      <!-- LOGIN FORM -->
      <div class="user">
        <!-- LOGO -->
        <div class="logo">
          <p>벌루틴 로고</p>
        </div>

        <!-- LOGIN, RECOVERY FORMS -->
        <div class="form-wrap">

          <!-- LOGIN FORM -->
          <div v-if="isLogin">
            <form class="login-form" @submit.prevent="login">
              <input type="text" class="input" placeholder="아이디" v-model="loginForm.username" />
              <input type="password" class="input" placeholder="비밀번호" v-model="loginForm.password" />
              <input type="checkbox" class="checkbox" v-model="loginForm.rememberMe" />
              <label for="remember_me">로그인 정보 저장</label>
              <input type="submit" class="button loginButton" value="로그인" />
            </form>

            <div class="help-action">
              <p><a href="#" @click.prevent="showPasswordRecovery">아이디/비밀번호 찾기</a></p>
            </div>

            <div class="signUp">
              <router-link to="/signup"><p>회원가입하기</p></router-link>
            </div>

            <div class="divider">
              <hr> <span> or </span> <hr>
            </div>

            <!-- 소셜 로그인 -->
            <div>
              <div class="socialLogin googleLogin"><img src="@/assets/icons/googleIcon.png">구글 계정으로 로그인</div>
              <div class="socialLogin kakaoLogin"><img src="@/assets/icons/kakaoIcon.png">카카오 계정으로 로그인</div>
              <div class="socialLogin naverLogin"><img src="@/assets/icons/naverIcon.png">네이버 계정으로 로그인</div>
            </div>
          </div>

          <!-- PASSWORD/ID RECOVERY TABS -->
          <div class="recovery" v-if="isPasswordRecovery">

            <!-- ID RECOVERY FORM -->
            <div class="recoverID">
              <h2>아이디 찾기<br><br></h2>
              <p>사용자 이름과 가입한 이메일을 입력하세요.<br></p>
              <form class="recovery-form" @submit.prevent="findId">
                <input type="text" class="input" placeholder="사용자 이름" v-model="recoveryForm.username" />
                <input type="email" class="input" placeholder="가입한 이메일" v-model="recoveryForm.email" />
                <input type="submit" class="button" value="아이디 찾기" />
              </form>
            </div>

            <!-- PASSWORD RECOVERY FORM -->
            <div  class="recoverPW">
              <h2>비밀번호 찾기<br><br></h2>
              <p>아이디와 가입한 이메일을 입력하세요.<br></p>
              <form class="recovery-form" @submit.prevent="findPassword">
                <input type="text" class="input" placeholder="아이디" v-model="recoveryForm.username" />
                <input type="email" class="input" placeholder="가입한 이메일" v-model="recoveryForm.email" />
                <input type="submit" class="button" value="비밀번호 찾기" />
              </form>
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

body {
  
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffffff;
  color: #000000;
}

.logo {
    margin-bottom: 70px;
    text-align: center;
}

footer {
    height: 80px;
}



/* 전체 모듈 */
.login {
  background-color: transparent;
  width: 400px;
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
}

input[type="text"],
input[type="password"],
input[type="email"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  background: transparent;
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




/* Forgot password link */
.help-action, .signUp {
  text-align: center;
}

.help-action a, .signUp a {
  color: rgb(84, 84, 84);
  font-size: 13px;
  text-decoration: none;
  margin-top: 5px;
}

.help-action a:hover, .signUp a:hover {
  text-decoration: underline;
}


/* 소셜 로그인 */
.socialLogin {
    border: solid 1px rgb(126, 126, 126);
    text-align: center;
    padding: 6px;
    margin-top: 5px;
    /* background-color: #eeeeee; */
}

.socialLogin img {
    width: 20px;
    margin-right: 5px;
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





/* -------------------------------- */
/* 복구화면 */
.recovery {
  display: grid;
  grid-template-columns: 1fr 1fr;
  width: 100vh;
  gap: 20px; /* 그리드 아이템 사이의 간격 추가 */
}

.recoverID {
  grid-column: 1;
}

.recoverPW {
  grid-column: 2;
}

</style>
