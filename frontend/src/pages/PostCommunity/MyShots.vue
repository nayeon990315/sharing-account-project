<template>
  <div id="my-shots">
    <!-- 카테고리 선택 버튼들 -->
    <div class="category-tags">
      <button
        v-for="category in categories"
        :key="category.value"
        @click="selectCategory(category.value)"
        :class="{ active: selectedCategory === category.value }"
      >
        {{ category.label }}
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

    <!-- Routine Section 2 -->
    <div class="routine-calendar second-routine">
      <div :class="['routine-header', selectedCategory, 'second-routine']">
        {{ secondRoutine }}
      </div>
      <h3 class="month-label">{{ currentMonth }}</h3>
      <div class="calendar-grid">
        <div
          v-for="day in days"
          :key="'second-routine-' + day"
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
      secondRoutine: '커피 마시고 잔 반납하기',
      currentMonth: '9월',
      selectedCategory: 'coffee',
      categories: [
        { value: 'coffee', label: '참여루틴' },
        { value: 'cafe', label: '#카페/간식' },
        { value: 'shopping', label: '#쇼핑' },
        { value: 'transport', label: '#교통' },
        { value: 'food', label: '#식비' },
        { value: 'culture', label: '#문화/여가' },
        { value: 'alcohol', label: '#술/유흥' },
        { value: 'housing', label: '#주거/공과금' },
        { value: 'other', label: '#기타' },
      ],
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
      routineNames: {
        coffee: ['텀블러에 커피 담아가기', '커피 마시고 잔 반납하기'],
        cafe: ['카페/간식 루틴 1', '카페/간식 루틴 2'],
        shopping: ['쇼핑 루틴 1', '쇼핑 루틴 2'],
        transport: ['교통 루틴 1', '교통 루틴 2'],
        food: ['식비 루틴 1', '식비 루틴 2'],
        culture: ['문화/여가 루틴 1', '문화/여가 루틴 2'],
        alcohol: ['술/유흥 루틴 1', '술/유흥 루틴 2'],
        housing: ['주거/공과금 루틴 1', '주거/공과금 루틴 2'],
        other: ['기타 루틴 1', '기타 루틴 2'],
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
      this.currentRoutine = this.routineNames[category][0] || '';
      this.secondRoutine = this.routineNames[category][1] || '';
    },
    generateRoutineStyles() {
      const categories = [
        'coffee',
        'cafe',
        'shopping',
        'transport',
        'food',
        'culture',
        'alcohol',
        'housing',
        'other',
      ];
      const colors = [
        '#ffcedc',
        '#c8b3a3',
        '#fabcbc',
        '#c4f1fd',
        '#dffba7',
        '#d8dbfd',
        '#ffcbd9',
        '#ffe0d2',
        '#ffe4b5',
      ];
      let styles = '';

      categories.forEach((category, index) => {
        styles += `.routine-header.${category} { background-color: ${colors[index]}; }\n`;
        styles += `.routine-header.${category}.second-routine { background-color: ${this.darkenColor(
          colors[index],
          20
        )}; }\n`;
      });

      return styles;
    },
    darkenColor(color, percent) {
      const num = parseInt(color.replace('#', ''), 16),
        amt = Math.round(2.55 * percent),
        R = (num >> 16) - amt,
        G = ((num >> 8) & 0x00ff) - amt,
        B = (num & 0x0000ff) - amt;
      return `#${(
        (1 << 24) |
        ((R < 255 ? (R < 1 ? 0 : R) : 255) << 16) |
        ((G < 255 ? (G < 1 ? 0 : G) : 255) << 8) |
        (B < 255 ? (B < 1 ? 0 : B) : 255)
      )
        .toString(16)
        .slice(1)}`;
    },
  },
  created() {
    const style = document.createElement('style');
    style.textContent = this.generateRoutineStyles();
    document.head.appendChild(style);
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
  background-color: #71b5fe;
}

.category-tags button:nth-child(1) {
  background-color: #fcf5ab;
}

.routine-calendar {
  background-color: #f9f9f9;
  padding: 20px;
  margin-bottom: 20px;
}

.routine-header {
  padding: 10px;
  border-radius: 10px 10px 0 0;
  width: calc(100% / 3);
  position: relative;
  top: -10px;
}

.month-label,
.routine-header,
.hashtags button {
  font-weight: bold;
}

.month-label {
  font-size: smaller;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, minmax(0, 1fr));
  gap: 10px;
}

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

.day-tile img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.day-tile span {
  font-size: 14px;
}
</style>
