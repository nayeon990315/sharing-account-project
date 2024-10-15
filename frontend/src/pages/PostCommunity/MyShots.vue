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
  background-color: white;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 3px solid black;

}

.calendar-section{
  height: 100%;
  overflow: hidden; /* 부모 영역을 넘어가는 것을 방지 */
  display: flex; /* 자식 요소가 부모의 크기에 맞추도록 설정 */
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

.header h2 {
  margin: 0;
  text-align: left;
}
</style>
