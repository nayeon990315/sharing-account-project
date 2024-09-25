<template>
    <div class="chart-container">
        <Bar v-if="loaded" :options="chartOptions" :data="chartData" />
    </div>
</template>

<script>
import { Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';
import { ref, computed, watch, onMounted } from 'vue';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

export default {
    name: 'BarChart',
    components: { Bar },
    props: {
        expensesToday: {
            type: Object,
            required: true
        },
        expensesPast: {
            type: Object,
            required: true
        }
    },
    setup(props) {
        //loaded true일 경우 차트 렌더링
        const loaded = ref(false);

        //ChartData 셋팅
        //반응형을 위해 ref 선언
        const chartData = ref({
            labels: [
                '0:00 - 3:00', '3:00 - 6:00', '6:00 - 9:00', '9:00 - 12:00',
                '12:00 - 15:00', '15:00 - 18:00', '18:00 - 21:00', '21:00 - 24:00'
            ],
            datasets: [
                {
                    label: '오늘',
                    data: [],
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                },
                {
                    label: '과거',
                    data: [],
                    backgroundColor: 'rgba(153, 102, 255, 0.6)',
                    borderColor: 'rgba(153, 102, 255, 1)',
                    borderWidth: 1
                }
            ]
        });

        const chartOptions = ref({
            responsive: true,
            scales: {
                x: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: '시간 (3H)'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: '지출 금액 (KRW)'
                    }
                }
            }
        });

        // props로 전달받은 지출 내역 computed 속성으로

        const groupByTime = (expenses) => {
            const groupedData = Array(8).fill(0); // 3시간씩 24시간 나누면 8구간
            expenses.forEach(expense => {
                const hour = new Date(expense.transactionDate).getHours();
                const timeIndex = Math.floor(hour / 3); // 3시간 단위로 그룹화
                groupedData[timeIndex] += expense.withdrawalAmount;
            });
            return groupedData;
        };

       //chartdata 갱신
        const updateChart = () => {
            //오늘 데이터는 그려지나, 과거 데이터가 그려지지 않음
            //loaded.value = false로 갱신한 후에 true로 다시 강제로 렌더링하려 했지만 실패
            loaded.value = false;

            const todayData = groupByTime(props.expensesToday);
            const pastData = groupByTime(props.expensesPast);

            console.log('Processed Past Data:', pastData);

            chartData.value.datasets[0].data = todayData;
            chartData.value.datasets[1].data = pastData;
            console.log('Processed chart Data:',  chartData.value.datasets[0].data);

            // Set loaded back to true after data is updated
            loaded.value = true;
        };

        // Watch for changes in computed properties and trigger chart update
        // watch([props.expensesPast], () => {
        //     updateChart();
        // });

        // Initially update the chart when the component is mounted
        onMounted(() => {
            
            updateChart();
        });

        return {
            loaded,
            chartData,
            chartOptions
        };
    }
};
</script>

<style scoped>
.chart-container {
    width: 100%;
    height: 400px;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
