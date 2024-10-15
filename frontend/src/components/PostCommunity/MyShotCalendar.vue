<template>
  <div class="calendar">
    <h2 class="calendar-title">
      <button @click="prevMonth" class="month-button">&lt;</button>
      <span @click="toggleMonthSelector">{{ currentYear }}년 {{ months[currentMonthIndex] }}</span>
      <button @click="nextMonth" class="month-button">&gt;</button>
    </h2>

    <div v-if="monthSelectorVisible" class="month-selector">
      <select v-model="selectedYear" @change="changeYear">
        <option v-for="year in years" :key="year" :value="year">{{ year }}년</option>
      </select>
      <select v-model="selectedMonth" @change="changeMonth">
        <option v-for="(month, index) in months" :key="index" :value="index">{{ month }}</option>
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
              saturday: day.date.getDay() === 6,
              sunday: day.date.getDay() === 0
            }"
          >
            <div class="day-number">{{ day.date.getDate() }}</div>
            <div class="day-events">
              <img v-if="imagesByDate[day.date.getDate()]" :src="imagesByDate[day.date.getDate()]" alt="image" class="day-image" />
              <img v-else src="/src/assets/꿀벌X2.webp" alt="default image" class="day-image small-default-image" />
              <div v-if="day.events.length > 0" class="event">
                <span class="event-count">습관 {{ day.events.length }}개 달성</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

  
  <script setup>
  import { ref, watch } from 'vue';
  import axios from 'axios';
  const props = defineProps({
    events: {
      type: Array,
      default: () => [],
    },
  });
  
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const weeks = ref([]);
  const currentYear = ref(new Date().getFullYear());
  const currentMonthIndex = ref(new Date().getMonth());
  const selectedMonth = ref(currentMonthIndex.value);
  const selectedYear = ref(currentYear.value);
  const monthSelectorVisible = ref(false);
  const months = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];
  const years = Array.from({ length: 21 }, (_, i) => currentYear.value - 10 + i);
  const imagesByDate = ref({});
  
  const toggleMonthSelector = () => {
    monthSelectorVisible.value = !monthSelectorVisible.value;
  };
  
  const createCalendar = (events = []) => {
    const startDate = new Date(currentYear.value, currentMonthIndex.value, 1);
    const endDate = new Date(currentYear.value, currentMonthIndex.value + 1, 0);
  
    const startDay = startDate.getDay();
  
    const totalDays = Array.from({ length: 35 }, (_, i) => {
      const date = new Date(startDate);
      date.setDate(i - startDay + 1);
      return { date, events: [] };
    });
  
    if (events && Array.isArray(events)) {
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
    }
  
    weeks.value = [];
    for (let i = 0; i < totalDays.length; i += 7) {
      weeks.value.push(totalDays.slice(i, i + 7));
    }
  };
  
  const fetchImagesByDate = async () => {
    const userId = localStorage.getItem('userId');
    try {
      const response = await axios.get(`http://localhost:8080/post-community/images/most-liked`, {
        params: {
          userId,
          month: currentMonthIndex.value + 1,
          year: currentYear.value,
        },
      });
  
      const imageMap = {};
      response.data.forEach((imageData) => {
        const createdAt = new Date(imageData.createdAt);
        const imageDate = createdAt.getDate();
        imageMap[imageDate] = imageData.imageURL;
      });
  
      imagesByDate.value = imageMap;
    } catch (error) {
      console.error('이미지 로드 중 오류 발생:', error);
    }
  };
  
  const changeMonth = () => {
    currentMonthIndex.value = selectedMonth.value;
    createCalendar(props.events);
    fetchImagesByDate();
  };
  
  const changeYear = () => {
    currentYear.value = selectedYear.value;
    createCalendar(props.events);
    fetchImagesByDate();
  };
  
  const prevMonth = () => {
    currentMonthIndex.value--;
    if (currentMonthIndex.value < 0) {
      currentMonthIndex.value = 11;
      currentYear.value--;
    }
    createCalendar(props.events);
    fetchImagesByDate();
  };
  
  const nextMonth = () => {
    currentMonthIndex.value++;
    if (currentMonthIndex.value > 11) {
      currentMonthIndex.value = 0;
      currentYear.value++;
    }
    createCalendar(props.events);
    fetchImagesByDate();
  };
  
  const isHoliday = (date) => {
    const holidays = [
      new Date(date.getFullYear(), 0, 1),
    ];
    return holidays.some(holiday => holiday.toDateString() === date.toDateString());
  };
  
  watch(() => props.events, (newEvents) => {
    createCalendar(newEvents);
    fetchImagesByDate();
  }, { immediate: true });
  </script>
  
  <style scoped>
  .saturday .day-number {
  color: #82beff; /* 파란색 */
}

.sunday .day-number {
  color: #ff808d; /* 빨간색 */
}

  .calendar {
    margin: 20px;
    border-radius: 5px;
    overflow: hidden;
    border: 2px solid #ddd;
    box-shadow: none;
    max-width: 100%; /* 부모 요소 크기에 맞게 너비를 제한 */
  }

  .calendar-title {
    text-align: center;
    font-size: 20px;
    font-weight: bold;
    background-color: #f0f0f0;
    padding: 15px 0;
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
    border: 2px solid #ddd;
    border-radius: 5px;
    height: 100%;
    /* max-height: 600px;  */
    /* overflow-y: auto;  */
  }

  .calendar-header {
    display: flex;
    background-color: #f9f9f9;
    text-align: left; /* 요일을 좌측 정렬 */
  }

  .calendar-header-cell {
    flex: 1;
    text-align: left; /* 요일을 좌측 정렬 */
    padding: 10px;
    border-bottom: 2px solid #ddd;
    font-weight: bold;
  }

  .calendar-body {
    display: flex;
    flex-direction: column;
    border: 2px solid #ddd;
    box-shadow: none;
  }

  .calendar-week {
    display: flex;
    justify-content: space-between;
  }

  .calendar-day {
    flex: 1;
    border-bottom: 1px solid #ddd;
    padding: 10px;
    position: relative;
    cursor: pointer;
    height: 120px; /* 크기를 조정하여 이미지 크기를 맞춤 */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .day-image {
    width: 100px;  /* 이미지 크기를 적절히 조정 */
    height: 100px; /* 이미지 크기를 적절히 조정 */
    object-fit: cover;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 0;
  }

  .small-default-image {
    width: 100px;
    height: 100px;
    object-fit: cover;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 0;
  }

  .day-number {
  position: absolute;
  top: 5px; /* 상단 여백 */
  left: 5px; /* 좌측 여백 */
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  z-index: 1;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
}


  .event-count {
    font-size: 1.2rem;
    font-weight: bold;
    color: white;
    z-index: 1;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
    cursor: pointer;
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

  .close {
    float: right;
    font-size: 25px;
    cursor: pointer;
  }
</style>
