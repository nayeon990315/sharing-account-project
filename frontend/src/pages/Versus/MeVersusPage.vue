<template>
    <div class="versus-container">
        <div class="button-group">
            <button class="btn btn-primary mb-2" @click="selectDate(1, '일전')">1일전</button>
            <button class="btn btn-primary mb-2" @click="selectDate(7, '주전')">1주전</button>
            <button class="btn btn-secondary mb-2" @click="selectDate(1, '달전')">1달전</button>
            <button class="btn btn-success mb-2" @click="selectDate(1, '년전')">1년전</button>
            <!-- 날짜 선택을 위한 버튼과 달력 -->
            <div class="date-picker-container">
                <Datepicker v-model="selectedDate" locale="ko" :enable-time-picker="false" @change="selectCalender"/>
                
            </div>
            <button class="btn btn-info mb-2" @click="selectCalender">기간설정</button>
        </div>

        <!-- 승리 결과 -->
        <div class="winner-result" v-if="showPastRoutines"
            :class="{ 'today-win': winnerMessage === true, 'past-win': winnerMessage === false, 'draw': winnerMessage === '무승부!' }">
            <template v-if="winnerMessage === true">
                <p>오늘 더 많은 절약을 했습니다!</p>
            </template>
            <template v-else-if="winnerMessage === false">
                <p>{{ targetDate }}에 더 많은 절약을 했습니다!</p>
            </template>
            <template v-else>
                <p>{{ winnerMessage }}</p>
            </template>
        </div>

        <div class="container">
            <div class="left">
                <div class="profile-section">
                    <img :src="profileImageUrl" alt="Profile" class="profile-image1" :class="{ 'winner-border': winnerMessage === true && showPastRoutines == true}"/>
                </div>
                <div class="total">
                    <div class="stat">
                        <p>오늘의 총 지출</p>
                        <h3><strong>{{ totalToday }}</strong>원</h3>
                    </div>
                </div>
                <div class="savings">
                    <div class="stat">
                        <p>오늘 절약한 금액</p>
                        <h3><strong>{{ habit?.savedAmountToday }}</strong>원</h3>
                    </div>
                </div>
                <div id="routine">
                    <PresentRoutinesList :routinesToday="routinesToday" :routinesPast="routinesPast" />
                </div>
            </div>
            <div class="right">
                <div class="profile-section">
                    <img :src="profileImageUrl" alt="Profile" class="profile-image2" :class="{ 'winner-border': winnerMessage === false}"/>
                </div>
                <div class="total">
                    <div class="stat">
                        <p>과거의 총 지출 
                            <span v-if="showPastRoutines">({{ targetDate }})</span>
                        </p>
                        <h3><strong>{{ totalPast }}</strong>원</h3>
                    </div>
                </div>
                <div class="savings">
                    <div class="stat">
                        <p>과거 절약한 금액
                            <span v-if="showPastRoutines">({{ targetDate }})</span>
                        </p>
                        <h3><strong>{{ habit.savedAmountPast }}</strong>원</h3>
                    </div>
                </div>
                <div id="routine">
                    <PastRoutinesList :routinesToday="routinesToday" :routinesPast="routinesPast" :showPastRoutines="showPastRoutines" />
                </div>
            </div>  
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import PresentRoutinesList from './PresentRoutine.vue';
import PastRoutinesList from './PastRoutine.vue';
import Datepicker from '@vuepic/vue-datepicker';  // 달력 라이브러리 import
import '@vuepic/vue-datepicker/dist/main.css';    // 달력 스타일 import
import defaultProfileImage from '@/assets/profile.png'; 

const API_URL = 'http://localhost:8080';

const userId = ref(null);
const checkDate = ref(null);
const checkedHabits = ref([]);

const routinesToday = ref([]);         // 오늘 달성한 루틴
const routinesPast = ref([]);          // 과거 달성한 루틴
const showPastRoutines = ref(false);   // 과거 루틴 랜더링 여부

const today = new Date();
const targetDate = ref(new Date());
const selectedDate = ref(new Date());

const profileImageUrl = ref(defaultProfileImage);

const habit = ref({
    savedAmountToday: 0,
    savedAmountPast: 0,
});

const user = ref({
    avatar: '',
});

const getUserInfo = async () => {
  if (!userId.value) {
    console.error('userId가 존재하지 않습니다.');
    return;
  }
  
  try {
    const response = await axios.get(`${API_URL}/users/mypage?userId=${userId.value}`);
    Object.assign(user.value, response.data);  
    profileImageUrl.value = user.value.avatar || defaultProfileImage;  
    console.log('아이디로 사용자 정보를 가져왔습니다.');
  } catch (error) {
    console.error('사용자 정보 요청 중 오류 발생:', error);
  }
};

