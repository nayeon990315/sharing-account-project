<template>
    <div class="calendar">
      <h2 class="calendar-title">
        <button @click="prevMonth" class="month-button">&lt;</button>
        <span @click="toggleMonthSelector">{{ currentMonth }}</span>
        <button @click="nextMonth" class="month-button">&gt;</button>
      </h2>
      <div v-if="monthSelectorVisible" class="month-selector">
        <select v-model="selectedMonth" @change="changeMonth">
          <option v-for="(month, index) in months" :key="index" :value="index">
            {{ month }}
          </option>
        </select>
        <select v-model="selectedYear" @change="changeYear">
          <option v-for="year in years" :key="year" :value="year">
            {{ year }}년
          </option>
        </select>
      </div>
      <div class="calendar-grid">
        <div class="calendar-header">
          <div v-for="(day, index) in daysOfWeek" :key="index" class="calendar-header-cell">
            {{ day }}
          </div>
        </div>
        <div class="calendar-body">
          <div v-for="(week, weekIndex) in weeks" :key="weekIndex" class="calendar-week">
            <div
              v-for="(day, dayIndex) in week"
              :key="dayIndex"
              class="calendar-day"
              :class="{
                holiday: day.date.getDay() === 0 || isHoliday(day.date),
                saturday: day.date.getDay() === 6
              }"
            >
              <div class="day-number">{{ day.date.getDate() }}</div>
              <div class="day-events">
                <div v-if="day.events.length > 0" class="event">
                  <span class="event-count" @click="openModal(day)">루틴 {{ day.events.length }}개 달성</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <div v-if="eventDetailsVisible" class="modal">
        <div class="modal-content">
          <h4 id="modal-title">{{ formatDate(hoveredDay.date) }} 달성 루틴</h4><br>
          <table>
            <tbody>
              <tr v-for="(event, index) in selectedDayEvents" :key="index">
                <td>{{ event.title }}</td>
                <td>{{ event.save }}원</td>
              </tr>
            </tbody>
          </table>
          <button class="close-btn" @click="closeModal">닫기</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, watch } from 'vue';
  
  const props = defineProps({
    events: {
      type: Array,
      required: true,
    },
  });
  
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const weeks = ref([]);
  const currentMonth = ref('');
  const currentYear = ref(new Date().getFullYear());
  const currentMonthIndex = ref(new Date().getMonth());
  const monthSelectorVisible = ref(false);
  const months = [
    '1월', '2월', '3월', '4월', '5월', '6월',
    '7월', '8월', '9월', '10월', '11월', '12월'
  ];
  const selectedMonth = ref(currentMonthIndex.value);
  const selectedYear = ref(currentYear.value);
  const years = Array.from({ length: 21 }, (_, i) => currentYear.value - 10 + i);
  const selectedDayEvents = ref([]); 
  const eventDetailsVisible = ref(false); 
  const hoveredDay = ref(null); 
  
  const toggleMonthSelector = () => {
    monthSelectorVisible.value = !monthSelectorVisible.value;
  };
  
  const changeMonth = () => {
    currentMonthIndex.value = selectedMonth.value;
    createCalendar(props.events);
  };
  
  const changeYear = () => {
    currentYear.value = selectedYear.value;
    createCalendar(props.events);
  };
  
  const createCalendar = (events) => {
    const startDate = new Date(currentYear.value, currentMonthIndex.value, 1);
    const endDate = new Date(currentYear.value, currentMonthIndex.value + 1, 0);
  
    currentMonth.value = `${startDate.getFullYear()}년 ${startDate.getMonth() + 1}월`;
  
    const startDay = startDate.getDay();
  
    const totalDays = Array.from({ length: 35 }, (_, i) => {
      const date = new Date(startDate);
      date.setDate(i - startDay + 1);
      return { date, events: [] };
    });
  
    events.forEach(event => {
      const eventDate = new Date(event.date);
      const eventDay = totalDays.find(d =>
        d.date.getFullYear() === eventDate.getFullYear() &&
        d.date.getMonth() === eventDate.getMonth() &&
        d.date.getDate() === eventDate.getDate()
      );
      if (eventDay) {
        eventDay.events.push({
          title: event.title,
          save: event.save,
        });
      }
    });
  
    weeks.value = [];
    for (let i = 0; i < totalDays.length; i += 7) {
      weeks.value.push(totalDays.slice(i, i + 7));
    }
  };
  
  const openModal = (day) => {
    selectedDayEvents.value = day.events;
    eventDetailsVisible.value = true;
    hoveredDay.value = day;
  };
  
  const closeModal = () => {
    eventDetailsVisible.value = false;
    hoveredDay.value = null;
  };
  
  const prevMonth = () => {
    currentMonthIndex.value--;
    if (currentMonthIndex.value < 0) {
      currentMonthIndex.value = 11;
      currentYear.value--;
    }
    createCalendar(props.events);
  };
  
  const nextMonth = () => {
    currentMonthIndex.value++;
    if (currentMonthIndex.value > 11) {
      currentMonthIndex.value = 0;
      currentYear.value++;
    }
    createCalendar(props.events);
  };
  
  const isHoliday = (date) => {
    const holidays = [
      new Date(date.getFullYear(), 0, 1), // 1월 1일 공휴일로 설정
      // 다른 공휴일 추가
    ];
    return holidays.some(holiday => holiday.toDateString() === date.toDateString());
  };

  const formatDate = (date) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('ko-KR', options);
  };
  
  watch(() => props.events, (newEvents) => {
    createCalendar(newEvents);
  }, { immediate: true });
  </script>
  
  <style scoped>
  .calendar {
    margin: 20px;
    border-radius: 0;
    overflow: hidden;
    border: 2px solid #ddd;
  }
  
  .calendar-title {
    text-align: center;
    font-size: 20px;
    font-weight: bold;
    background-color: #ffd7399a
    ; 
    padding: 15px 0;
    margin: 0;
  }
  
  .month-button {
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
  }
  
  .month-selector {
    text-align: center;
    margin: 10px 0;
  }
  
  select {
    margin: 0 5px; 
  }
  
  .calendar-grid {
    display: flex;
    flex-direction: column;
  }
  
  .calendar-header {
    display: flex;
    background-color: #ffd7395e; 
  }
  
  .calendar-header-cell {
    flex: 1;
    text-align: center;
    padding: 10px;
    border-bottom: 2px solid #ddd; 
    font-weight: bold; 
  }
  
  .calendar-body {
    display: flex;
    flex-direction: column;
  }
  
  .calendar-week {
    display: flex;
  }
  
  .calendar-day {
    flex: 1;
    border-bottom: 1px solid #ddd; 
    padding: 10px;
    position: relative;
    cursor: pointer; 
    height: 100px;
  }
  
  .saturday {
    color: #007bff; 
  }
  
  .holiday {
    color: #dc3545; 
  }
  
  .day-number {
    font-size: 18px;
    font-weight: bold;
  }
  
  .event-count {
    cursor: pointer; 
    font-size: 14px;
  }
  
  .modal {
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.7);
    z-index: 1000; 
  }
  
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    width: 500px; 
  }

  #modal-title {
    text-align: center;
  }

  .modal table {
    width: 100%;
    margin-bottom: 30px;
    border-top: 1px solid #ddd;
  }

  .modal table td {
    padding: 12px 15px; 
    border-bottom: 1px solid #ddd; 
    color: #333;
  }

  .modal table tr:nth-child(even) {
    background-color: #f2f2f2; 
  }

  .close-btn {
    padding: 10px;
    border-radius: 0;
    /* border: 3px solid #6c757d; */
    border: none;
    background-color: black;
    color: white;
  }

  .close-btn:hover {
    color: white;
    background-color: #6c757d;
  }
  
  </style>
  