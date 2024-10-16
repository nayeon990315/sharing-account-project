<template>
    <div id="past-routine" class="col-md-6 mb-4">
      <div class="card shadow-sm">
        <div class="card-body">
          <p class="card-title text-center">달성한 루틴</p>
          <ul v-if="showPastRoutines" class="list-group list-group-flush routine-list">
            <li v-if="routinesPast.length === 0" class="list-group-item text-center">
              과거에 달성한 루틴이 없습니다
            </li>
            <li v-for="(routine, index) in routinesPast" :key="index" class="list-group-item text-center">
              <span class="badge bg-secondary">{{ routine.category }}</span>{{ routine.routine_name }} 
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
  #past-routine {
    width: 210px;
    /* border: none; */
    height: 160px;
  }

  .card {
      border-radius: 10px;
      overflow: hidden;
      border: none;
      border-radius: 0;
  }
  
  .card-title {
      font-weight: 600;
      margin-bottom: 10px;
      color: rgb(89, 57, 0);
  }
  
  .card-body {
    height: 170px;
    margin: 0;
    overflow-y: auto;
  }
  
  .list-group-item {
      font-size: 14px;
      padding: 0;
      text-align: center;
      color: rgb(89, 57, 0);
  }
  
  .badge {
      font-size: 0.8em;
      background-color: rgb(89, 57, 0) !important ;
      color: white;
      border-radius: 0;
      margin-right: 3px;
  }
  
  .routine-list {
      max-height: 200px;
      overflow-y: auto;
  }
  </style>