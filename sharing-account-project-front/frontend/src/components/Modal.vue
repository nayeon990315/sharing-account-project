<template>
  <div class="modal" v-if="isVisible">
    <div class="modal-content">
      <h4 id="modal-title">{{ title }}</h4>
      <p id="modal-content">{{ message }}</p>
      <button @click="closeModal">확인</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isVisible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
    },
    message: {
      type: String,
    },
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
  },
};
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
  animation: slideIn 0.3s ease-out; /* 모달이 슬라이드 인 애니메이션 */
  
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 0;
  width: 500px;
  height: 300px;

  font-size: 15px;
  text-align: center;
  align-items: center;
  background-image: url('@/assets/images/background/background.png');
  /* 배경 이미지 경로 */
  background-size: cover;
  /* 이미지가 화면에 맞게 조정되도록 설정 */
  background-position: center;
  /* 이미지 중앙에 배치 */
}

#modal-title {
  text-align: center;
  margin-top: 10px;
}

#modal-content {
  margin-top: 10px;
  font-size: normal;
}

button {
  margin-top: 10px;
  padding: 8px 16px;
  border: none;
  border-radius: 0 !important;
  background-color: #000000;
  color: white;
}

button:hover {
  border: none;
  border-radius: 0 !important;
  color: white;
  background-color: #5a5542d3;
}
/* 슬라이드 인 애니메이션 */
@keyframes slideIn {
  from {
    transform: translateY(-100%); /* 화면 위에서 내려옴 */
    opacity: 0;
  }
  to {
    transform: translateY(0); /* 정상 위치에 도착 */
    opacity: 1;
  }
}
</style>