// 로컬스토리지에서 사용자 아이디 가져오기
const getUserIdFromLocal = async () => {
    userId.value = localStorage.getItem('userId');
    getUserInfo();
    getRoutinesToday();
    getPastData();
    saveMoneyToday();
};

// 오늘 달성한 루틴
const getRoutinesToday = async () => {
    try {
        const response = await axios.get(`${API_URL}/habits/checked`, {
            params: {
                userId: userId.value,
                checkDate: checkDate.value,
            },
        });
        console.log("오늘 Response data:", response.data);
        routinesToday.value = response.data.map(item => ({
            routine_name: item.habitTitle, 
            category: item.categoryTitle 
        }));
    } catch (error) {
        console.error("Error fetching checked habits:", error);
    }
};

// 오늘 절약한 금액
const saveMoneyToday = async () => {
    try {
        const response = await axios.get(`${API_URL}/habits/money/checked`, {
            params: {
                userId: userId.value,
                checkDate: checkDate.value,
            },
        });
        console.log("오늘 save money:", response.data);
        habit.value.savedAmountToday = response.data || 0;
    } catch (error) {
        console.error("Error fetching checked habits:", error);
        habit.value.savedAmountToday = 0;
    }
};

// 과거 달성한 루틴
const getPastData = async () => {
    try {
        const response = await axios.get(`${API_URL}/habits/checked`, {
            params: {
                userId: userId.value,
                checkDate: targetDate.value,
            },
        });
        console.log("과거 Response data:", response.data);
        console.log("날짜 : ", targetDate.value);
        routinesPast.value = response.data.map(item => ({
            routine_name: item.habitTitle, 
            category: item.categoryTitle 
        }));
        
    } catch (error) {
        console.error("Error fetching past data:", error);
    }
};

// 과거 절약한 금액
const saveMoneyPast = async () => {
    try {
        const response = await axios.get(`${API_URL}/habits/money/checked`, {
            params: {
                userId: userId.value,
                checkDate: targetDate.value,
            },
        });
        console.log("과거 save money:", response.data);
        habit.value.savedAmountPast = response.data || 0;
    } catch (error) {
        console.error("Error fetching checked habits:", error);
        habit.value.savedAmountPast = 0;
    }
};

// 오늘의 소비 내역
const expensesToday = ref([
    { transactionDate: '2024-09-22T10:15', type: '체크카드', category: '카페/간식', recipient: '스타벅스신촌역점', withdrawalAmount: 5000, depositAmount: 0, balance: 85000 },
    { transactionDate: '2024-09-22T12:30', type: '체크카드', category: '식비', recipient: '맥도날드홍대점', withdrawalAmount: 7600, depositAmount: 0, balance: 77400 },
    { transactionDate: '2024-09-22T14:00', type: '체크카드', category: '의류', recipient: '유니클로강남점', withdrawalAmount: 35000, depositAmount: 0, balance: 42400 },
    { transactionDate: '2024-09-22T16:10', type: '체크카드', category: '교통비', recipient: '카카오택시', withdrawalAmount: 9500, depositAmount: 0, balance: 20900 },
    { transactionDate: '2024-09-22T17:50', type: '체크카드', category: '식비', recipient: '백년토종순대국', withdrawalAmount: 8500, depositAmount: 0, balance: 12400 },
    { transactionDate: '2024-09-22T20:45', type: '체크카드', category: '카페/간식', recipient: '탐앤탐스신촌', withdrawalAmount: 4500, depositAmount: 0, balance: 1000 },
    { transactionDate: '2024-09-22T21:20', type: '체크카드', category: '식비', recipient: '롯데리아강남점', withdrawalAmount: 7400, depositAmount: 0, balance: -6400 },
    { transactionDate: '2024-09-22T22:00', type: '체크카드', category: '교통비', recipient: '서울지하철', withdrawalAmount: 1250, depositAmount: 0, balance: -7650 }
]);

// 내역
// computed 속성으로 expensesToday와 expensesPast의 총 지출 계산
const totalToday = computed(() => {
    return expensesToday.value.reduce((acc, expense) => acc + expense.withdrawalAmount, 0);
});

const totalPast = computed(() => {
    console.log("expensesPast:", expensesPast.value);
    return Array.isArray(expensesPast.value)
        ? expensesPast.value.reduce((sum, expense) => sum + expense.withdrawalAmount, 0)
        : 0; 
});

const winnerMessage = computed(() => {
    if (habit.value.savedAmountToday > habit.value.savedAmountPast) {
        return true;
    } else if (habit.value.savedAmountToday < habit.value.savedAmountPast) {
        return false;
    } else {
        return '무승부!';
    }
});

