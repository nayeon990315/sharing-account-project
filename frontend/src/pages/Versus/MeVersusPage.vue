<template>
    <div class="versus-container">
        <div class="button-group">
            <button class="btn btn-primary mb-2" @click="selectYesterday">1일전</button>
            <button class="btn btn-secondary mb-2" @click="selectMonthAgo">1달전</button>
            <button class="btn btn-success mb-2" @click="selectYearAgo">1년전</button>

            <!-- 날짜 선택을 위한 버튼과 달력 -->
            <div class="date-picker-container">
                <Datepicker v-model="selectedDate" locale="ko" :enable-time-picker="false" />
                <button class="btn btn-info mb-2" @click="selectCalender">기간설정</button>
            </div>
        </div>
        <!-- 총 지출 내역 -->
        <div class="total">
            <p>오늘의 총 지출: {{ totalToday }} 원</p>
            <p>과거의 총 지출: {{ totalPast }} 원</p>
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
        <div class="expenses">
            <div id="present-expense">
                <ul>
                    <li>여기에 과거</li>
                    <li>지출 내역</li>
                </ul>
            </div>

            <div id="past-expense">
                <ul>
                    <li>여기에 현재</li>
                    <li>지출 내역</li>
                </ul>
            </div>
        </div>
        <div id="chart" class="half">
            <BarChart :expensesToday="computedExpensesToday" :expensesPast="computedExpensesPast" />
        </div>
        <div id="routine" class="half">
            <!-- RoutinesList 컴포넌트에 routinesToday와 routinesPast를 props로 전달 -->
            <RoutinesList :routinesToday="routinesToday" :routinesPast="routinesPast" />
        </div>
    </div>
</template>

<script>
import { ref, computed } from 'vue';
import BarChart from './BarChart.vue';
import RoutinesList from './RoutinesList.vue';
import Datepicker from '@vuepic/vue-datepicker'; // 달력 라이브러리 import
import '@vuepic/vue-datepicker/dist/main.css'; // 달력 스타일 import

export default {
    name: 'MeVersusPage',
    components: { BarChart, RoutinesList, Datepicker },
    setup() {
        const today = new Date();
        const targetDate = ref(new Date());
        const selectedDate = ref(new Date());
        //오늘의 루틴
        //habit check
        const routinesToday = ref([
            { "routine_name": "아침 식사 준비", "category": "식비" },
            { "routine_name": "저녁 외식", "category": "식비" },
            { "routine_name": "아침 커피 한 잔", "category": "카페/간식" },
            { "routine_name": "오후 간식 타임", "category": "카페/간식" },
            { "routine_name": "월간 필수 아이템 구매", "category": "온라인쇼핑" },
            { "routine_name": "할인 쿠폰으로 쇼핑하기", "category": "온라인쇼핑" },
            { "routine_name": "화장품 꼭 필요한 것만 사기", "category": "뷰티" }
        ]);
        //오늘의 소비
        //내역
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

        //내역
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

        //과거의 루틴
        const routinesPast = ref([]);
        //과거의 소비
        const expensesPast = ref([]);

        const computedExpensesToday = computed(() => expensesToday.value);
        const computedExpensesPast = computed(()=>expensesPast.value);

        //날짜 포맷팅 메소드 ex)2024-09-24
        const formatDate = (date) => {
            const year = date.getFullYear();
            const month = date.getMonth() + 1;
            const day = date.getDate();
            return year + '-' + month + '-' + day;
        }

        const selectYesterday = () => {
            expensesPast.value = expensesPastDay;
            computedExpensesPast.value = computed(() => expensesPast.value);
            routinesPast.value = routinesPastDay;

            console.log(expensesPast.value);

            const yesterday = new Date();
            yesterday.setDate(today.getDate() - 1);
            targetDate.value = formatDate(yesterday);
            console.log(targetDate.value);
        };

        const selectMonthAgo = () => {
            expensesPast.value = expensesPastMonth;
            routinesPast.value = routinesPastMonth;

            const monthAgo = new Date();
            monthAgo.setMonth(today.getMonth() - 1);
            targetDate.value = formatDate(monthAgo);
            console.log(targetDate.value);
        };
        const selectYearAgo = () => {
            expensesPast.value = expensesPastYear;
            routinesPast.value = routinesPastYear;
            const yearAgo = new Date();
            yearAgo.setFullYear(today.getFullYear() - 1)
            targetDate.value = formatDate(yearAgo);
            console.log(targetDate.value);
        };
        const selectCalender = () => {
            let formatedDate = formatDate(selectedDate.value);
            targetDate.value = formatedDate;
            console.log(targetDate.value);
        }



        //현재는 더미데이터로 두었지만, 백엔드 구현 시 api로 바꿀 예정
        const routinesPastDay = [
            { "routine_name": "아침 식사 준비", "category": "식비" },
            { "routine_name": "저녁 외식", "category": "식비" },
            { "routine_name": "아침 커피 한 잔", "category": "카페/간식" },
            { "routine_name": "오후 간식 타임", "category": "카페/간식" },
            { "routine_name": "월간 필수 아이템 구매", "category": "온라인쇼핑" },
            { "routine_name": "할인 쿠폰으로 쇼핑하기", "category": "온라인쇼핑" },
            { "routine_name": "화장품 꼭 필요한 것만 사기", "category": "뷰티" }
        ];
        const routinesPastMonth = [
            {
                "routine_name": "점심 도시락 준비",
                "category": "식비"
            },
            {
                "routine_name": "헬스장 운동",
                "category": "운동"
            },
            {
                "routine_name": "퇴근 후 카페 방문",
                "category": "카페/간식"
            },
            {
                "routine_name": "온라인 쇼핑몰 할인받기",
                "category": "온라인쇼핑"
            },
            {
                "routine_name": "주말 영화 관람",
                "category": "영화/문화"
            },
            {
                "routine_name": "아침 스트레칭",
                "category": "운동"
            }
        ];

        const routinesPastYear = [
            {
                "routine_name": "저녁 요리하기",
                "category": "식비"
            },
            {
                "routine_name": "아침 러닝",
                "category": "운동"
            },
            {
                "routine_name": "도서관에서 독서",
                "category": "취미/여가"
            },
            {
                "routine_name": "친구와 점심 약속",
                "category": "식비"
            },
            { "routine_name": "아침 식사 준비", "category": "식비" },
            { "routine_name": "저녁 외식", "category": "식비" },
            { "routine_name": "아침 커피 한 잔", "category": "카페/간식" },
            { "routine_name": "오후 간식 타임", "category": "카페/간식" },
            { "routine_name": "월간 필수 아이템 구매", "category": "온라인쇼핑" },
            { "routine_name": "할인 쿠폰으로 쇼핑하기", "category": "온라인쇼핑" },
            { "routine_name": "화장품 꼭 필요한 것만 사기", "category": "뷰티" }
        ];
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
        ];
        const expensesPastYear = [
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
        return {
            routinesToday,
            routinesPast,
            computedExpensesToday,
            computedExpensesPast,
            selectYesterday,
            selectMonthAgo,
            selectYearAgo,
            selectCalender,
            selectedDate,
            formatDate,
            totalToday,
            totalPast,
            winnerMessage,
            targetDate
        };
    }
};
</script>

<style scoped>
.versus-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100vh;
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
#chart {
    background-color: #f9f9f9;
    border-radius: 10px;
    padding: 15px;
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

    .chart-container {
        height: auto;
    }
}
</style>