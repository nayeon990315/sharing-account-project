<template>
    <div class="info">
        <h1 >Spending By Routine</h1>
        <h5>루틴별 지출</h5>
        <p>내 벌루틴에 대한 지출 내역을 한눈에 확인하고 달성을 체크해 보세요! 루틴을 지키기 위해 어떤 지출을 줄여나갈 수 있을지 함께 고민해요.</p>
    </div>

    <div class="container">
        <div class="row">
            <!-- Left Sidebar (Routine Category) -->
            <aside class="col-md-3 p-3 border-end">
                <h2 class="h4">내 루틴</h2>
                    <div class="info">
                        <span>루틴 목록을 선택하면 해당 카테고리의 지출 내역을 불러옵니다.</span><br/>
                    </div>
                <div class="">
                    <div v-for="(category, index) in routineCategories" :key="index" class="mb-3 bg-white"
                        :class="{ 
                            'selected-primary': selectedCategory === category,
                        }" 
                            @click="selectCategory(category)">
                        <!-- <span class="h6">{{ categoryEmojis[category] }}&nbsp;{{ category }}</span> -->
                        <!-- <h4 class="h6"><img src="@/assets/images/check/true.png" style="width:14px">&nbsp;{{ category }}</h4> -->
                        <span class="h6">{{ category }}</span>

                        <ul class="list-group border">
                            <li v-for="routine in filteredRoutines(category)" :key="routine.habitTitle"
                                class="list-group-item border"
                                :class="{'border-black border-2': routine.categoryTitle === selectedCategory}"
                                >
                                {{ routine.habitTitle }}
                            </li>
                        </ul>
                    </div>
                </div>
                <div 
                    v-if="selectedCategory !== '전체'"
                    class="mt-5 bg-white d-flex justify-content-center"
                    @click="selectCategory('전체')">
                    <ul class="list-group border">
                        <li class="list-group-item border reset">
                            <i class="fa-solid fa-arrow-rotate-right"></i>
                            <span>선택 해제</span> <!-- 텍스트 -->
                        </li>
                    </ul>
                </div>

            </aside>

            <!-- Main Content -->
            <main class="col-md-9 main-content p-3">
                <div class="row">
                    <!-- Daily Report Section -->
                    <section class="col-md-12">
                        <h2 class="h4">데일리 리포트</h2>
                        <div class="d-flex border p-3 block justify-content-between align-items-center" style="height: 250px;">
                            <div class="text-container">
                                <ul v-if="!selectedCategory || selectedCategory === '전체'">
                                    <li><span class="icon">💰</span>오늘 쓴 돈은 &nbsp;총&nbsp;<strong>{{ formatCurrency(totalExpenses)}}</strong>&nbsp;입니다.</li>
                                    <li><span class="icon">🏆</span> <strong>{{ maxExpenseCategory.maxCategory }}</strong>&nbsp;카테고리에서 제일 많이 쓰셨네요!</li>
                                    <li><span class="icon">📊</span> <strong>{{ formatCurrency(maxExpenseCategory.maxAmount) }}</strong>을 사용했어요.</li>
                                </ul>
                                <ul v-else>
                                    <li>{{categoryEmojis[selectedCategory]}}&nbsp;<strong>{{ selectedCategory }}</strong>을(를) 선택하셨네요!</li>
                                    <li>선택하신 카테고리에서 사용하신 금액은&nbsp;<strong>{{ formatCurrency(selectedCategoryExpenses.amount) }}</strong>이에요.</li>
                                    <li><strong style="font-size: medium;">{{nickname}}</strong>님, 오늘 지출의 &nbsp;<strong>{{ formatPercentage(selectedCategoryExpenses.percentage) }}</strong>를 차지하고 있어요!</li>
                                     
                                </ul>
                            </div>

                            <div class="text-center mb-3" style="height:30vh; width:22vw">
                                <canvas id="expenseChart"></canvas>
                            </div>
                            
                        </div>
                    </section>
                    
                    <!-- Spending Details Section -->
                    <section class="col-md-12 mt-2">
                        <h2 class="h4">지출 상세 내역</h2>
                        <!-- <div class="block"> -->
                            <table class="table table-bordered border border-2">
                                <thead class="">
                                    <tr>
                                        <th style="width: 20%;">시간</th>
                                        <th style="width: 50%;">지출 내역</th>
                                        <th style="width: 15%;">지출액</th>
                                        <th style="width: 15%;">잔액</th>
                                    </tr>
                                </thead>
                                <tbody class="text-center">
                                    <tr v-if="!selectedCategory">
                                        <td colspan="4" class="text-center">카테고리를 고르세요</td>
                                    </tr>
                                    <tr v-else-if="filteredExpenses.length == 0">
                                        <td colspan="4" class="text-center">지출 내역이 없습니다.</td>
                                    </tr>
                                    <tr v-else v-for="(expense, index) in filteredExpenses" :key="index">
                                        <td>{{ formatTime(expense.transactionDate) }}</td>
                                        <td>{{ expense.recipient }}</td>
                                        <td>{{ expense.withdrawalAmount }}원</td>
                                        <td>{{ expense.balance }}원</td>
                                    </tr>
                                </tbody>
                            </table>
                        <!-- </div> -->
                        
                    </section>
                </div>
            </main>
        </div>
    </div>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useHabitStore } from '@/stores/habitStore';
