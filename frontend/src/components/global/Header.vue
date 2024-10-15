<template>
    <!-- <header class="header"> -->
    <header class=" navbar sticky-top bg-body-tertiary">
        <!-- Left Menu Buttons -->
        <div class="menu-buttons" >
            <div class="logo" @click="navigateByLoginStatus">
                
                <img src="@/assets/images/header/bee_header3.png">
            </div>

            <div class="dropdown dropdown p-2" id="routineMoneyDropdown">
                <button class="btn dropdown-toggle btn-outline-dark rounded-pill menu-btn" type="button"
                    id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    Routine & Money
                </button>
                <ul class="dropdown-menu menu-item" aria-labelledby="dropdownMenuButton1">
                    <li><a href="/spending-by-routine" class="dropdown-item">루틴별 지출</a></li>
                    <li><a href="/versus" class="dropdown-item" >절약: 나 vs 나</a></li>
                </ul>
            </div>

            <!-- <div class="dropdown dropdown p-2" id="moneyDropdown">
                <button class="btn dropdown-toggle btn-outline-dark rounded-pill menu-btn" type="button"
                    id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    Money
                </button>
                <ul class="dropdown-menu menu-item" aria-labelledby="dropdownMenuButton1">
                    <li><a class="dropdown-item" href="/versus">절약: 나 vs 나</a></li>
                </ul>
            </div> -->

            <div class="dropdown dropdown p-2" id="communityDropdown">
                <button class="btn dropdown-toggle btn-outline-dark rounded-pill menu-btn" type="button"
                    id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    Community
                </button>
                <ul class="dropdown-menu menu-item" aria-labelledby="dropdownMenuButton1">
                    <li><a href="/routine-community" class="dropdown-item">벌루틴 공유</a></li>
                    <li><a href="/post-community/explore" class="dropdown-item" >허니샷 인증</a></li>
                </ul>
            </div>
        </div>

        <div class="dropdown dropdown p-2" id="myDropdown">
                <button class="btn dropdown-toggle btn-outline-dark rounded-pill menu-btn" type="button"
                    id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    My
                </button>
                <ul class="dropdown-menu menu-item" aria-labelledby="dropdownMenuButton1">
                    <li><a href="/myroutine/settings" class="dropdown-item">마이루틴 설정</a></li>
                    <li><a href="/myreward" class="dropdown-item">마이페이지</a></li>
                </ul>
            </div>

        

        <div class="auth-buttons">
            <template v-if="isLoggedIn">
                <!-- <span>반갑습니다 {{ userName }}님!</span> -->
                <!-- <router-link to="/myreward"><button class="auth-button">Mypage</button></router-link> -->
                <button class="auth-button" @click="logout">Log out</button>
            </template>
            <template v-else>
                <router-link to="/login"><button class="auth-button">Log in</button></router-link>
                <!-- <router-link to="/register"><button class="auth-button">Join</button></router-link> -->
            </template>

        </div>
    </header>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Dropdown } from 'bootstrap';
import { useRouter } from 'vue-router';
import VueCookies from 'vue-cookies';

const activeMenu = ref(null);
const isLoggedIn = ref(false);  // 로그인 여부 상태
const userName = ref('');  // 사용자 이름 상태

const router = useRouter();

// const hoverMenu = (menu) => {
//     activeMenu.value = menu;s
// };

// 드롭다운 메뉴가 호버 시 열리고 닫히도록 설정
onMounted(() => {
    checkLoginStatus();

    const dropdowns = ['routineMoneyDropdown', 'communityDropdown', 'myDropdown']; // 각 드롭다운 ID

    // dropdowns.forEach(id => {
    //     const dropdownElement = document.querySelector(`#${id} button`);
    //     const dropdownInstance = new Dropdown(dropdownElement);

    //     const dropdownContainer = document.getElementById(id);

    //     dropdownContainer.addEventListener('mouseover', () => {
    //         dropdownInstance.show();
    //         dropdownElement.style.backgroundColor = "#000000";
    //         dropdownElement.style.color = "#FFFFFF";
    //     });

    //     dropdownContainer.addEventListener('mouseleave', () => {
    //         dropdownInstance.hide();
    //         dropdownElement.style.backgroundColor = "transparent";
    //         dropdownElement.style.color = "black";
    //     });
    // });

    // 로그아웃 기능
});

router.beforeEach((to, from, next) => {
    checkLoginStatus();  // 페이지 이동할 때마다 로그인 상태 확인
    next();
});

const checkLoginStatus = () => {
    const storedNickName = localStorage.getItem('nickname');  // localStorage에서 사용자 이름 가져오기
    if (storedNickName) {
        isLoggedIn.value = true;
        userName.value = storedNickName;  // 사용자 이름 설정
    } else {
        isLoggedIn.value = false;
    }
};

const logout = () => {

    localStorage.removeItem('nickname');
    localStorage.removeItem('userId');

    // 쿠키 삭제
    VueCookies.remove('jwtToken');
    isLoggedIn.value = false;
    router.push('/login');  // 로그인 페이지로 리다이렉트
};


// 홈 버튼
const navigateByLoginStatus = () => {
    if (isLoggedIn.value) {
        router.push('/loginHome');  // 로그인된 경우
    } else {
        router.push('/');  // 로그인되지 않은 경우
    }
};
</script>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    /* padding: 20px; */
    /* background-color:  #6363633a ; */
    width: 100%;
    position: relative; /* 헤더 내에서 요소를 정렬 */
}

.navbar {
    height: 45px;
    background-color:  rgba(255, 255, 255, 0.48) !important;
    display: flex; 
    align-items: center; /* 세로 가운데 정렬 */
    justify-content: space-between; /* 양쪽 끝으로 로고와 버튼을 배치 */
    padding: 0;
    margin: 0;
    backdrop-filter: blur(10px); /* 흐릿한 블러 효과 */
    /* filter: contrast(1.2); 약간의 명암 조절로 흐릿한 효과 강조 */

}

.logo {
    position: relative;
    left: 0;
    margin-left: 20px;
}

.menu-buttons {
    display: flex;
    flex-direction: row;
    /* align-items: flex-start; */
    align-items: center;
    justify-content: space-around; /* 버튼들 간격 동일하게 배치 */
    flex-grow: 1;

}

.logo img{
    width: 30px;
}

.auth-buttons {
    display: flex;
    align-items: center;
    justify-content: space-around; /* 버튼들 간격을 균등하게 배치 */
    flex-grow: 1; /* 버튼들이 navbar 안에서 균등한 간격을 유지 */
}

.auth-button {
    background-color: transparent;
    border: none;
    font-weight: 400;
    font-size: 14px;
    margin-left: 20px;
    cursor: pointer;
}

/* 드롭다운 버튼 기본 스타일 */
.dropdown-toggle {
    background-color: transparent;
    color: black;
    border: none; 
    font-weight: normal; 
}

/* 호버 시 드롭다운 버튼 스타일 */
.dropdown-toggle:hover, .dropdown-toggle:active {
    font-weight: bold; 
    /* background: none; */
    /* color: #FFFFFF; */
    background-color: transparent;
    border: none;
}

.btn:active,
.btn:focus {
    outline: none !important;
    box-shadow: none !important;
    /* background-color: #5a5a5a; */
}

.dropdown-item:active {
    background-color: black;
}


.menu-btn {
    width: 120px;
    /* 버튼의 너비 고정 */
}

.dropdown-menu {
    background-color: rgba(255, 255, 255, 0.502);
    border: none; 
    color: black;
}

.menu-item {
    width: 120px;
    /* 드롭다운 메뉴창 너비 고정 */
}
</style>