// 과거의 소비
const expensesPast = ref([]);

const computedExpensesToday = computed(() => expensesToday.value);
const computedExpensesPast = computed(() => expensesPast.value);

// 날짜 선택
const selectDate = async (days, unit) => {
    showPastRoutines.value = true;
    const newDate = new Date();
    if (unit === '일전') {
        newDate.setDate(today.getDate() - days);
        expensesPast.value = expensesPastDay; 
    } else if (unit === '주전') {
        newDate.setDate(today.getDate() - days);
        expensesPast.value = expensesPastWeek;
    } else if (unit === '달전') {
        newDate.setMonth(today.getMonth() - days);
        expensesPast.value = expensesPastMonth; 
    } else if (unit === '년전') {
        newDate.setFullYear(today.getFullYear() - days);
        expensesPast.value = expensesPastYear;
    }
    
    targetDate.value = formatDate(newDate);
    await getPastData(); 
    await saveMoneyPast();
};

const selectCalender = () => {
    showPastRoutines.value = true;
    targetDate.value = formatDate(selectedDate.value);
    getPastData(); 
    saveMoneyPast();
};

const formatDate = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    const day = date.getDate().toString().padStart(2, '0'); 
    return `${year}-${month}-${day}`;
};

const expensesPastDay = [
    {
        transactionDate: "2024-10-11T10:15",
        type: "체크카드",
        category: "카페/간식",
        recipient: "스타벅스신촌역점",
        withdrawalAmount: 5000,
        depositAmount: 0,
        balance: 85000
    },
    {
        transactionDate: "2024-10-11T12:30",
        type: "체크카드",
        category: "식비",
        recipient: "맥도날드홍대점",
        withdrawalAmount: 7600,
        depositAmount: 0,
        balance: 77400
    },
    {
        transactionDate: "2024-10-11T14:00",
        type: "체크카드",
        category: "의류",
        recipient: "유니클로강남점",
        withdrawalAmount: 35000,
        depositAmount: 0,
        balance: 42400
    },
    {
        transactionDate: "2024-10-11T11:01",
        type: "체크카드",
        category: "의류",
        recipient: "유니클로강남점",
        withdrawalAmount: 35000,
        depositAmount: 0,
        balance: 42400
    }
];

const expensesPastWeek = [];
const expensesPastYear = [];

const expensesPastMonth = [
    {
        transactionDate: "2024-10-12T10:15",
        type: "체크카드",
        category: "카페/간식",
        recipient: "스타벅스신촌역점",
        withdrawalAmount: 5000,
        depositAmount: 0,
        balance: 85000
    },
    {
        transactionDate: "2024-10-12T12:30",
        type: "체크카드",
        category: "식비",
        recipient: "맥도날드홍대점",
        withdrawalAmount: 7600,
        depositAmount: 0,
        balance: 77400
    }
];

onMounted(() => {
    getUserIdFromLocal();
});

</script>

<style scoped>

.container {
  display: flex;
  /* height: 100vh; 화면 높이 전체 사용 */
}

.left {
  background-color: rgb(213, 242, 252); 
  flex: 1;
}

.right {
  background-color: rgb(235, 193, 193); 
  flex: 1; 
}

.profile-section {
    display: flex;
    justify-content: center; 
    align-items: center; 
}

.profile-image1, .profile-image2{
  width: 140px;
  height: 150px;
  border-radius: 50%;
  margin-right: 30px;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid #ddd;
  transition: transform 0.3s ease, border-color 0.3s ease;
}

.total {
  display: flex;
  justify-content: space-between;
  margin-left: 20px;
  margin-right: 20px;
}

.savings {
  display: flex;
  justify-content: space-between;
  margin-left: 20px;
  margin-right: 20px;
}

.stat {
  background-color: #f0f0f0;
  padding: 20px;
  width: 100%;
  margin: 10px;
  border-radius: 5px;
  border: 2px solid #ddd; 
  background-color: #f9f9f9; 
  text-align: center;
}

.draw {
    color: orange;
}

.list-group-item {
    font-size: 1.1em;
    padding: 10px;
    text-align: center;
}

.badge {
    font-size: 0.9em;
}

#routine {
    display: flex;
    justify-content: center;
    align-items: center; 
    width: 100%;
}

.winner-border {
    border: 5px solid green; 
    animation: borderPulse 1s infinite;
}

.winner-result {
    text-align: center;
}

@keyframes borderPulse {
    0% {
        border-color: red;
    }
    50% {
        border-color: #FFD700; 
    }
    100% {
        border-color: red;
    }
}

</style>