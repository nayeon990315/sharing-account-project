<template>
  <div class="container upload-container">
    <form @submit.prevent="submitForm" class="border p-4 rounded bg-light">
      <h3>인증하기</h3>

      <!-- <div class="mb-3">
        <label for="userId" class="form-label">인증방법</label>
        <input type="text" v-model="formData.s" id="userId" class="form-control" required />
      </div> -->

      <div class="mb-3">
        <label for="userId" class="form-label">User ID(테스트용)</label>
        <input type="text" v-model="formData.userId" id="userId" class="form-control" required />
      </div>

      <div class="mb-3">
        <label for="myHabitId" class="form-label">My Habit ID(테스트용)</label>
        <input type="text" v-model="formData.myHabitId" id="myHabitId" class="form-control" required />
      </div>

      <div class="mb-3">
        <label for="myHabitId" class="form-label">Habit ID(테스트용)</label>
        <input type="text" v-model="formData.habitId" id="habitId" class="form-control" required />
      </div>

      <div v-for="habit in filteredHabits" :key="habit.myHabitId" class="form-check">
        <input
          class="form-check-input"
          type="radio"
          :id="habit.myHabitId"
          :value="habit.myHabitId"
          v-model="formData.myHabitId"
          :disabled="habit.isCheckedToday"
        />
        <label
          class="form-check-label"
          :for="habit.myHabitId"
          :class="{ 'text-decoration-line-through': habit.isCheckedToday }"
        >
          {{ habit.habitTitle }} : 인증방법: {{ habit.certification || 'Null 인 경우' }}
        </label>
      </div>

      <div class="mb-3">
        <label for="content" class="form-label">내용</label>
        <textarea
          v-model="formData.content"
          id="content"
          class="form-control"
          placeholder="인증 내용을 작성해주세요."
          required
        ></textarea>
      </div>

      <div class="mb-3">
        <label for="hashtag" class="form-label">Hashtag</label>
        <input
          type="text"
          v-model="formData.hashtag"
          id="hashtag"
          class="form-control"
          placeholder="#태그를 입력해주세요"
        />
      </div>

      <div class="mb-3">
        <label class="form-label">사진 업로드</label>
        <div class="d-flex align-items-center">
          <input type="file" @change="handleImageUpload" class="form-control me-3" />
          <img v-if="imagePreview" :src="imagePreview" alt="Image preview" class="img-thumbnail ms-auto" style="width: 150px; height: 150px;" />
        </div>
      </div>

      <div class="d-flex justify-content-end">
        <button type="submit" class="btn btn-success">POST</button>
      </div>
    </form>
  </div>
</template>


<script setup>
import { computed, reactive, ref, onMounted, watch } from 'vue';
import { useHabitStore } from '@/stores/habitStore';
import axios from 'axios';

// 로컬 스토리지에서 userId 가져오기
onMounted(() => {
  const storedUserId = localStorage.getItem('userId');
  if (storedUserId) {
    formData.userId = storedUserId;
  }
  formData.myHabitId = habitStore.selectedMyHabitId;
  formData.habitId = findSelectedHabit.value[0].habitId;
  console.log('writerId: ', findSelectedHabit.value[0].writerId)
});

const habitStore = useHabitStore();

const formData = reactive({
  myHabitId: '',
  userId: '',
  habitId: '',
  content: '',
  hashtag: '',
  image: null, // 이미지 파일 저장
});

// '진행' 상태인 습관만 필터링
const filteredHabits = computed(() => {
  return habitStore.habits.filter(habit => habit.state === '진행');
});

// Pinia의 selectedMyHabitId가 바뀔 때 formData.myHabitId에 반영되도록 watch 사용
watch(() => habitStore.selectedMyHabitId, (newValue) => {
  formData.myHabitId = newValue;
  formData.habitId = findSelectedHabit.value[0].habitId;
  // formData.habitId = habitStore.useFilteredHabit;
  // console.log(habitStore.useFilteredHabit(myHabitId), "useFiltered")
});

const findSelectedHabit = computed(() => {
  return habitStore.habits.filter(habit => habit.myHabitId === formData.myHabitId);
});

// myHabitId에 해당하는 habitId 찾기
// const { filteredHabit } = useFilteredHabit(myHabitId);

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
  const data = new FormData();
  data.append('myHabitId', formData.myHabitId);
  data.append('userId', formData.userId);
  data.append('habitId', formData.habitId);
  data.append('content', formData.content);
  data.append('hashtag', formData.hashtag);
  data.append('image', formData.image);

  try {
    const response = await axios.post('http://localhost:8080/post-community/add', data, {
      headers: { 'Content-Type': 'multipart/form-data' },
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