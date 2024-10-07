<template>
  <div class="container">
    <h1>Daily Report</h1>

    <div class="report-section">
      <h2>오늘의 소비 내역 리포트</h2>
      <p>루틴 형성 정도를 한눈에 볼 수 있습니다.</p>
      <div class="flex-container">
        <div class="report-graph">리포트 그래프</div>
        <ul class="category-list">
          <li>식비</li>
          <li>카페/간식</li>
          <li>쇼핑</li>
          <li>교통</li>
          <li>문화/여가</li>
        </ul>
      </div>
    </div>

    <div class="report-section">
      <h2>오늘의 루틴 & 절약 리포트</h2>
      <p>루틴 형성 정도를 한눈에 볼 수 있습니다.</p>
      <div class="flex-container">
        <div class="report-graph">리포트 그래프</div>
        <!-- 추가적인 내용 -->
      </div>
    </div>

    <h2>Details</h2>
    <div class="flex-container">
      <ul class="routine-list">
        <li><input type="checkbox" /> 루틴 1</li>
        <li><input type="checkbox" /> 루틴 2</li>
        <li><input type="checkbox" /> 루틴 3</li>
        <!-- 추가 항목 -->
      </ul>
      <div class="details-list">관련 지출내역 리스트</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 필요한 데이터 추가
    };
  },
};
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
}
.report-section {
  margin-bottom: 40px;
}
.flex-container {
  display: flex;
  justify-content: space-between;
}
.report-graph,
.details-list {
  width: 48%;
  height: 200px;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.category-list,
.routine-list {
  width: 48%;
  list-style-type: none; /* 리스트 아이콘 제거 */
}
.details-list {
  background-color: #f9f9f9;
}
</style>