import { Chart, registerables } from 'chart.js';
import { bottom, right } from '@popperjs/core';
import axios from 'axios';

const habitStore = useHabitStore();

Chart.register(...registerables);
const chart = ref(null);

const backgroundColors = ref([
    '#FF6384',
    '#36A2EB',
    '#FFCE56',
    '#9966FF',
    '#4BC0C0',
    '#FF9F40',
    '#FFC300',
    '#DAF7A6',
]);

const nickname = ref(null);

const categoryEmojis = {
    "식비": "🍽️",
    "카페/간식": "☕",
    "온라인쇼핑": "🖥️",
    "패션/쇼핑": "👗",
    "문화/여가": "🎨",
    "술/유흥": "🍻",
    "교육": "📚",
    "의료/건강": "💊",
    "생활": "🧹",
    "주거/공과금": "💡",
    "금융": "💰",
    "뷰티": "💄",
    "자동차": "🚗",
    "교통": "🚋",
    "반려동물": "🐶",
    "여행": "✈️",
    "경조사/회비": "🎉"
};

const categoryBackgrounds = {
    "식비": "#1D75F6",
    "카페/간식": "#8B6555",
    "온라인쇼핑": "#3EBAC2",
    "패션/쇼핑": "#F5778D",
    "문화/여가": "#8C46C2",
    "술/유흥": "#FFAD0D",
    "교육": "#A2C616",
    "의료/건강": "#FA4949",
    "생활": "#76AFF7",
    "주거/공과금": "#6D5D3F",
    "금융": "#757E95",
    "뷰티": "#E659E9",
    "자동차": "#5B9FF4",
    "교통": "#949BAD",
    "반려동물": "#6D5D3F",
    "여행": "#696E76",
    "경조사/회비": "#F39C77",
    "기타": "#ffdfba"
};


// const expenses = ref([
//     {
//         transactionDate: '2024-06-26T18:17',
//         type: '체크카드',
//         category: '식비',
//         recipient: '사나이뚝배기화곡역',
//         withdrawalAmount: 7800,
//         depositAmount: 0,
//         balance: 691,
//     },
//     {
//         transactionDate: '2024-06-26T14:39',
//         type: '체크카드',
//         category: '카페/간식',
//         recipient: '애크로매틱어린이대',
//         withdrawalAmount: 3500,
//         depositAmount: 0,
//         balance: 8491,
//     },
//     {
//         transactionDate: '2024-06-26T13:10',
//         type: '체크카드',
//         category: '카페/간식',
//         recipient: '매머드커피앱_발트_1',
//         withdrawalAmount: 2000,
//         depositAmount: 0,
//         balance: 11991,
//     },
//     {
//         transactionDate: '2024-06-26T12:50',
//         type: '체크카드',
//         category: '식비',
//         recipient: '홈플러스익스프레스',
//         withdrawalAmount: 2590,
//         depositAmount: 0,
//         balance: 13991,
//     },
//     {
//         transactionDate: '2024-06-26T12:00',
//         type: '체크카드',
//         category: '식비',
//         recipient: '맘스터치화곡점',
//         withdrawalAmount: 9800,
//         depositAmount: 0,
//         balance: 16581,
//     },
//     {
//         transactionDate: '2024-06-26T09:11',
//         type: '체크카드',
//         category: '카페/간식',
//         recipient: '메가엠지씨커피우장',
//         withdrawalAmount: 2000,
//         depositAmount: 0,
//         balance: 26381,
//     },
//     {
//         transactionDate: '2024-06-26T09:31',
//         type: '체크카드',
//         category: '뷰티',
//         recipient: '블랭크유',
//         withdrawalAmount: 74000,
//         depositAmount: 0,
//         balance: 28381,
//     }
// ]);

