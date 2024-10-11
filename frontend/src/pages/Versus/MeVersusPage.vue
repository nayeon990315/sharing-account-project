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
        <!-- 총 지출 내역 -->
        <div class="total">
            <p>오늘의 총 지출: {{ totalToday }} 원</p>
            <p>과거의 총 지출: {{ totalPast }} 원</p>
        </div>
        <div class="savings">
            <p>오늘 절약한 금액: {{ habit?.savedAmountToday}} 원</p>
            <p>과거 절약한 금액: {{ habit.savedAmountPast }} 원</p>
        </div>
        <!-- 승리 결과 -->
        <div class="winner-result"
            :class="{ 'today-win': winnerMessage === true, 'past-win': winnerMessage === false, 'draw': winnerMessage === '무승부!' }">
            <template v-if="winnerMessage === true">
                <p>오늘 더 적은 지출을 했습니다!</p>
            </template>
            <template v-else-if="winnerMessage === false">
                <p>{{ targetDate }}에 더 적은 지출을 했습니다!</p>
            </template>
            <template v-else>
                <p>{{ winnerMessage }}</p>
            </template>
        </div>
        <div id="routine" class="half">
            <!-- RoutinesList 컴포넌트에 routinesToday와 routinesPast를 props로 전달 -->
            <RoutinesList :routinesToday="routinesToday" :routinesPast="routinesPast" :showPastRoutines="showPastRoutines" />
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import RoutinesList from './RoutinesList.vue';
import Datepicker from '@vuepic/vue-datepicker';  // 달력 라이브러리 import
import '@vuepic/vue-datepicker/dist/main.css';    // 달력 스타일 import

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

const habit = ref({
    savedAmountToday: 0,
    savedAmountPast: 0,
});

// 로컬스토리지에서 사용자 아이디 가져오기
const getUserIdFromLocal = async () => {
    userId.value = localStorage.getItem('userId');

    if (!userId.value) {
        alert('로그인이 필요합니다.');
        return;
    }
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
        // await saveMoneyPast();
        
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
    {
        transactionDate: '2024-09-22T10:15',
        type: '체크카드',
        category: '카페/간식',
        recipient: '스타벅스신촌역점',
        withdrawalAmount: 5000,
        depositAmount: 0,
        balance: 85000
    },
    {
        transactionDate: '2024-09-22T12:30',
        type: '체크카드',
        category: '식비',
        recipient: '맥도날드홍대점',
        withdrawalAmount: 7600,
        depositAmount: 0,
        balance: 77400
    },
    {
        transactionDate: '2024-09-22T14:00',
        type: '체크카드',
        category: '의류',
        recipient: '유니클로강남점',
        withdrawalAmount: 35000,
        depositAmount: 0,
        balance: 42400
    },
    
    {
        transactionDate: '2024-09-22T16:10',
        type: '체크카드',
        category: '교통비',
        recipient: '카카오택시',
        withdrawalAmount: 9500,
        depositAmount: 0,
        balance: 20900
    },
    {
        transactionDate: '2024-09-22T17:50',
        type: '체크카드',
        category: '식비',
        recipient: '백년토종순대국',
        withdrawalAmount: 8500,
        depositAmount: 0,
        balance: 12400
    },
    
    {
        transactionDate: '2024-09-22T20:45',
        type: '체크카드',
        category: '카페/간식',
        recipient: '탐앤탐스신촌',
        withdrawalAmount: 4500,
        depositAmount: 0,
        balance: 1000
    },
    {
        transactionDate: '2024-09-22T21:20',
        type: '체크카드',
        category: '식비',
        recipient: '롯데리아강남점',
        withdrawalAmount: 7400,
        depositAmount: 0,
        balance: -6400
    },
    {
        transactionDate: '2024-09-22T22:00',
        type: '체크카드',
        category: '교통비',
        recipient: '서울지하철',
        withdrawalAmount: 1250,
        depositAmount: 0,
        balance: -7650
    }
]);

// 내역
// computed 속성으로 expensesToday와 expensesPast의 총 지출 계산
const totalToday = computed(() => {
    return expensesToday.value.reduce((acc, expense) => acc + expense.withdrawalAmount, 0);
});

const totalPast = computed(() => {
    return expensesPast.value.reduce((acc, expense) => acc + expense.withdrawalAmount, 0);
});

const winnerMessage = computed(() => {
    if (totalToday.value < totalPast.value) {
        return true;
    } else if (totalToday.value > totalPast.value) {
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
const selectDate = (days, unit) => {
    showPastRoutines.value = true;
    const newDate = new Date();
    if (unit === '일전') newDate.setDate(today.getDate() - days);
    else if (unit === '주전') newDate.setDate(today.getDate() - days);
    else if (unit === '달전') newDate.setMonth(today.getMonth() - days);
    else if (unit === '년전') newDate.setFullYear(today.getFullYear() - days);
    targetDate.value = formatDate(newDate);
    getPastData(); 
    saveMoneyPast();
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
        transactionDate: "2024-09-21T10:15",
        type: "체크카드",
        category: "카페/간식",
        recipient: "스타벅스신촌역점",
        withdrawalAmount: 5000,
        depositAmount: 0,
        balance: 85000
    },
    {
        transactionDate: "2024-09-21T12:30",
        type: "체크카드",
        category: "식비",
        recipient: "맥도날드홍대점",
        withdrawalAmount: 7600,
        depositAmount: 0,
        balance: 77400
    },
    {
        transactionDate: "2024-09-21T14:00",
        type: "체크카드",
        category: "의류",
        recipient: "유니클로강남점",
        withdrawalAmount: 35000,
        depositAmount: 0,
        balance: 42400
    },
    {
        transactionDate: "2024-09-21T11:01",
        type: "체크카드",
        category: "의류",
        recipient: "유니클로강남점",
        withdrawalAmount: 35000,
        depositAmount: 0,
        balance: 42400
    }
];

const expensesPastMonth = [
    {
        transactionDate: "2024-09-21T10:15",
        type: "체크카드",
        category: "카페/간식",
        recipient: "스타벅스신촌역점",
        withdrawalAmount: 5000,
        depositAmount: 0,
        balance: 85000
    },
    {
        transactionDate: "2024-09-21T12:30",
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
.versus-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    /* height: 100vh; */
    background-color: #f5f5f5;
}


.half {
    width: 90%;
    height: 50%;
    /* 카드와 차트가 더 커지도록 비율을 수정 */
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    margin: 10px 0;
    box-sizing: border-box;
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.today-win {
    color: green;
}

.past-win {
    color: red;
}

.draw {
    color: orange;
}

.card {
    border-radius: 15px;
    overflow: hidden;
    margin-bottom: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    background-color: #fff;
    padding: 15px;
    height: 350px;
    /* 카드 높이를 350px로 확대 */
}

.card-body {
    max-height: 250px;
    /* 카드 본문의 최대 높이를 늘림 */
    overflow-y: auto;
    /* 스크롤바 추가 */
}

.card-title {
    font-size: 1.5em;
    font-weight: bold;
    margin-bottom: 10px;
    text-align: center;
}

.list-group-item {
    font-size: 1.1em;
    padding: 10px;
    text-align: center;
}

.badge {
    font-size: 0.9em;
}

@media (max-width: 768px) {
    .versus-container {
        flex-direction: column;
    }

    .half {
        width: 100%;
        height: auto;
    }
}
</style>