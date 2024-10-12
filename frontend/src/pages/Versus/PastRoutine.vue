<template>
    <div id="past-routine" class="col-md-6 mb-4">
      <div class="card shadow-sm">
        <div class="card-body">
          <h3 class="card-title text-center">과거의 루틴</h3>
          <ul v-if="showPastRoutines" class="list-group list-group-flush routine-list">
            <li v-if="routinesPast.length === 0" class="list-group-item text-center">
              과거에 달성한 루틴이 없습니다.
            </li>
            <li v-for="(routine, index) in routinesPast" :key="index" class="list-group-item text-center">
              {{ routine.routine_name }} <span class="badge bg-secondary">{{ routine.category }}</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'PastRoutine',
    props: {
      routinesPast: Array,
      showPastRoutines: {
        type: Boolean,
        default: false 
      }
    }
  };
  </script>
  
  <style scoped>
  .card {
      border-radius: 10px;
      overflow: hidden;
  }
  
  .card-title {
      font-size: 1.5em;
      font-weight: bold;
      margin-bottom: 10px;
  }
  
  .card-body {
    min-height: 300px;
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
      max-height: 200px;
      overflow-y: auto;
  }
  </style>