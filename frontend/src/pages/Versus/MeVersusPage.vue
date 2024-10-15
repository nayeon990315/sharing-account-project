<template>

    <div class="info">
        <h1>Savings: Today vs Past</h1>
        <h5>절약: 나 vs 나</h5>
        <p>'과거의 나'와 '오늘의 나' 중 누가 더 적게 쓰고, 더 많이 절약했을까요?</p>
    </div>


    <div class="versus-container">

        <div class="dateSelect">
            <p>대결할 과거의 날짜를 선택한 후 카드를 클릭하세요</p>
            <div class="button-group">

                <div>
                    <button class="btn " @click="selectDate(1, '일전')">1일전</button>
                    <button class="btn " @click="selectDate(7, '주전')">1주전</button>
                    <button class="btn  " @click="selectDate(1, '달전')">1달전</button>
                    <button class="btn " @click="selectDate(1, '년전')">1년전</button>
                </div>

                <div class="chooseDate">
                    <!-- <p> 직접 날짜 선택</p> -->
                    <div class="date-picker-container">
                        <Datepicker v-model="selectedDate" locale="ko" :enable-time-picker="false"
                            @change="selectCalender" />

                    </div>
                    <button class="btn btn-info " @click="selectCalender">기간설정</button>
                </div>

            </div>
        </div>

        <!-- 카드 보기 버튼 -->
        <!-- <button class="btn btn-flip" @click="toggleFlip">카드 보기</button> -->


        <!-- 승리 결과 -->
        <div class="winner-result" v-show="showPastRoutines && isFlipped"
            :class="{ 'today-win': winnerMessage === 'true', 'past-win': winnerMessage === 'false', 'draw': winnerMessage === '무승부!' }">

            <template v-if="winnerMessage === 'true'">
                <p>오늘 더 많은 절약을 했습니다!</p>
            </template>
            <template v-else-if="winnerMessage === 'false'">
                <p>{{ formatTemplateDate(targetDate) }}에 더 많은 절약을 했습니다!</p>
            </template>
            <template v-else>
                <p>{{ winnerMessage }}</p>
            </template>
        </div>

        <div class="container" @click="toggleFlip">
            <!-- Left 카드 -->
            <div class="past-section">
                <div class="dateInfo">
                    <h3>Past</h3>
                    <p>{{ formatTemplateDate(targetDate) }}</p>
                </div>
                <div class="card-container">
                    <div class="card" :class="{ flipped: isFlipped }">
                        <div class="card-front left">
                            <img src="@/assets/images/tarot/tarot3.png" alt="Tarot Card">
                        </div>

                        <div class="card-back left">
                            <!-- <div class="profile-section">
                        <img :src="profileImageUrl" alt="Profile" class="profile-image2" :class="{ 'winner-border': winnerMessage === false}"/>
                    </div> -->

                            <p class="cardWin" v-if="winnerMessage === 'false'">Winner</p>
                            <p class="cardWin" v-else-if="winnerMessage === 'true'">Loser</p>
                            <p class="cardWin" v-else>Draw</p>

                            <div class="total">
                                <div class="stat">
                                    <p class="statInfo">총 지출
                                        <!-- <span v-if="showPastRoutines">({{ targetDate }})</span> -->
                                    </p>
                                    <p class="statValue"> {{ totalPast }}원</p>
                                </div>
                            </div>
                            <div class="savings">
                                <div class="stat">
                                    <p class="statInfo">절약한 금액
                                        <!-- <span v-if="showPastRoutines">({{ targetDate }})</span> -->
                                    </p>
                                    <p class="statValue">{{ habit.savedAmountPast }}원</p>
                                </div>
                            </div>
                            <div id="routine">
                                <PastRoutinesList :routinesToday="routinesToday" :routinesPast="routinesPast"
                                    :showPastRoutines="showPastRoutines" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right 카드 -->
            <div class="today-section">
                <div class="dateInfo">
                    <h3>Today </h3>
                    <p>{{ formatTemplateDate(today) }}</p>
                </div>
                <div class="card-container">
                    <div class="card" :class="{ flipped: isFlipped }">
                        <div class="card-front right">
                            <img src="@/assets/images/tarot/tarot3.png" alt="Tarot Card">
                        </div>

                        <div class="card-back right">
                            <!-- <div class="profile-section">
                            <img :src="profileImageUrl" alt="Profile" class="profile-image1" :class="{ 'winner-border': winnerMessage === true && showPastRoutines == true}"/>
                        </div> -->

                            <p class="cardWin" v-if="winnerMessage === 'true'">Winner</p>
                            <p class="cardWin" v-else-if="winnerMessage === 'false'">Loser</p>
                            <p class="cardWin" v-else>Draw</p>


                            <div class="total">
                                <div class="stat">
                                    <p class="statInfo">총 지출</p>
                                    <p class="statValue">{{ totalToday }}원</p>
                                </div>
                            </div>
                            <div class="savings">
                                <div class="stat">
                                    <p class="statInfo">절약한 금액</p>
                                    <p class="statValue">{{ habit.savedAmountToday }}원</p>
                                </div>
                            </div>
                            <div id="routine">
                                <PresentRoutinesList :routinesToday="routinesToday" :routinesPast="routinesPast" />
                            </div>
                        </div>
                    </div>
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
const EXPENSE_API_URL = 'http://localhost:3000';

