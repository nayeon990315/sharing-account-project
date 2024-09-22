<template>
    <div class="chart-container">
        <Bar v-if="loaded" :options="chartOptions" :data="chartData" />
    </div>
</template>

<script>
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import { ref, onMounted } from 'vue'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
    name: 'BarChart',
    components: { Bar },
    data() {
        return {
            expensesToday: [
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
                    transactionDate: '2024-09-22T15:20',
                    type: '체크카드',
                    category: '뷰티',
                    recipient: '올리브영홍대입구',
                    withdrawalAmount: 12000,
                    depositAmount: 0,
                    balance: 30400
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
                    transactionDate: '2024-09-22T19:30',
                    type: '체크카드',
                    category: '영화/문화',
                    recipient: 'CGV용산',
                    withdrawalAmount: 12000,
                    depositAmount: 0,
                    balance: 400
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
            ],
            expensesPast: [
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
            loaded: false,
            chartData: {
                labels: [], // 시간대
                datasets: [
                    {
                        label: '오늘',
                        data: [], // 오늘의 3시간 간격 지출 금액
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    },
                    {
                        label: '과거',
                        data: [], // 과거의 3시간 간격 지출 금액
                        backgroundColor: 'rgba(153, 102, 255, 0.6)',
                        borderColor: 'rgba(153, 102, 255, 1)',
                        borderWidth: 1
                    }
                ]
            },
            chartOptions: {
                responsive: true,
                scales: {
                    x: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: '시간 (3H)',
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: '지출 금액 (KRW)',
                        }
                    }
                }
            }
        };
    },
    methods: {
        // Backend API -> 선택한 날짜의 지출 긁어오기 (현재 & 과거)
        groupByTime(expenses) {
            const groupedData = Array(8).fill(0); // 3시간씩 24시간 나누면 8구간
            expenses.forEach(expense => {
                const hour = new Date(expense.transactionDate).getHours();
                const timeIndex = Math.floor(hour / 3); // 3시간 단위로 그룹화
                groupedData[timeIndex] += expense.withdrawalAmount;
            });
            return groupedData;
        }
    },
    mounted() {
        const todayData = this.groupByTime(this.expensesToday);
        const pastData = this.groupByTime(this.expensesPast);

        this.chartData.labels = [
            '0:00 - 3:00', '3:00 - 6:00', '6:00 - 9:00', '9:00 - 12:00',
            '12:00 - 15:00', '15:00 - 18:00', '18:00 - 21:00', '21:00 - 24:00'
        ];

        this.chartData.datasets[0].data = todayData;
        this.chartData.datasets[1].data = pastData;

        this.loaded = true;
    }
}
</script>
<style scoped>
.chart-container {
  width: 100%; /* 가로로 전체를 차지하도록 설정 */
  height: 400px; /* 차트의 높이를 설정 */
  display: flex;
  justify-content: center;
  align-items: center;
}

canvas {
  width: 70% !important; /* 차트의 가로 크기를 70%로 고정 */
  height: auto !important; /* 차트의 세로 크기를 자동으로 설정 */
}
</style>
