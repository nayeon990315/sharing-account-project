<template>
    <header class="header">
        <!-- Left Menu Buttons -->
        <div class="menu-buttons">
            <div class="dropdown dropend p-2" id="routineDropdown">
                <button class="btn dropdown-toggle btn-outline-dark rounded-pill menu-btn" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                Routine
                </button>
                <ul class="dropdown-menu menu-item" aria-labelledby="dropdownMenuButton1">
                <li><a class="dropdown-item" href="/myroutine">마이 루틴 관리</a></li>
                </ul>
            </div>

            <div class="dropdown dropend p-2" id="moneyDropdown">
                <button class="btn dropdown-toggle btn-outline-dark rounded-pill menu-btn" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                Money
                </button>
                <ul class="dropdown-menu menu-item" aria-labelledby="dropdownMenuButton1">
                <li><a class="dropdown-item" href="/versus">지출: 나 vs 나</a></li>
                <li><a class="dropdown-item" href="#">가계부</a></li>
                </ul>
            </div>

            <div class="dropdown dropend p-2" id="communityDropdown">
                <button class="btn dropdown-toggle btn-outline-dark rounded-pill menu-btn" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                Community
                </button>
                <ul class="dropdown-menu menu-item" aria-labelledby="dropdownMenuButton1">
                <li><a class="dropdown-item" href="/routine-community">습관 공유 커뮤니티</a></li>
                <li><a class="dropdown-item" href="/post-community">인증 커뮤니티</a></li>
                </ul>
            </div>
        </div>

        <div class="logo">
            <h1>K-Bee</h1>
        </div>

        <div class="auth-buttons">
            <router-link to="/login"><button class="auth-button">Log in</button></router-link>
            <button class="auth-button">Join</button>
        </div>
    </header>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Dropdown } from 'bootstrap';

const activeMenu = ref(null);

// const hoverMenu = (menu) => {
//     activeMenu.value = menu;
// };

// 드롭다운 메뉴가 호버 시 열리고 닫히도록 설정
onMounted(() => {
    const dropdowns = ['routineDropdown', 'moneyDropdown', 'communityDropdown']; // 각 드롭다운 ID

    dropdowns.forEach(id => {
        const dropdownElement = document.querySelector(`#${id} button`);
        const dropdownInstance = new Dropdown(dropdownElement);

        const dropdownContainer = document.getElementById(id);

        dropdownContainer.addEventListener('mouseover', () => {
            dropdownInstance.show();
            dropdownElement.style.backgroundColor = "#000000";
            dropdownElement.style.color = "#FFFFFF";
        });

        dropdownContainer.addEventListener('mouseleave', () => {
            dropdownInstance.hide();
            dropdownElement.style.backgroundColor = "#FFFFFF";
            dropdownElement.style.color = "#000000";
        });
    });
});

</script>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    background-color: #F5F5F5;
    width: 1000px;
}

.menu-buttons {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.logo {
    text-align: center;
    font-size: 2rem;
    font-weight: bold;
}

.auth-buttons {
    display: flex;
    align-items: center;
}

.auth-button {
    background-color: transparent;
    border: none;
    font-weight: 400;
    font-size: 1.2rem;
    margin-left: 20px;
    cursor: pointer;
}

/* 드롭다운 버튼 기본 스타일 */
.dropdown-toggle {
    background-color: transparent;
    color: black;
}

/* 호버 시 드롭다운 버튼 스타일 */
.dropdown-toggle:hover {
    background-color: #000000;
    color: #FFFFFF;
}

.btn:active, .btn:focus {
    outline: none !important;
    box-shadow: none !important;
}

.menu-btn {
    width: 150px; /* 버튼의 너비 고정 */
}

.menu-item {
    width: 150px; /* 드롭다운 메뉴창 너비 고정 */
}
</style>
