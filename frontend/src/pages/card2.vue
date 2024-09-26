<template>
  <div class="card-container">
    <div
      v-for="(card, index) in cards"
      :key="index"
      class="card"
      :style="getCardStyle(index)"
      @mouseover="handleMouseOver(index)"
      @mouseleave="handleMouseLeave"
      :class="{ selected: selectedCard === index }"
    >
      Card {{ index + 1 }}
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  setup() {
    // 카드 배열과 선택된 카드 상태
    const cards = ref(new Array(10).fill(null)); // 카드 10개 생성
    const selectedCard = ref(null); // 선택된 카드를 추적하는 상태

    // 카드의 기울기와 위치를 동적으로 설정
    const getCardStyle = (index) => {
      const translateZ = index * -50;  // 카드가 Z축에서 멀어지도록 설정
      const rotateY = index * 5;       // 각 카드가 Y축으로 기울어지도록 설정
      const translateX = index * 50;   // 카드가 X축으로 이동
      return {
        transform: `translateX(${translateX}px) translateZ(${translateZ}px) rotateY(${rotateY}deg)`
      };
    };

    // 마우스 오버 시 해당 카드 선택
    const handleMouseOver = (index) => {
      selectedCard.value = index;
    };

    // 마우스가 카드에서 벗어났을 때 선택 해제
    const handleMouseLeave = () => {
      selectedCard.value = null;
    };

    return {
      cards,
      selectedCard,
      getCardStyle,
      handleMouseOver,
      handleMouseLeave,
    };
  }
};
</script>

<style scoped>
body {
  margin: 0;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  perspective: 1500px;
  background-color: #f0f0f0;
}

.card-container {
  position: relative;
  width: 600px;
  height: 400px;
  transform-style: preserve-3d;
  display: flex;
  align-items: center;
}

.card {
  position: absolute;
  width: 300px;
  height: 200px;
  background: lightgray;
  border: 1px solid #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  color: white;
  font-weight: bold;
  backface-visibility: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  transform-origin: bottom left;
  transition: transform 0.5s ease, opacity 0.5s ease;
}

/* 카드에 마우스를 올렸을 때 효과 */
.card:hover {
  transform: translateZ(50px) scale(1.1);
}

/* 선택된 카드 효과 */
.card.selected {
  transform: translateZ(100px) scale(1.2) rotateY(0deg);
}
</style>
