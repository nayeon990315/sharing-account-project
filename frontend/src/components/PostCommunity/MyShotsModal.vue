<template>
  <div v-if="isVisible" class="modal-overlay">
    <div class="modal-content">
      <button class="close-button" @click="handleClose">X</button>
      <slot></slot>
    </div>
  </div>
</template>


<script setup>
const props = defineProps({
  isVisible: {
    type: Boolean,
    required: true
  }
});

// 모달 닫기 함수
const emit = defineEmits(['close']);
const handleClose = () => {
  emit('close');
  console.log('Close button clicked');
};
</script>

<style scoped>
/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(5px);
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  margin: 20px;
  border-radius: 12px;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;

  /* 높이 관련 스타일 */
  min-height: 400px; /* 모달의 최소 높이 설정 */
  max-height: 90vh;  /* 화면 높이의 90%로 최대 높이 설정 */
  overflow-y: auto;  /* 내용이 넘칠 경우 스크롤 */
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #333;
}

.close-button:hover {
  color: red;
}
</style>
