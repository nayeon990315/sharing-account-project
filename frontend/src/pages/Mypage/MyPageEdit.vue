<template>
  <div class="my-page">
    <h1>My Page</h1>
    <div class="profile-section">
      <img 
        :src="profileImageUrl" 
        alt="Profile" 
        class="profile-image" 
        @click="triggerFileInput" 
      />
      <div class="profile-info">
        <h2>{{ user.name }} 님</h2>
        <p>오늘도 한 꿀씩 쌓아볼까요?</p>
        <button @click="editProfile">프로필 수정</button>
      </div>
    </div>

    <div class="stats-section">
      <div class="stat">
        <p>지금까지 벌꿀님이 모은 꿀은</p>
        <h3>1,000 꿀</h3>
      </div>
      <div class="stat">
        <p>지금까지 이행한 루틴은</p>
        <h3>15건</h3>
      </div>
      <div class="stat">
        <p>지금까지 절약한 금액은</p>
        <h3>153,000원</h3>
      </div>
    </div>

    <button class="save-button">꿀 단지 넣기</button>
    
    <!-- Hidden file input for uploading profile picture -->
    <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" style="display: none;" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        name: '김벌꿀',
      },
      profileImageUrl: require('@/assets/images/profile-image.jpg'), // 기본 프로필 이미지 경로
    };
  },
  methods: {
    editProfile() {
      alert('프로필 수정 페이지로 이동합니다.');
    },
    triggerFileInput() {
      // 숨겨진 파일 입력 클릭
      this.$refs.fileInput.click();
    },
    onFileChange(event) {
      const file = event.target.files[0]; // 선택된 파일
      if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.profileImageUrl = e.target.result; // 새 프로필 이미지로 변경
        };
        reader.readAsDataURL(file);
      } else {
        alert('이미지를 선택해 주세요.');
      }
    },
  },
};
</script>

<style scoped>
.my-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
.profile-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
.profile-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-right: 20px;
  cursor: pointer; /* 클릭 가능한 커서로 변경 */
}
.profile-info h2 {
  margin: 0;
}
.stats-section {
  display: flex;
  justify-content: space-between;
}
.stat {
  background-color: #f0f0f0;
  padding: 10px;
  width: calc(33% - 10px);
}
.save-button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 20px;
}
</style>