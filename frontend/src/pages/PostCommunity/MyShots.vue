<template>
  <div id="my-shots">
    <!-- 카테고리 선택 버튼들 -->
    <div class="category-tags">
      <button
        @click="selectCategory('참여루틴')"
        :class="{ active: selectedCategory === null }"
      >
        참여루틴
      </button>
      <button
        @click="selectCategory(['cafe', 'dessert'])"
        :class="{ active: isActiveCategory(['cafe', 'dessert']) }"
      >
        카페/간식
      </button>
      <button
        @click="
          selectCategory(['fashion_shopping', 'online_shopping', 'shopping'])
        "
        :class="{
          active: isActiveCategory([
            'fashion_shopping',
            'online_shopping',
            'shopping',
          ]),
        }"
      >
        쇼핑
      </button>
      <button
        @click="selectCategory(['transport', 'transportation'])"
        :class="{ active: isActiveCategory(['transport', 'transportation']) }"
      >
        교통
      </button>
      <button
        @click="selectCategory('food')"
        :class="{ active: isActiveCategory(['food']) }"
      >
        식비
      </button>
      <button
        @click="selectCategory(['culture', 'culture_leisure'])"
        :class="{ active: isActiveCategory(['culture', 'culture_leisure']) }"
      >
        문화/여가
      </button>
      <button
        @click="selectCategory(['alcohol', 'alcohol_entertainment'])"
        :class="{
          active: isActiveCategory(['alcohol', 'alcohol_entertainment']),
        }"
      >
        술/유흥
      </button>
      <button
        @click="selectCategory(['housing', 'housing_utilities'])"
        :class="{ active: isActiveCategory(['housing', 'housing_utilities']) }"
      >
        주거/공과금
      </button>
      <button
        @click="selectCategory('other')"
        :class="{ active: isActiveCategory(['other']) }"
      >
        기타
      </button>
    </div>

    <!-- 날짜 선택 (월/년) -->
    <VueDatePicker v-model="selectedMonthYear" month-picker />

    <!-- 루틴 목록을 동적으로 표시 -->
    <div
      v-for="(routine, index) in filteredRoutines"
      :key="index"
      class="routine-calendar"
    >
      <div class="routine-header">
        {{ routine.habitTitle }}<br />
        {{ selectedMonthYear?.year || '연도 없음' }}년
        {{ (selectedMonthYear?.month ?? 0) + 1 }}월
      </div>

      <!-- 달력에 이미지 매핑 -->
      <div class="calendar-grid">
        <div v-for="day in days" :key="'routine-' + day" class="day-tile">
          <img
            v-if="routine.shots && routine.shots[day]"
            :src="routine.shots[day]"
            alt="Shot Image"
            class="day-image"
          />
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

const days = Array.from({ length: 31 }, (_, i) => i + 1);
const activeRoutines = ref([]);
const selectedMonthYear = ref({
  month: new Date().getMonth(),
  year: new Date().getFullYear(),
});
const selectedCategory = ref(null);

const selectCategory = (category) => {
  if (category === '참여루틴') {
    selectedCategory.value = null;
  } else {
    selectedCategory.value = Array.isArray(category) ? category : [category];
  }
};

const isActiveCategory = (categories) => {
  return (
    Array.isArray(selectedCategory.value) &&
    categories.some((cat) => selectedCategory.value.includes(cat))
  );
};

const filteredRoutines = computed(() => {
  if (!selectedCategory.value) {
    return activeRoutines.value;
  }
  return activeRoutines.value.filter((routine) => {
    const category =
      routine.categoryTitle?.toLowerCase() ||
      routine.category_title?.toLowerCase();
    return selectedCategory.value.includes(category);
  });
});

const fetchCurrentRoutines = async () => {
  const userId = localStorage.getItem('userId');
  try {
    const response = await axios.get(
      `http://localhost:8080/post-community/myhabit/active`,
      {
        params: { userId },
      }
    );
    activeRoutines.value = response.data;
    activeRoutines.value.forEach((routine) => {
      fetchRoutineImages(routine);
    });
  } catch (error) {
    console.error('진행 중인 루틴을 가져오는 중 오류 발생:', error);
  }
};

const fetchRoutineImages = async (routine) => {
  const userId = localStorage.getItem('userId');
  try {
    const response = await axios.get(
      `http://localhost:8080/post-community/images`,
      {
        params: {
          userId,
          month: selectedMonthYear.value.month + 1,
          year: selectedMonthYear.value.year,
          habitId: routine.habitId,
        },
      }
    );

    const shots = {};
    response.data.forEach((imageData) => {
      const imageDate = new Date(imageData.createdAt);
      const day = imageDate.getDate();
      shots[day] = imageData.imageURL;
    });

    routine.shots = shots;
  } catch (error) {
    console.error('이미지를 가져오는 중 오류 발생:', error);
  }
};

watch(selectedMonthYear, () => {
  activeRoutines.value.forEach((routine) => {
    fetchRoutineImages(routine);
  });
});

onMounted(() => {
  fetchCurrentRoutines();
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
  padding: 8px15px;
  border-radius: 20px;
  border: none;
  background-color: #d3d3d3;
  color: #333;
  box-shadow: 2px2px5pxrgba (0, 0, 0, 0.2);
  transition: background-color0.3s, color0.3s, box-shadow0.3s;
}

.category-tags button.active {
  background-color: #71b5fe;
}

.category-tags button:hover {
  background-color: #4a90e2;
  color: #fff;
  box-shadow: none;
}

.routine-calendar {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 4px8pxrgba (0, 0, 0, 0.1);
}

.routine-header {
  padding: 10px;
  border-radius: 10px;
  width: calc(100% / 3);
  position: relative;
  top: -10px;
  background-color: #71b5fe;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, minmax(0, 1fr));
  gap: 10px;
}

.day-tile {
  width: auto;
  aspect-ratio: 1/1;
  position: relative;
  border-radius: 10px;
  background-color: #e0e0e0;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.day-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.pagination-year {
  margin-bottom: 5px;
}

.pagination-month {
  margin-top: 5px;
}
</style>
