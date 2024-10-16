<template>
  <div id="my-shots">
    <!-- <div class="header">
      <h2>내 사진</h2>
    </div> -->

    <!-- MyShotCalendar 컴포넌트를 사용 -->
     <div class ="calendar-section">
      <MyShotCalendar />
     </div>
  </div>
</template>

<script setup>
import MyShotCalendar from '@/components/PostCommunity/MyShotCalendar.vue'; // MyShotCalendar 컴포넌트를 가져옴
</script>

<style scoped>
#my-shots {
  padding: 20px;
  margin: 0 auto;
  /* 가로 중앙 정렬 */
  max-width: 80%;
  /* 전체 너비를 제한하여 비율 유지 */
  height: 90vh;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 3px solid black;
  background-color: #fffbee;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 20px;
  justify-content: flex-start; /* 왼쪽 정렬 */
}

.calendar-section{
  height: 100%;
  overflow: hidden; /* 부모 영역을 넘어가는 것을 방지 */
  display: flex; /* 자식 요소가 부모의 크기에 맞추도록 설정 */
}
.category-tags button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 7px 15px;
  border-radius: 0px;
  border: none;
  background-color: #f7d794;
  color: #333;
}


.header {
  display: flex;
  justify-content: space-between;
  /* 좌우 정렬 */
  align-items: center;
  /* 세로 정렬 */
  margin-bottom: 20px;
  /* 아래에 공간 추가 */
  position: relative;
}

.category-tags button.active {
  background-color: #000000; /* 활성화 색상 */
  color: #fff;
}

.category-tags button:hover {
  background-color: #fa983a; /* 호버 색상 */
  color: #fff;
}

.routine-calendar {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 0px;
  margin-bottom: 20px;
  /* 아래에 공간 추가 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.routine-header {
  padding: 10px;
  border-radius: 0px;
  width: calc(100% / 3);
  position: relative;

  top: -10px;
  background-color: #f1a72f8d;
}



.header h2 {
  margin: 0;
  text-align: left;
}
.day-tile {
  aspect-ratio: 1/1;
  width: auto;
  position: relative;
  border-radius: 10px;
  background-color: #e0e0e0;
}
.day-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지 비율을 유지하며 잘 맞추기 */
}
</style>