const userId = ref(null);
const checkDate = ref(null);
const checkedHabits = ref([]);

const routinesToday = ref([]);         // 오늘 달성한 루틴
const routinesPast = ref([]);          // 과거 달성한 루틴
const showPastRoutines = ref(false);   // 과거 루틴 랜더링 여부

const today = new Date();
const yesterday = new Date(today);
yesterday.setDate(today.getDate() - 1);

const targetDate = ref(yesterday);
const selectedDate = ref(yesterday);



const formatTemplateDate = (date) => {
    if (typeof (date) === 'string') {
        const dateResult = date.split('-')
        const strYear = dateResult[0]
        const strMonth = dateResult[1]
        const strDay = dateResult[2]
        return `${strYear}년 ${strMonth}월 ${strDay}일`;
    }
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}년 ${month}월 ${day}일`;
};

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

// 카드 뒤집기
const isFlipped = ref(false);

const toggleFlip = () => {
    isFlipped.value = !isFlipped.value;
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
    // { transactionDate: '2024-09-22T10:15', type: '체크카드', category: '카페/간식', recipient: '스타벅스신촌역점', withdrawalAmount: 5000, depositAmount: 0, balance: 85000 },
    // { transactionDate: '2024-09-22T12:30', type: '체크카드', category: '식비', recipient: '맥도날드홍대점', withdrawalAmount: 7600, depositAmount: 0, balance: 77400 },
    // { transactionDate: '2024-09-22T14:00', type: '체크카드', category: '의류', recipient: '유니클로강남점', withdrawalAmount: 35000, depositAmount: 0, balance: 42400 },
    // { transactionDate: '2024-09-22T16:10', type: '체크카드', category: '교통비', recipient: '카카오택시', withdrawalAmount: 9500, depositAmount: 0, balance: 20900 },
    // { transactionDate: '2024-09-22T17:50', type: '체크카드', category: '식비', recipient: '백년토종순대국', withdrawalAmount: 8500, depositAmount: 0, balance: 12400 },
    // { transactionDate: '2024-09-22T20:45', type: '체크카드', category: '카페/간식', recipient: '탐앤탐스신촌', withdrawalAmount: 4500, depositAmount: 0, balance: 1000 },
    // { transactionDate: '2024-09-22T21:20', type: '체크카드', category: '식비', recipient: '롯데리아강남점', withdrawalAmount: 7400, depositAmount: 0, balance: -6400 },
    // { transactionDate: '2024-09-22T22:00', type: '체크카드', category: '교통비', recipient: '서울지하철', withdrawalAmount: 1250, depositAmount: 0, balance: -7650 }
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
        console.log('true1231231231')
        return 'true';
    } else if (habit.value.savedAmountToday < habit.value.savedAmountPast) {
        console.log('false1231231231')
        return 'false';
    } else {
        console.log('무승부!1231231231')
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
        // expensesPast.value = expensesPastDay; 
    } else if (unit === '주전') {
        newDate.setDate(today.getDate() - days);
        // expensesPast.value = expensesPastWeek;
    } else if (unit === '달전') {
        newDate.setMonth(today.getMonth() - days);
        // expensesPast.value = expensesPastMonth; 
    } else if (unit === '년전') {
        newDate.setFullYear(today.getFullYear() - days);
        // expensesPast.value = expensesPastYear;
    }

    targetDate.value = formatDate(newDate);
    await getPastData();
    await saveMoneyPast();
    await getExpensesPast();
};

const getExpensesToday = async () => {
    try {
        const formattedToday = formatDate(today);
        const response = await axios.get(`${EXPENSE_API_URL}/${formattedToday}`);
        expensesToday.value = response.data;
        console.log('오늘의 소비 내역:', expensesToday.value);
    } catch (error) {
        console.error('오늘의 소비 내역을 가져오는 중 오류 발생:', error);
        expensesToday.value = [];
    }
};

const getExpensesPast = async () => {
    try {
        const response = await axios.get(`${EXPENSE_API_URL}/${targetDate.value}`);
        expensesPast.value = response.data;
        console.log(`${targetDate.value}의 소비 내역:`, expensesPast.value);
    } catch (error) {
        // console.error('과거의 소비 내역을 가져오는 중 오류 발생:', error);
        expensesPast.value = [];
    }
};

const selectCalender = async () => {
    showPastRoutines.value = true;
    targetDate.value = formatDate(selectedDate.value);
    await getPastData();
    await saveMoneyPast();
    await getExpensesPast();
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
    getExpensesToday();
});

</script>

<style scoped>
/* 인포 */
.info {
    margin: 6% 8%;
}

.info h1 {
    font-weight: 800;
}

.info p {
    /* font-weight: 700; */
    margin-top: 25px;
}

/* .versus-container {
    display: grid;
    width: 100%;
} */

.versus-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    /* justify-items: center; */
}

.container {
    display: flex;
    justify-content: space-between;
    /* flex-direction: column; */
    /* grid-template-columns: 1fr 1fr; */
    /* grid-template-rows: 1; */
    /* gap: 0; */
    margin: 8% auto;
    padding: 0;
    width: 70%;
    /* height: 100vh; 화면 높이 전체 사용 */
}

.past-section,
.today-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    /* 가운데 정렬 */
    width: 50%;
    /* 두 섹션이 나란히 배치되게 하기 위해 */
    text-align: center;
}

.left {
    width: 50%;
    background-color: black;
    /* grid-column: 1; */
    /* box-sizing: border-box; */

}

.right {
    width: 50%;
    background-color: black;
    /* grid-column: 2;  */
    /* box-sizing: border-box; */
}

/* 날짜 선택 */
.dateSelect {
    text-align: center;
    /* justify-self: center; */
    width: 500px;
    display: flex;
    justify-content: center;
    flex-direction: column;
    margin: 0 auto;
}

.button-group {
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.button-group button {
    margin: 0 5px;
}

.chooseDate {
    display: flex;
    flex-direction: row;
    width: 100%;
    justify-content: center;
}

.chooseDate button {
    margin-left: 6px;
    font-size: 13px;
    /* padding: 3px; */
}


.btn {
    background-color: black;
    color: white;
    border-radius: 0;
    border: none;
}

/* 카드 내부 */
/* .dateInfo {
    color: white;
} */

.profile-section {
    display: flex;
    justify-content: center;
    align-items: center;
}

.profile-image1,
.profile-image2 {
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
    margin: 0 20px;

}

.savings {
    display: flex;
    justify-content: space-between;
    margin-left: 20px;
    margin-right: 20px;
}

.stat {
    /* background-color: #f0f0f0; */
    color: black;
    /* padding: 20px; */
    width: 100%;
    /* margin: 10px; */
    text-align: center;
}

.stat .statInfo {
    font-weight: 600;
    margin-bottom: 0;
    color: rgb(89, 57, 0);
}

.stat .statValue {
    font-weight: 900;
    font-size: 22px;
    color: rgb(89, 57, 0);
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
    width: 250px;
    margin: 0 auto;
}

.winner-border {
    border: 5px solid green;
    animation: borderPulse 1s infinite;
}

.winner-result {
    text-align: center;
    margin-top: 30px;
    font-weight: 800;
    font-size: 20px;
    /* visibility: hidden; */
    height: 50px;
    /* visibility: hidden; */
    /* opacity: 0; 카드가 뒤집히기 전에는 투명하게 설정 */
}

.flipped~.winner-result {
    /* visibility: visible; */
    opacity: 1;
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


/* 뒤집히는 카드 */
.card-container {
    width: 300px;
    height: 500px;
    perspective: 1500px;
    /* 3D 효과를 주기 위한 perspective */
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    /* 수평 중앙 정렬 */
    position: relative;
}

.card {
    width: 100%;
    height: 100%;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: transform 0.6s ease;
    transform-style: preserve-3d;
    transform-origin: center;

    border: none;
}

.flipped {
    transform: rotateY(180deg);
}

.card-front,
.card-back {
    position: absolute;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    justify-items: center;
    align-items: center;
    backface-visibility: hidden;
    border-radius: 30px;
    border: none;
    top: 0;
    left: 0;
}

.card-front img,
.card-back img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 30px;
    border: none;

    filter: drop-shadow(10px 6px 20px #c3c3c3);
    /*그림자*/
}

.card-back {
    background: url('@/assets/images/tarot/tarot_back.png');
    background-size: 100% 100%;
    color: rgb(0, 0, 0);
    transform: rotateY(180deg);
    border-radius: 30px;
    border: none;
    flex-direction: column;
    justify-content: center;
    padding: 20px;

    filter: drop-shadow(10px 6px 20px #c3c3c3);
    /*그림자*/
}

.btn-flip {
    margin-bottom: 20px;
}

/* 카드 안 성공 메시지 */
.cardWin {
    font-weight: 800;
    font-size: 24px;
    text-decoration: underline;
}
</style>