<template>
  <div class="container my-5">
    <div class="row">
      <div id="present-routine" class="col-md-6 mb-4">
        <div class="card shadow-sm">
          <div class="card-body">
            <h3 class="card-title text-center">오늘의 루틴</h3>
            <ul class="list-group list-group-flush routine-list">
              <!-- 오늘의 루틴 목록 표시 -->
              <li v-for="(routine, index) in routinesToday" :key="index" class="list-group-item text-center">
                {{ routine.routine_name }} <span class="badge bg-primary">{{ routine.category }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div id="past-routine" class="col-md-6 mb-4">
        <div class="card shadow-sm">
          <div class="card-body">
            <h3 class="card-title text-center">과거의 루틴</h3>
            <ul class="list-group list-group-flush routine-list">
              <!-- 과거의 루틴 목록 표시 -->
              <li v-for="(routine, index) in routinesPast" :key="index" class="list-group-item text-center">
                {{ routine.routine_name }} <span class="badge bg-secondary">{{ routine.category }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RoutinesList',
  props: {
      routinesToday: Array,
      routinesPast: Array
  }
};
</script>

<style scoped>
.card {
    border-radius: 15px;
    overflow: hidden;
}

.card-title {
    font-size: 1.5em;
    font-weight: bold;
    margin-bottom: 10px;
}

.card-body {
    max-height: 250px; /* 스크롤이 적용되는 본문의 최대 높이 확대 */
    /* 스크롤바 활성화 */
}

.list-group-item {
    font-size: 1.1em;
    padding: 10px;
    text-align: center;
}

.badge {
    font-size: 0.9em;
}

.routine-list {
    max-height: 200px; /* 스크롤바 영역을 더 크게 확대 */
    overflow-y: auto;
}
</style>