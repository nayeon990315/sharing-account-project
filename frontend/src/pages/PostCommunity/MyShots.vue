<template>
  <div id="my-shots">
    <!-- 카테고리 선택 버튼들 -->
    <div class="category-tags">

      <button @click="selectCategory('참여루틴')" :class="{ active: selectedCategory === null }" 
        >
        참여루틴
      </button>

      <button @click="selectCategory(['cafe', 'dessert'])"
        :class="{ active: Array.isArray(selectedCategory) && (selectedCategory.includes('cafe') || selectedCategory.includes('dessert')) }">
        #카페/간식
      </button>

      <button @click="selectCategory(['fashion_shopping', 'online_shopping', 'shopping'])"
        :class="{ active: Array.isArray(selectedCategory) && (selectedCategory.includes('fashion_shopping') || selectedCategory.includes('online_shopping') || selectedCategory.includes('shopping')) }">
        #쇼핑
      </button>

      <button @click="selectCategory(['transport', 'transportation'])"
        :class="{ active: Array.isArray(selectedCategory) && (selectedCategory.includes('transport') || selectedCategory.includes('transportation')) }">
        #교통
      </button>

      <button @click="selectCategory('food')"
        :class="{ active: Array.isArray(selectedCategory) && selectedCategory.includes('food') }">
        #식비
      </button>

      <button @click="selectCategory(['culture', 'culture_leisure'])"
        :class="{ active: Array.isArray(selectedCategory) && (selectedCategory.includes('culture') || selectedCategory.includes('culture_leisure')) }">
        #문화/여가
      </button>

      <button @click="selectCategory(['alcohol', 'alcohol_entertainment'])"
        :class="{ active: Array.isArray(selectedCategory) && (selectedCategory.includes('alcohol') || selectedCategory.includes('alcohol_entertainment')) }">
        #술/유흥
      </button>

      <button @click="selectCategory(['housing', 'housing_utilities'])"
        :class="{ active: Array.isArray(selectedCategory) && (selectedCategory.includes('housing') || selectedCategory.includes('housing_utilities')) }">
        #주거/공과금
      </button>

      <button @click="selectCategory('other')"
        :class="{ active: Array.isArray(selectedCategory) && selectedCategory.includes('other') }">
        #기타

      </button>
    </div>



    <!-- 날짜 선택 (월/년) -->
    <VueDatePicker v-model="selectedMonthYear" month-picker />

    <!-- 루틴 목록을 동적으로 표시 -->
    <div v-for="(routine, index) in filteredRoutines" :key="index" class="routine-calendar">
      <!-- 각각의 루틴에 대한 제목 (habit_title) -->
      <div class="routine-header">
        {{ routine.habitTitle }}<br>
        {{ selectedMonthYear?.year || '연도 없음' }}년 {{ (selectedMonthYear?.month ?? 0) + 1 }}월
      </div>

      <!-- 달력에 이미지 매핑 -->
      <div class="calendar-grid">
        <div v-for="day in days" :key="'routine-' + day" class="day-tile">
          <img v-if="routine.shots && routine.shots[day]" :src="routine.shots[day]" alt="Shot Image"
            class="day-image" />
          <span v-else>{{ day }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import axios from 'axios';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

// 기본 데이터 설정
const days = Array.from({ length: 31 }, (_, i) => i + 1);  // 1~31일까지의 날짜 리스트
const activeRoutines = ref([]);  // 진행 중인 루틴들
const selectedMonthYear = ref({
  month: new Date().getMonth(),  // 현재 월 (1월이 0이므로 0부터 시작)
  year: new Date().getFullYear(), // 현재 연도
});
const selectedCategory = ref(null);  // 선택된 카테고리 (처음엔 선택되지 않음)

// 카테고리 선택 메소드
const selectCategory = (category) => {
  if (category === '참여루틴') {
    selectedCategory.value = null;  // 전체 데이터를 보여주기 위해 null로 설정
  } else {
    selectedCategory.value = Array.isArray(category) ? category : [category];  // 배열이거나 단일값 처리
  }
};





// 선택된 카테고리에 맞는 루틴을 필터링
const filteredRoutines = computed(() => {
  if (!selectedCategory.value) {
    return activeRoutines.value;  // 전체 데이터를 반환
  }
  return activeRoutines.value.filter((routine) => {
    const category = routine.categoryTitle?.toLowerCase() || routine.category_title?.toLowerCase();
    return selectedCategory.value.includes(category);  // selectedCategory 배열에 포함된 카테고리만 필터링
  });
});


// 여러 개의 진행 중인 루틴을 서버에서 가져옴
const fetchCurrentRoutines = async () => {
  const userId = localStorage.getItem('userId');  // 사용자 ID 가져옴
  try {
    const response = await axios.get(`http://localhost:8080/post-community/myhabit/active`, {
      params: { userId }  // userId를 쿼리 파라미터로 전송
    });
    activeRoutines.value = response.data;  // 루틴 데이터 저장

    // 각 루틴에 대해 개별적으로 이미지를 가져오는 함수 호출
    activeRoutines.value.forEach((routine) => {
      fetchRoutineImages(routine);  // 루틴별 이미지 데이터 로드
    });
  } catch (error) {
    console.error('진행 중인 루틴을 가져오는 중 오류 발생:', error);
  }
};

// 특정 루틴의 이미지 데이터를 서버에서 가져옴
const fetchRoutineImages = async (routine) => {
  const userId = localStorage.getItem('userId');  // 사용자 ID 가져옴
  try {
    const response = await axios.get(`http://localhost:8080/post-community/images`, {
      params: {
        userId,
        month: selectedMonthYear.value.month + 1,  // 현재 선택된 달 (1월이 0이므로 +1)
        year: selectedMonthYear.value.year,    // 현재 선택된 연도
        habitId: routine.habitId   // 각 루틴의 habitId에 맞는 이미지 가져오기
      }
    });

    console.log("fetchRoutineImages", response.data);

    // 이미지를 날짜에 맞게 매핑하는 로직
    const shots = {};  // 빈 객체에 날짜별 이미지를 저장

    response.data.forEach((imageData) => {
      const imageDate = new Date(imageData.createdAt);  // createdAt을 JS Date 객체로 변환
      const day = imageDate.getDate();  // 날짜를 추출 (1~31 사이의 값)

      // 해당 날짜에 이미지를 저장
      shots[day] = imageData.imageURL;
    });

    // 해당 루틴에 대한 이미지 데이터를 shots 배열에 저장
    routine.shots = shots;  // 날짜별 이미지 데이터를 매핑
    console.log("routine.shots", routine.shots);

  } catch (error) {
    console.error('이미지를 가져오는 중 오류 발생:', error);
  }
};

// 월/년이 변경될 때마다 데이터를 새로 가져옴
watch(selectedMonthYear, () => {
  activeRoutines.value.forEach((routine) => {
    fetchRoutineImages(routine);  // 선택된 달/년의 이미지 데이터를 루틴별로 새로 가져옴
  });
});

// 페이지가 로드될 때 실행되는 함수
onMounted(() => {
  fetchCurrentRoutines();  // 현재 진행 중인 루틴을 로드
});
</script>





<style scoped>
#my-shots {
  padding: 20px;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.category-tags button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 5px 10px;
  border-radius: 20px;
  border: none;
  background-color: #f0f0f0;
}

