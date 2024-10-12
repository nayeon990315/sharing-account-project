<template>
    <div class="container">
        <h1>Daily Report</h1>

        <!-- Today's Expense Report Section -->
        <div class="report-section block">
            <h2 class="section-title">오늘의 소비 내역 리포트</h2>
            <p class="description">
                오늘의 소비를 카테고리별로 확인하고, 달성률을 %로 한눈에 확인하세요.
            </p>
            <div class="flex-container expense-container">
                <div class="report-graph">
                    <canvas id="expenseChart"></canvas>
                </div>
                <ul class="category-list">
                    <li v-for="(amount, category, index) in expensesByCategory" :key="category">
                        <span :style="{
                            backgroundColor: backgroundColors[index],
                            width: '25px',
                            height: '15px',
                            display: 'inline-block',
                            marginRight: '15px',
                        }"></span>
                        <span class="category-text">{{ category }}: {{ formatCurrency(amount) }}</span>
                    </li>
                </ul>
            </div>
        </div>

        <!-- Savings & Routine Report Section -->
        <div class="report-section block">
            <h2 class="section-title">오늘의 루틴 & 절약 리포트</h2>
            <p class="description">
                루틴 달성 정도를 확인하고, 카테고리별 절약 목표와 달성 금액을
                비교해보세요.
            </p>
            <div class="flex-container graph-right">
                <canvas id="savingsChart"></canvas>
            </div>
        </div>

        <!-- Details Section -->
        <div class="block">
            <h2 class="section-title">Details</h2>
            <div class="flex-container details-container">
                <ul class="routine-list">
                    <li><input type="checkbox" /> 루틴 1</li>
                    <li><input type="checkbox" /> 루틴 2</li>
                    <li><input type="checkbox" /> 루틴 3</li>
                </ul>
                <div class="details-list">관련 지출 내역 리스트</div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import { useHabitStore } from '@/stores/habitStore';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export default {
    setup() {
        const storedUserId = localStorage.getItem('userId');
        if (storedUserId) {
            const habitStore = useHabitStore()
            habitStore.getHabitsFromServer(storedUserId);
        }
    },
    data() {
        return {
            expenses: [
                { category: '카페/간식', amount: 5000 },
                { category: '쇼핑', amount: 12000 },
                { category: '교통', amount: 7000 },
                { category: '식비', amount: 23000 },
                { category: '문화/여가', amount: 15000 },
                { category: '술/유흥', amount: 8000 },
                { category: '주거/공과금', amount: 20000 },
                { category: '기타', amount: 5000 },
            ],
            targets: [10000, 15000, 8000, 25000, 18000, 10000, 22000, 6000],
            chart: null,
            backgroundColors: [
                '#FF6384',
                '#36A2EB',
                '#FFCE56',
                '#9966FF',
                '#4BC0C0',
                '#FF9F40',
                '#FFC300',
                '#DAF7A6',
            ],
        };
    },
    mounted() {
        this.getUserIdFromToken();
        this.renderExpenseChart();
        this.renderSavingsChart();
    },
    computed: {
        expensesByCategory() {
            return this.expenses.reduce((acc, expense) => {
                if (!acc[expense.category]) acc[expense.category] = 0;
                acc[expense.category] += expense.amount;
                return acc;
            }, {});
        },
    },
    methods: {
            renderExpenseChart() {
                const ctx = document.getElementById('expenseChart').getContext('2d');
                if (!ctx) return;

                const categories = Object.keys(this.expensesByCategory);
                const amounts = Object.values(this.expensesByCategory);

                if (this.chart) this.chart.destroy();

                this.chart = new Chart(ctx, {
                    type: 'doughnut',
                    data: {
                        labels: categories,
                        datasets: [
                            {
                                data: amounts,
                                backgroundColor: this.backgroundColors,
                                hoverOffset: 4,
                            },
                        ],
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: { display: false },
                        },
                    },
                });
            },
            renderSavingsChart() {
                const ctx = document.getElementById('savingsChart').getContext('2d');
                if (!ctx) return;

                const categories = Object.keys(this.expensesByCategory);
                const amounts = Object.values(this.expensesByCategory);

                new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: categories,
                        datasets: [
                            {
                                label: '목표 금액',
                                data: this.targets,
                                backgroundColor: '#cccccc',
                            },
                            {
                                label: '달성 금액',
                                data: amounts,
                                backgroundColor: this.backgroundColors,
                            },
                        ],
                    },
                    options: {
                        indexAxis: 'y', // Horizontal bar chart
                        responsive: true,
                        plugins: {
                            legend: { position: 'top' },
                        },
                        scales: {
                            xAxes: [
                                {
                                    ticks: { beginAtZero: true },
                                },
                            ],
                        },
                    },
                });
            },
            formatCurrency(value) {
                return new Intl.NumberFormat('ko-KR').format(value) + '원';
            },
            async getUserIdFromToken() {
                const jwtToken = this.$cookies.get('jwtToken');
                if (!jwtToken) {
                    alert('로그인이 필요합니다!');
                    this.$router.push('/login'); // Vue Router를 사용하여 리다이렉트
                    return;
                }
                console.log(jwtToken); try {
                    // axios를 이용해 백엔드의 findId 컨트롤러에 JWT 토큰을 전송
                    const response = await axios.post('http://localhost:8080/users/findId', {}, {
                        headers: {
                            'Authorization': `Bearer ${jwtToken}` // JWT 토큰을 Authorization 헤더에 추가
                        }
                    });
                    console.log(response.data)
                    // 응답으로 받은 userId를 localStorage에 저장
                    const userId = response.data.userId;
                    const nickname = response.data.nickname;
                    localStorage.setItem('userId', userId);
                    localStorage.setItem('nickname', nickname);
                    console.log('사용자 ID가 localStorage에 저장되었습니다:', userId);
                } catch (error) {
                    console.error('사용자 정보를 가져오지 못했습니다:', error);
                    if (error.response && error.response.status === 401) {
                        alert('인증 오류: 로그인이 필요합니다.');
                        this.$router.push('/login');
                    }
                }
            }
        },
}
</script>

<style scoped>
.container {
    max-width: 1200px;
    /* 전체 컨테이너 크기 */
    margin: 40px auto;
    /* Daily Report 아래에 공백 추가 */
    padding: 20px;
    font-family: Arial, sans-serif;
    line-height: 1.6;
}

.section-title {
    font-size: 1.6em;
    font-weight: bold;
    color: #333;
    margin-bottom: 15px;
}

.description {
    margin-bottom: 20px;
    color: #666;
}

.report-section {
    margin-bottom: 50px;
}

.flex-container {
    display: flex;
    justify-content: space-around;
    align-items: flex-start;
}

.graph-right {
    justify-content: flex-end;
    /* 그래프를 오른쪽으로 이동 */
}

.details-container {
    flex-direction: column;
}

.report-graph {
    flex-grow: 1;
}

canvas {
    max-width: 500px;
    /* 그래프 크기 증가 */
    max-height: 500px;
}

.category-list,
.routine-list {
    list-style-type: none;
    padding-left: 0;
}

.category-list li,
.routine-list li {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    /* 항목 간의 간격을 넓힘 */
}

.category-text {
    font-size: 1.2em;
    /* 카테고리 텍스트의 크기를 증가 */
}

.details-list {
    margin-top: 20px;
}

/* 모서리가 둥근 블럭 스타일 */
.block {
    border: 1px solid #ddd;
    border-radius: 10px;
    padding: 20px;
    background-color: #f9f9f9;
    margin-bottom: 30px;
}
</style>