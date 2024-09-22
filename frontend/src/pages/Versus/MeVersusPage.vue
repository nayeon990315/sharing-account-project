<template>
    <div class="versus-container">
      <div id="chart" class="half">
        <BarChart />
      </div>
      <div id="habit" class="half">
        <h2>hihi</h2>
      </div>
    </div>
  </template>
  
  <script>
  import BarChart from './BarChart.vue';
  
  export default {
    name: 'MeVersusPage',
    components: { BarChart }
  }
  </script>
  
  <style scoped>
  .versus-container {
    display: flex;
    flex-direction: column; /* 세로로 배치 */
    width: 100%;
    height: 100vh; /* Viewport height, so it takes the full height of the screen */
  }
  
  .half {
    width: 100%; /* 가로로는 꽉 채우고 */
    height: 50%; /* 세로로 화면의 절반씩 차지 */
    display: flex;
    justify-content: center; /* Center horizontally */
    align-items: center; /* Center vertically */
    padding: 10px;
    box-sizing: border-box; /* Include padding in width/height calculation */
  }
  
  #chart {
    background-color: #f9f9f9;
  }
  
  #habit {
    background-color: #e3e3e3;
  }
  </style>
  