.category-tags button.active {
  background-color: #71b5fe;
  /* Active button color */
}

.category-tags button:nth-child(1) {
  background-color: #fcf5ab;
  /* Light yellow for 참여루틴 */
}

.routine-calendar {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px 10px 10px 10px;
  margin-bottom: 20px; /* 요소 사이에 간격 추가 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.routine-header {
  padding: 10px;
  border-radius: 10px 10px 0 0;
  /* Rounded top corners */
  width: calc(100% / 3);
  /* One-third width */
  position: relative;
  top: -10px;
  /* Move the index slightly up */
  background-color: #71b5fe
}

/* Category-specific colors */
.routine-header.coffee {
  background-color: #ffcedc;
  /* Color for coffee routine */
}

.routine-header.cafe {
  background-color: #c8b3a3;
  /* Light yellow for cafe */
}

.routine-header.shopping {
  background-color: #fabcbc;
  /* Soft red for shopping */
}

.routine-header.transport {
  background-color: #c4f1fd;
  /* Light blue for transport */
}

.routine-header.food {
  background-color: #dffba7;
  /* Light khaki for food */
}

.routine-header.culture {
  background-color: #d8dbfd;
  /* Light lavender for culture */
}

.routine-header.alcohol {
  background-color: #ffcbd9;
  /* Light coral for alcohol */
}

.routine-header.housing {
  background-color: #ffe0d2;
  /* Light green for housing */
}

.routine-header.other {
  background-color: #ffe4b5;
  /* Light lemon chiffon for other/////////////////////////// */
  width: calc(100% / 3);
  position: relative;
  top: -10px;
}

.month-label,
.routine-header,
.hashtags button {
  font-weight: bold;
  /* Bold font */
}

.month-label {
  font-size: smaller;
  /* Smaller font size for the month label */
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7,
      minmax(0, 1fr));
  /* Seven columns for a week */
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

/* 날짜 타일의 크기와 스타일 지정 */
.day-tile {
  width: auto;
  aspect-ratio: 1 / 1;
  position: relative;
  border-radius: 10px;
  background-color: #e0e0e0;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

/* 이미지 크기를 타일에 맞추기 위한 스타일 */
.day-image {
  width: 100%;
  /* 타일의 너비에 맞게 이미지 조정 */
  height: 100%;
  /* 타일의 높이에 맞게 이미지 조정 */
  object-fit: cover;
  /* 이미지가 타일의 크기를 벗어나지 않도록 조정 */
}

/* 년도와 달 선택 관련 페이지네이션 스타일 */
.pagination-year {
  margin-bottom: 5px;
}

.pagination-month {
  margin-top: 5px;
}
</style>