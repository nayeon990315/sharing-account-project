<template>
  <div class="container upload-container">
    <form @submit.prevent="submitForm" class="border p-4 rounded bg-light">
      <h3>인증하기</h3>

      <!-- User ID 입력 -->
      <div class="mb-3">
        <label for="userId" class="form-label">User ID</label>
        <input type="text" v-model="formData.userId" id="userId" class="form-control" required />
      </div>

      <!-- 카테고리 선택 (habitId 선택) -->
      <div class="mb-3">
        <label class="form-label">카테고리</label>
        <p>나의 '벌루틴' 중 인증을 올릴 루틴 한 가지를 선택해주세요.</p>
        <div v-for="habit in habits" :key="habit.habitId" class="form-check">
          <input class="form-check-input" type="radio" :id="habit.habitId" :value="habit.habitId"
            v-model="formData.habitId" />
          <label class="form-check-label" :for="habit.habitId">{{ habit.habitName
            }}</label>
        </div>
      </div>

      <!-- 내용 입력 -->
      <div class="mb-3">
        <label for="content" class="form-label">내용</label>
        <textarea v-model="formData.content" id="content" class="form-control"
          placeholder="인증 내용을 작성해주세요. 인증할 루틴의 카테고리 태그는 자동 입력되며, 추가 해시태그는 자유롭게 입력할 수 있습니다!" required></textarea>
      </div>

      <!-- 해시태그 입력 -->
      <div class="mb-3">
        <label for="hashtag" class="form-label">Hashtag</label>
        <input type="text" v-model="formData.hashtag" id="hashtag" class="form-control" placeholder="#태그를 입력해주세요" />
      </div>

      <!-- 이미지 업로드 -->
      <div class="mb-3">
        <label class="form-label">사진 업로드</label>
        <div class="d-flex align-items-center">
          <input type="file" @change="handleImageUpload" class="form-control me-3" style="width: auto;" />
          <img v-if="imagePreview" :src="imagePreview" alt="Image preview" class="img-thumbnail ms-auto"
            style="width: 150px; height: 150px;" />
        </div>
      </div>

      <!-- 제출 버튼 -->
      <div class="d-flex justify-content-end">
        <button type="submit" class="btn btn-success">POST</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import axios from 'axios';

// 더미 데이터: 습관 목록
const habits = [
  { habitId: 1, habitName: '외식비 1만원씩 넘지 않기', categoryName: '식비' },
  { habitId: 2, habitName: '저렴한 커피 대신 마시기', categoryName: '카페/간식' },
  { habitId: 3, habitName: '택시 한 달에 1만원 타기', categoryName: '교통' },
  { habitId: 4, habitName: '수제 간식 만들기', categoryName: '취미/운동' },
];

// reactive 데이터: 폼 데이터 저장
const formData = reactive({
  userId: '',
  habitId: null,
  content: '',
  hashtag: '',
  image: null, // 이미지 파일 저장
});

// 이미지 미리보기 상태 저장
const imagePreview = ref(null);

// 이미지 업로드 핸들러
const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    formData.image = file; // 이미지 파일 저장
    const reader = new FileReader();
    reader.onload = (e) => {
      imagePreview.value = e.target.result; // 이미지 미리보기
    };
    reader.readAsDataURL(file); // 이미지 미리보기 데이터 URL 생성
  }
};

// 폼 제출 핸들러
const submitForm = async () => {
  // FormData 객체를 생성하여 multipart/form-data 요청 준비
  const data = new FormData();
  data.append('userId', formData.userId);
  data.append('habitId', formData.habitId);
  data.append('content', formData.content);
  data.append('hashtag', formData.hashtag);
  data.append('image', formData.image); // 이미지 파일 추가

  try {
    // 서버에 요청 전송
    const response = await axios.post('http://localhost:8080/post-community/add', data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (response.status === 200) {
      alert('Post created successfully!');
    }
  } catch (error) {
    console.error('Error submitting form', error);
    alert('Failed to create post.');
  }
};
</script>

<style scoped>
.upload-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
}

img {
  object-fit: cover;
}

.form-control {
  border-radius: 8px;
}

.btn-success {
  background-color: #42b983;
}
</style>