<template>
  <div class="container">
    <h1>분류별 지출</h1>
    <Doughnut :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Doughnut } from 'vue-chartjs';

// Chart.js의 구성 요소를 등록합니다.
ChartJS.register(Title, Tooltip, Legend, ArcElement);

export default {
  name: 'ExpenseChart',
  components: {
    Doughnut,
  },
  data() {
    return {
      chartData: {
        labels: [
          '카페/간식',
          '쇼핑',
          '교통',
          '식비',
          '문화/여가',
          '술/유흥',
          '주거/공과금',
          '기타',
        ],
        datasets: [
          {
            data: [5000, 12000, 7000, 23000, 15000, 8000, 20000, 5000],
            backgroundColor: [
              '#FF6384',
              '#36A2EB',
              '#FFCE56',
              '#9966FF',
              '#4BC0C0',
              '#FF9F40',
              '#FFC300',
              '#DAF7A6',
            ],
          },
        ],
      },
      chartOptions: {
        responsive: true,
        plugins: {
          legend: { position: 'top' },
        },
      },
    };
  },
};
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: auto;
}
</style>