const expenses = ref([])

const getBankData = async () => {
    const todayDate = '2024-10-15';  // 요청할 날짜
    try {
        const response = await axios.get(`http://localhost:3000/${todayDate}`);
        // 데이터가 성공적으로 반환되면 expenses 배열에 저장
        expenses.value = response.data.map(item => ({
            transactionDate: item.transactionDate,
            type: item.type,
            recipient: item.recipient,
            withdrawalAmount: item.withdrawalAmount,
            depositAmount: item.depositAmount,
            balance: item.balance,
            category: item.category,
            id: item.id
        }));
        console.log(expenses.value);  // 콘솔에서 데이터 확인
    } catch (error) {
        console.error('데이터 요청 실패:', error);
        // alert('데이터를 불러오는 중 오류가 발생했습니다.');
    }
};

const expensesByCategory = computed(() => 
    expenses.value.reduce((acc, expense) => {
        if (!acc[expense.category]) acc[expense.category] = 0;
        acc[expense.category] += expense.withdrawalAmount;
        return acc;
    }, {})
);

// 차트를 그리는 함수
const renderExpenseChart = () => {
    const ctx = document.getElementById('expenseChart').getContext('2d');
    if (!ctx) return;

    const categories = Object.keys(expensesByCategory.value);
    const amounts = Object.values(expensesByCategory.value);

    const habitsBackgroundColors = categories.map(category => categoryBackgrounds[category] || "#ffdfba");

    if (chart.value) chart.value.destroy(); // 기존 차트가 있으면 삭제

    chart.value = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: categories,
            datasets: [
                {
                    data: amounts,
                    backgroundColor: habitsBackgroundColors,
                    hoverOffset: 4,
                },
            ],
        },
        options: {
            responsive: true,
            plugins: {
                legend: { 
                    display: true,
                    position: 'right'
                }, // 범례를 표시하지 않음
            },
        },
    });
};
// 차트를 그리는 함수
const renderExpenseChartSelected = () => {
    const ctx = document.getElementById('expenseChart').getContext('2d');
    if (!ctx) return;

    const totalAmount = Object.values(expensesByCategory.value).reduce((acc, amount) => acc + amount, 0);  // 전체 지출 총액
    const selectedAmount = expensesByCategory.value[selectedCategory.value] || 0;  // 선택된 카테고리의 지출 금액
    const restAmount = totalAmount - selectedAmount;  // 나머지 카테고리의 지출 금액

    const categories = selectedAmount > 0 ? [selectedCategory.value, '나머지'] : ['나머지'];  // 카테고리명 설정
    const amounts = selectedAmount > 0 ? [selectedAmount, restAmount] : [restAmount];  // 지출 금액 설정

    // const selectedPercentage = selectedAmount > 0 ? (selectedAmount / totalAmount * 100).toFixed(2) : 0;  // 퍼센트 계산
    // console.log(selectedCategoryPercentage, '123123')
    // selectedCategoryPercentage.value = selectedPercentage
    // data.selectedCategoryPercentage.value = selectCategory
    // selectedCategoryPercentage.value = selectedPercentage

    if (chart.value) chart.value.destroy();  // 기존 차트가 있으면 삭제

    chart.value = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: categories,
            datasets: [
                {
                    data: amounts,
                    backgroundColor: selectedAmount > 0 ? [categoryBackgrounds[selectedCategory.value], '#E0E0E0'] : ['#E0E0E0'],  // 선택한 카테고리 색상과 나머지 색상
                    hoverOffset: 4,
                },
            ],
        },
        options: {
            responsive: true,
            plugins: {
                legend: { 
                    display: true,
                    position: 'right'
                }, 
            },
        },
    });
};

const renderExpenseChartHandler = () => {
    if (selectedCategory.value === '전체' || selectedCategory.value === null) {
        renderExpenseChart();  // '전체' 또는 카테고리가 선택되지 않은 경우 기본 차트를 표시
    } else {
        renderExpenseChartSelected();  // 선택된 카테고리가 있는 경우 선택된 카테고리와 나머지를 표시
    }
};

// 차트를 마운트된 후에 생성
onMounted(() => {
    selectedCategory.value = '전체'
    getBankData();
    renderExpenseChartHandler()
    nickname.value = localStorage.getItem('nickname')
});

// 지출 데이터가 변경될 때 차트를 업데이트
watch(expensesByCategory, () => {
    renderExpenseChartHandler();
});

const selectedCategory = ref(null);

