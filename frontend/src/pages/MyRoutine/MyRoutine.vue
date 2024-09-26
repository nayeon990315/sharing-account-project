<template>
    <div class="container">
        <!-- Header -->
        <!-- <header class="text-center my-4">
            <h1>K-Bee</h1>
        </header> -->

        <div class="row">
            <!-- Left Sidebar (Routine Category) -->
            <aside class="col-md-3 bg-light p-3">
                <h2 class="h5">ROUTINE CATEGORY</h2>
                <div v-for="(category, index) in routineCategories" :key="index" class="mb-3 category-block"
                    :class="{ 'selected-category': selectedCategory === category }" @click="selectCategory(category)">
                    <h4 class="h6">{{ category }}</h4>
                    <ul class="list-group">
                        <li v-for="routine in filteredRoutines(category)" :key="routine.routine_name"
                            class="list-group-item">
                            {{ routine.routine_name }}
                        </li>
                    </ul>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="col-md-9">
                <div class="row">
                    <!-- Daily Report Section -->
                    <section class="col-md-12">
                        <h2>DAILY REPORT</h2>
                        <div class="d-flex border p-3">
                            <div class="text-center bg-secondary mb-3" style="width: 150px; height: 150px;">
                                차트 영역
                            </div>
                            <ul>
                                <li>오늘 총 지출: {{ formatCurrency(totalExpenses) }}원</li>
                                <li>가장 지출이 많은 카테고리는 {{ maxExpenseCategory.maxCategory }}입니다.</li>
                                <li>오늘 총 지출의 {{ formatPercentage(maxExpenseCategory.maxPercentage) }}
                                    ({{formatCurrency(maxExpenseCategory.maxAmount) }}원) 입니다.</li>
                                <li v-if="selectedCategory"></li>
                                <li v-if="selectedCategory">선택하신 카테고리는 {{ selectedCategory }}입니다.</li>
                                <li v-if="selectedCategory">오늘 총 지출의 {{ formatPercentage(selectedCategoryExpenses.percentage) }}
                                    ({{ formatCurrency(selectedCategoryExpenses.amount) }}원)입니다.</li>
                            </ul>
                        </div>
                    </section>

                    <!-- Spending Details Section -->
                    <section class="col-md-12 mt-4">
                        <h2>SPENDING DETAILS</h2>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>시간</th>
                                    <th>지출 내역</th>
                                    <th>지출액</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-if="!selectedCategory">
                                    <td colspan="3" class="text-center">카테고리를 고르세요</td>
                                </tr>
                                <tr v-else v-for="(expense, index) in filteredExpenses" :key="index">
                                    <td>{{ formatTime(expense.transactionDate) }}</td>
                                    <td>{{ expense.recipient }}</td>
                                    <td>{{ expense.withdrawalAmount }}원</td>
                                </tr>
                            </tbody>
                        </table>
                    </section>
                </div>
            </main>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            routinesArray: [
                {
                    "routine_name": "아침 식사 준비",
                    "category": "식비"
                },
                {
                    "routine_name": "저녁 외식",
                    "category": "식비"
                },
                {
                    "routine_name": "아침 커피 한 잔",
                    "category": "카페/간식"
                },
                {
                    "routine_name": "오후 간식 타임",
                    "category": "카페/간식"
                },
                {
                    "routine_name": "월간 필수 아이템 구매",
                    "category": "온라인쇼핑"
                },
                {
                    "routine_name": "할인 쿠폰으로 쇼핑하기",
                    "category": "온라인쇼핑"
                },
                {
                    "routine_name": "화장품 꼭 필요한 것만 사기",
                    "category": "뷰티"
                }
            ],
            expenses: [
                {
                    transactionDate: '2024-06-26T18:17',
                    type: '체크카드',
                    category: '식비',
                    recipient: '사나이뚝배기화곡역',
                    withdrawalAmount: 7800,
                    depositAmount: 0,
                    balance: 691,
                },
                {
                    transactionDate: '2024-06-26T14:39',
                    type: '체크카드',
                    category: '카페/간식',
                    recipient: '애크로매틱어린이대',
                    withdrawalAmount: 3500,
                    depositAmount: 0,
                    balance: 8491,
                },
                {
                    transactionDate: '2024-06-26T13:10',
                    type: '체크카드',
                    category: '카페/간식',
                    recipient: '매머드커피앱_발트_1',
                    withdrawalAmount: 2000,
                    depositAmount: 0,
                    balance: 11991,
                },
                {
                    transactionDate: '2024-06-26T12:50',
                    type: '체크카드',
                    category: '식비',
                    recipient: '홈플러스익스프레스',
                    withdrawalAmount: 2590,
                    depositAmount: 0,
                    balance: 13991,
                },
                {
                    transactionDate: '2024-06-26T12:00',
                    type: '체크카드',
                    category: '식비',
                    recipient: '맘스터치화곡점',
                    withdrawalAmount: 9800,
                    depositAmount: 0,
                    balance: 16581,
                },
                {
                    transactionDate: '2024-06-26T09:11',
                    type: '체크카드',
                    category: '카페/간식',
                    recipient: '메가엠지씨커피우장',
                    withdrawalAmount: 2000,
                    depositAmount: 0,
                    balance: 26381,
                },
                {
                    transactionDate: '2024-06-26T09:31',
                    type: '체크카드',
                    category: '뷰티',
                    recipient: '블랭크유',
                    withdrawalAmount: 74000,
                    depositAmount: 0,
                    balance: 28381,
                }
            ],
            selectedCategory: null
        };
    },

    computed: {
        // 카테고리별로 그룹화된 배열 생성
        routineCategories() {
            return [...new Set(this.routinesArray.map(routine => routine.category))];
        },
        // 선택된 카테고리에 따른 지출 내역 필터링 및 날짜순 정렬
        filteredExpenses() {
            if (!this.selectedCategory) return [];
            return this.expenses
                .filter(expense => expense.category === this.selectedCategory)
                .sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate)); // 날짜 내림차순으로 정렬
        },
        // 오늘의 지출 총액을 계산
        totalExpenses() {
            return this.expenses.reduce((total, expense) => total + expense.withdrawalAmount, 0);
        },
        // 카테고리별 지출총액 합
        expensesByCategory() {
            return this.expenses.reduce((acc, expense) => {
                if (!acc[expense.category]) {
                    acc[expense.category] = 0;
                }
                acc[expense.category] += expense.withdrawalAmount;
                return acc;
            }, {});
        },
        // 오늘의 최다 지출에 대한 카테고리, 총지출, 확률을 계산
        maxExpenseCategory() {
            const maxCategory = Object.keys(this.expensesByCategory).reduce((max, category) => {
                return this.expensesByCategory[category] > this.expensesByCategory[max] ? category : max;
            }, Object.keys(this.expensesByCategory)[0]);

            return {
                maxCategory,
                maxAmount: this.expensesByCategory[maxCategory],
                maxPercentage: this.expensesByCategory[maxCategory] / this.totalExpenses * 100
            };
        },
        // 선택한 카테고리의 총지출, 확률을 계산
        selectedCategoryExpenses() {
            const amount = this.expensesByCategory[this.selectedCategory] || 0;
            const percentage = (amount / this.totalExpenses) * 100;
            return { amount, percentage };
        }
    },

    methods: {
        // 카테고리별로 루틴 필터링
        filteredRoutines(category) {
            return this.routinesArray.filter(routine => routine.category === category);
        },
        // 카테고리 선택
        selectCategory(category) {
            this.selectedCategory = category;
        },
        // Spring의 Date타입(Timestamp)를 HH:mm로 변환
        formatTime(date) {
            const d = new Date(date);
            return d.toLocaleTimeString('ko-KR', {
                hour: '2-digit',
                minute: '2-digit',
            });
        },
        // 숫자를 한국식 원화로 변환
        formatCurrency(value) {
            return new Intl.NumberFormat('ko-KR').format(value);
        },
        // 확률을 소수점 1번째 자리까지 표시
        formatPercentage(value) {
            return value.toFixed(1) + '%';
        }
    }
};
</script>

<style scoped>
header {
    font-size: 2.5rem;
    font-weight: bold;
}

aside {
    background-color: #f8f9fa;
}

.table-bordered th,
.table-bordered td {
    text-align: center;
}

.category-block {
    cursor: pointer;
}

.selected-category {
    background-color: #d1e7dd;
}
</style>