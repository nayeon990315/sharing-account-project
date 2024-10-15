<template>
  <div class="container full-screen-container">
    <div class="profile-card p-4 shadow-lg">
      <h1 class="text-center mb-4">🐝 환영합니다! 🐝</h1>

      <form @submit.prevent="submitForm">
        <div class="form-group mb-4">
          <label for="nickname" class="form-label">닉네임:</label>
          <input
            type="text"
            id="nickname"
            v-model="nickname"
            class="form-control"
            placeholder="닉네임을 입력하세요"
            required
          />
        </div>

        <div class="form-group mb-4">
          <label for="image" class="form-label">프로필 이미지:</label>
          <input
            type="file"
            id="image"
            @change="onFileChange"
            class="form-control"
            accept="image/*"
            required
          />
        </div>

        <div class="d-flex justify-content-center">
          <button type="submit" class="btn submit-btn mt-3">벌루틴 시작하기</button>
        </div>
      </form>

      <div v-if="previewImage" class="text-center mt-4">
        <h3>미리보기:</h3>
        <img
          :src="previewImage"
          alt="Profile Image Preview"
          class="img-thumbnail preview-image"
        />
      </div>
    </div>

    <!-- Alert Modal -->
    <CustomModal :isVisible="isModalVisible" title="알림" :message="modalMessage" @close="closeModal" />
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import CustomModal from '@/components/Modal.vue';

export default {
  name: 'Register',
  components: {
    CustomModal
  },
  setup() {
    const nickname = ref('');
    const image = ref(null);
    const previewImage = ref(null);
    const isModalVisible = ref(false);
    const modalMessage = ref('');

    const router = useRouter();

    const onFileChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        image.value = file;
        previewImage.value = URL.createObjectURL(file);
      }
    };

    const closeModal = () => {
      isModalVisible.value = false;
      router.push('/loginHome');
    };

    const submitForm = async () => {
      if (!nickname.value || !image.value) {
        modalMessage.value = 'Please complete the form.';
        isModalVisible.value = true;
        return;
      }

      const jwtToken = $cookies.get('jwtToken');
      if (!jwtToken) {
        modalMessage.value = '로그인이 필요합니다!';
        isModalVisible.value = true;
        router.push('/login');
        return;
      }

      const formData = new FormData();
      formData.append('nickname', nickname.value);
      formData.append('image', image.value);

      try {
        const response = await axios.post('http://localhost:8080/users/updateProfile', formData, {
          headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'multipart/form-data'
          }
        });

        modalMessage.value = '환영합니다! ' + nickname.value + "님!";
        isModalVisible.value = true;
        
      } catch (error) {
        modalMessage.value = 'Error updating profile.';
        isModalVisible.value = true;
      }
    };

    return {
      nickname,
      image,
      previewImage,
      isModalVisible,
      modalMessage,
      onFileChange,
      submitForm,
      closeModal
    };
  }
};
</script>

<style scoped>
/* Container 스타일 */
.full-screen-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #fff7e6;
  background-image: url('@/assets/images/bee_background.png');
  background-size: cover;
  background-position: center;
}

/* 프로필 카드 스타일 */
.profile-card {
  background-color: #ffecb3;
  border-radius: 15px;
  width: 100%;
  max-width: 500px;
  text-align: center;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.1);
  position: relative;
}

h1 {
  color: #ffca28;
  font-family: 'Roboto', sans-serif;
  font-weight: bold;
  font-size: 1.8rem;
  margin-bottom: 20px;
}

.form-label {
  font-weight: bold;
  color: #ffca28;
}

.form-control {
  border: 2px solid #ffca28;
  border-radius: 10px;
  padding: 10px;
}

.btn.submit-btn {
  background-color: #fbc02d;
  color: white;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 10px;
  border: none;
}

.btn.submit-btn:hover {
  background-color: #f9a825;
  cursor: pointer;
}

.preview-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 50%;
  margin-top: 15px;
  border: 3px solid #ffca28;
}

h3 {
  color: #f57f17;
}

img {
  margin-top: 10px;
  width: 50px;
}

</style>