// 카테고리별 루틴 필터링
const filteredRoutines = (category) => {
    return habitStore.habits.filter(routine => routine.categoryTitle === category)
    // return routinesArray.value.filter(routine => routine.category === category);
};

// 카테고리 선택
const selectCategory = (category) => {
    selectedCategory.value = category;
    renderExpenseChartHandler()
};

// 카테고리별 그룹화된 배열
const routineCategories = computed(() => {
    return [...new Set(habitStore.habits.map(routine => routine.categoryTitle))];
});

// 선택된 카테고리에 따른 지출 내역 필터링 및 날짜순 정렬
const filteredExpenses = computed(() => {
    if (!selectedCategory.value) return [];
    console.log('hi')
    if (selectedCategory.value === '전체') {
        return expenses
                .value.sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate));
    }
    console.log('hi2', selectedCategory)
    return expenses.value
        .filter(expense => expense.category === selectedCategory.value)
        .sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate)); // 날짜 내림차순으로 정렬
});

// 오늘의 지출 총액 계산
const totalExpenses = computed(() => {
    return expenses.value.reduce((total, expense) => total + expense.withdrawalAmount, 0);
});

// 오늘의 최다 지출에 대한 카테고리, 총지출, 비율 계산
const maxExpenseCategory = computed(() => {
    const maxCategory = Object.keys(expensesByCategory.value).reduce((max, category) => {
        return expensesByCategory.value[category] > expensesByCategory.value[max] ? category : max;
    }, Object.keys(expensesByCategory.value)[0]);

    return {
        maxCategory,
        maxAmount: expensesByCategory.value[maxCategory],
        maxPercentage: expensesByCategory.value[maxCategory] / totalExpenses.value * 100
    };
});

// 선택한 카테고리의 총지출, 비율 계산
const selectedCategoryExpenses = computed(() => {
    const amount = expensesByCategory.value[selectedCategory.value] || 0;
    const percentage = (amount / totalExpenses.value) * 100;
    return { amount, percentage };
});

// 숫자를 원화로 변환
const formatCurrency = (value) => {
    return new Intl.NumberFormat('ko-KR').format(value) + '원';
};

// 시간을 HH:mm 형식으로 변환
const formatTime = (date) => {
    const d = new Date(date);
    return d.toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit',
    });
};

// 확률을 소수점 1자리까지 표시
const formatPercentage = (value) => {
    return value.toFixed(1) + '%';
};
</script>

<style scoped>
header {
    font-size: 2.5rem;
    font-weight: bold;
}

aside {
    /*background-color: #f8f9fa;*/
    width: 30%;
    max-width: 300px;
    min-height: 100vh;
}

.info {
    display: block;
    margin-bottom: 20px;
    color: gray;
    
}

section {
    max-height: 100%;
    min-width: 650px;
    
}

.table-bordered th,
.table-bordered td {
    text-align: center;
    
}

.category-block {
    cursor: pointer;
    
}

.selected-category {
    /*background-color: lightslategray;*/
    background-color: red;
    border: 0px;
    
}

.block {
    /* border: 1px solid #ddd; */
    border-radius: 0px;
    padding: 20px;
    background-color: #ffd7392b;
    margin-bottom: 30px;
}

.main-content {
    width: 70%;
    
}

i {
    font-size: 1.2rem; /* 아이콘 크기 */
}

.me-2 {
    margin-right: 8px; /* 텍스트와 아이콘 간 간격 */
}

.card-container {
    padding: 20px;
}

.text-container ul {
list-style-type: none;
padding: 0;
margin: 0;
}

.text-container li {
font-size: 16px;
margin-bottom: 10px;
display: flex;
align-items: center;
}

.text-container .icon {
width: 24px;
margin-right: 15px;
}

.text-container strong {
font-weight: 600;
color: #333; /* 강조 색상 */
font-size: large;
}

.text-container span {
font-size: 18px;
color: #777; /* 일반 텍스트 색상 */
}


.chart-box {
    min-width: 280px;
    min-height: 280px;
    width: 30%;
    height: 30%;
}

.list-group {
    border-radius: 0px;
}




/* 인포 */
.info {
    margin: 6% 8%;
    color: black;
}

.info h1{
    font-weight: 800;
}

.info p {
    /* font-weight: 700; */
    margin-top: 25px;
}


.h4 {
    font-weight: 700;
}

.reset {
    font-size: 13px;
}
.reset i {
    font-size: 13px;
    margin-right: 2px;
}

.h6 {
    /* 카테고리 이름 */
    background-color: black;
    color: white;
    padding: 4px 8px;
    font-size: 13px;
    
}
</style>
