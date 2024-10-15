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
            @click="() => {
              console.log('dt=', day.date.getDate()); 
              openPostModal( day.date.getFullYear(), day.date.getMonth()+1,  day.date.getDate() ); // 날짜 클릭 시 모달 열기
              // openPostModal('2024-11-1'); // 날짜 클릭 시 모달 열기
            }"
          >
            <div class="day-number">{{ day.date.getDate() }}</div>
            <div class="day-events">
              <img 
                v-if="imagesByDate[day.date.getDate()]" 
                :src="imagesByDate[day.date.getDate()]" 
                alt="image" 
                class="day-image" 
              />
              <img 
                v-else 
                src="/src/assets/꿀벌 X.png" 
                alt="default image" 
                class="day-image small-default-image" 
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 모달창 -->
    <Modal :isVisible="isModalOpen" @close="closeModal">
      <!-- <PostItem2 :selectedDate="selectedDate" /> -->
      <PostItem2 :selectedYear="selectedYear"   :selectedMonth="selectedMonth"  :selectedDt="selectedDt"  />
    </Modal>
  </div>
</template>


<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';
import Modal from '@/components/PostCommunity/MyShotsModal.vue';
import PostItem2 from '@/components/PostCommunity/PostItem2.vue'; // postItem2 컴포넌트 import

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
const post = ref([]);
const isModalOpen = ref(false);

const selectedDt =ref(null);


const toggleMonthSelector = () => {
  monthSelectorVisible.value = !monthSelectorVisible.value;
};

const createCalendar = (events = [], posts = []) => {
  const startDate = new Date(currentYear.value, currentMonthIndex.value, 1);
  const startDay = startDate.getDay();

  // 35일간의 날짜 배열 생성
  const totalDays = Array.from({ length: 35 }, (_, i) => {
    const date = new Date(startDate);                                 //date객체 선언되는 부분
    date.setDate(i - startDay + 1);

    // 각 날짜에 해당하는 post 데이터를 찾아 할당 (여기서 날짜에 맞는 post를 찾음)
    const postForThisDay = posts.find(post => {
      const postDate = new Date(post.date);  // post 데이터의 날짜
      return postDate.getFullYear() === date.getFullYear() &&
             postDate.getMonth() === date.getMonth() &&
             postDate.getDate() === date.getDate();
    });

    return { date, events: [], post: postForThisDay || null }; // post 데이터가 있으면 할당
  });

  // 주 단위로 분리하여 weeks 배열에 할당
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

    console.log('resoponse.data',response.data);
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

/* 모달부분 */
// 모달 상태와 선택된 포스트 데이터 관리
const isModalVisible = ref(false); // 모달 보이기 상태
const selectedPost = ref(null); // 선택된 포스트 정보

// 날짜 셀 클릭 시 모달창 열고, 선택된 날짜를 설정
// const openPostModal = (date) => {
//   selectedDate.value = date.toISOString();  // Date 객체를 ISO 형식의 문자열로 변환
 
//   isModalOpen.value = true;   // 모달 열기
// };
const openPostModal = ( year, month, dt) => {
  // selectedDate.value = date.toISOString();  // Date 객체를 ISO 형식의 문자열로 변환
  selectedDt.value = dt;
  selectedMonth.value = month;
  selectedYear.value = year;
  isModalOpen.value = true;   // 모달 열기
};
// 모달 닫기 함수
const closeModal = () => {
  isModalOpen.value = false;
  isModalVisible.value = false;
  console.log('Closing modal');
  console.log(isModalOpen.value);
};
</script>

<style scoped>
.saturday .day-number {
  color: #82beff;
  /* 파란색 */
}

.sunday .day-number {
  color: #ff808d;
  /* 빨간색 */
}

.calendar {
  /* margin: 20px; */
  border-radius: 5px;
  border: 2px solid #ddd;
  box-shadow: none;
  display: flex;
  flex-direction: column;
  height: 100%;
  /* 부모의 높이에 맞춰 자식 요소 높이 설정 */
  max-height: 100%;
  /* 부모 높이를 넘지 않도록 제한 */
  width: 100%;
  /* 부모의 너비를 100% 차지하도록 설정 */
}

.calendar-title {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  background-color: #fef7d1;
  /* 옅은 노랑 배경 */
  color: #333;
  /* 텍스트를 짙은 회색으로 */
  padding: 15px 0;
  flex-shrink: 0;
  /* 제목 영역은 고정 크기로 설정 */
  border-bottom: 2px solid #ddd;
}

.calendar-grid {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  width: 100%;
  border: 2px solid #ddd;
  border-radius: 5px;
}


.calendar-header {
  display: flex;
  background-color: #f9f9f9;
  text-align: left;
  /* 요일을 좌측 정렬 */
  flex-shrink: 0;
  /* 헤더는 고정 높이로 설정 */
}

.calendar-body {
  flex-grow: 1;

  display: flex;
  flex-direction: column;
  border: 2px solid #ddd;
  box-shadow: none;
}

.calendar-week {
  display: flex;
  justify-content: space-between;
  flex-grow: 1;
  width: 100%;
}

.calendar-day {
  flex: 1;
  width: 100%;
  /* 날짜 셀의 너비를 설정 */
  border-bottom: 1px solid #ddd;
  padding: 10px;
  position: relative;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border: 1px solid #eee;
  background-color: #f9f9f9;
  transition: background-color 0.2s ease;
}

.month-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #333;
  font-weight: bold;
}

.month-selector {
  text-align: center;
  margin: 10px 0;
}

select {
  margin: 0 5px;
}




.calendar-header-cell {
  flex: 1;
  text-align: left;
  /* 요일을 좌측 정렬 */
  padding: 10px;
  border-bottom: 2px solid #ddd;
  font-weight: bold;
  background-color: white;
  /* 옅은 노랑 배경 */
  color: #333;
}



.day-image {
  width: 100px;
  /* 이미지 크기를 적절히 조정 */
  height: 100px;
  /* 이미지 크기를 적절히 조정 */
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
  top: 5px;
  /* 상단 여백 */
  left: 5px;
  /* 좌측 여백 */
  font-size: 1.5rem;
  font-weight: bold;
  color: black;
  z-index: 1;
  /* text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7); */
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
