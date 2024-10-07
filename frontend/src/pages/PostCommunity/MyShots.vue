<template>
  <div id="my-shots">
    <!-- 카테고리 선택 버튼들 -->
    <div class="category-tags">
      <button
        @click="selectCategory('coffee')"
        :class="{ active: selectedCategory === 'coffee' }"
      >
        참여루틴
      </button>
      <button
        @click="selectCategory('cafe')"
        :class="{ active: selectedCategory === 'cafe' }"
      >
        #카페/간식
      </button>
      <button
        @click="selectCategory('shopping')"
        :class="{ active: selectedCategory === 'shopping' }"
      >
        #쇼핑
      </button>
      <button
        @click="selectCategory('transport')"
        :class="{ active: selectedCategory === 'transport' }"
      >
        #교통
      </button>
      <button
        @click="selectCategory('food')"
        :class="{ active: selectedCategory === 'food' }"
      >
        #식비
      </button>
      <button
        @click="selectCategory('culture')"
        :class="{ active: selectedCategory === 'culture' }"
      >
        #문화/여가
      </button>
      <button
        @click="selectCategory('alcohol')"
        :class="{ active: selectedCategory === 'alcohol' }"
      >
        #술/유흥
      </button>
      <button
        @click="selectCategory('housing')"
        :class="{ active: selectedCategory === 'housing' }"
      >
        #주거/공과금
      </button>
      <button
        @click="selectCategory('other')"
        :class="{ active: selectedCategory === 'other' }"
      >
        #기타
      </button>
    </div>

    <!-- Routine Section 1 -->
    <div class="routine-calendar">
      <div :class="['routine-header', selectedCategory]">
        {{ currentRoutine }}
      </div>
      <h3 class="month-label">{{ currentMonth }}</h3>
      <div class="calendar-grid">
        <div v-for="day in days" :key="'routine-' + day" class="day-tile">
          <img
            v-if="currentShots[day]"
            :src="currentShots[day]"
            alt="Shot Image"
          />
          <span v-else>{{ day }}</span>
        </div>
      </div>
    </div>

    <!-- Routine Section 2 (Duplicate) -->
    <div class="routine-calendar">
      <div :class="['routine-header', selectedCategory]">
        {{ currentRoutine }}
      </div>
      <h3 class="month-label">{{ currentMonth }}</h3>
      <div class="calendar-grid">
        <div
          v-for="day in days"
          :key="'duplicate-routine-' + day"
          class="day-tile"
        >
          <img
            v-if="currentShots[day]"
            :src="currentShots[day]"
            alt="Shot Image"
          />
          <span v-else>{{ day }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      days: Array.from({ length: 31 }, (_, i) => i + 1),
      currentRoutine: '텀블러에 커피 담아가기',
      currentMonth: '9월',
      selectedCategory: 'coffee',
      shots: {
        coffee: {
          1: 'https://example.com/coffee1.jpg',
          2: 'https://example.com/coffee2.jpg',
          // Add more images as needed
        },
        cafe: {},
        shopping: {},
        transport: {},
        food: {},
        culture: {},
        alcohol: {},
        housing: {},
        other: {},
      },
    };
  },
  computed: {
    currentShots() {
      return this.shots[this.selectedCategory];
    },
  },
  methods: {
    selectCategory(category) {
      this.selectedCategory = category;

      // Update routine name based on category
      const routineNames = {
        coffee: '텀블러에 커피 담아가기',
        cafe: '카페/간식 루틴',
        shopping: '쇼핑 루틴',
        transport: '교통 루틴',
        food: '식비 루틴',
        culture: '문화/여가 루틴',
        alcohol: '술/유흥 루틴',
        housing: '주거/공과금 루틴',
        other: '기타 루틴',
      };

      this.currentRoutine = routineNames[category] || '';
    },
  },
};
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
  background-color: #71b5fe; /* Active button color */
}

.category-tags button:nth-child(1) {
  background-color: #fcf5ab; /* Light yellow for 참여루틴 */
}

.routine-calendar {
  background-color: #f9f9f9;
  padding: 20px;
}

.routine-header {
  padding: 10px;
  border-radius: 10px 10px 0 0; /* Rounded top corners */
  width: calc(100% / 3); /* One-third width */
  position: relative;
  top: -10px; /* Move the index slightly up */
}

/* Category-specific colors */
.routine-header.coffee {
  background-color: #ffcedc; /* Color for coffee routine */
}

.routine-header.cafe {
  background-color: #c8b3a3; /* Light yellow for cafe */
}

.routine-header.shopping {
  background-color: #fabcbc; /* Soft red for shopping */
}

.routine-header.transport {
  background-color: #c4f1fd; /* Light blue for transport */
}

.routine-header.food {
  background-color: #dffba7; /* Light khaki for food */
}

.routine-header.culture {
  background-color: #d8dbfd; /* Light lavender for culture */
}

.routine-header.alcohol {
  background-color: #ffcbd9; /* Light coral for alcohol */
}

.routine-header.housing {
  background-color: #ffe0d2; /* Light green for housing */
}

.routine-header.other {
  background-color: #ffe4b5; /* Light lemon chiffon for other */
}

.month-label,
.routine-header,
.hashtags button {
  font-weight: bold; /* Bold font */
}

.month-label {
  font-size: smaller; /* Smaller font size for the month label */
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(
    7,
    minmax(0, 1fr)
  ); /* Seven columns for a week */
  gap: 10px;
}

.day-tile {
  width: auto;
  aspect-ratio: 1 / 1; /* Maintain square aspect ratio */
  position: relative;
  border-radius: 10px; /* Rounded corners */
  background-color: #e0e0e0; /* Light gray background */
}

.day-tile img,
.day-tile span {
}
</style>
