<template>
  <div class="container upload-container">
    <form @submit.prevent="submitForm" class="border p-4 rounded bg-light">
      <h3>인증하기</h3>
      <div class="list-group">
        <div
          v-for="habit in filteredHabits"
          :key="habit.myHabitId"
          class="list-group-item list-group-item-action"
          :class="{ 
            'disabled-list': habit.isCheckedToday, 
            'active border list-group-item-light': (formData.myHabitId === habit.myHabitId) && !habit.isCheckedToday,
          }"
          @click="!habit.isCheckedToday && (formData.myHabitId = habit.myHabitId)"
        >
          <div class="d-flex w-100 justify-content-between">
            <h6 class="mb-1" :class="{ 'text-decoration-line-through': habit.isCheckedToday }">
              {{ habit.habitTitle }}
            </h6>  
          </div>
          <small :class="{ 'text-decoration-line-through': habit.isCheckedToday }">
            {{ habit.certification || '자유롭게 작성해주세요.' }}
          </small>
        </div>
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
        <label class="input-label">해시태그</label>
        <div class="hash-wrapper">
          <div
            class="hash-item"
            v-for="(tag, index) in hashArr"
            :key="index"
            @click="removeHashTag(index)"
            draggable="true"
            @dragstart="onDragStart(index, $event)"
            @dragover="onDragOver(index, $event)"
            @drop="onDrop(index, $event)"
            @keyup.enter="preventSubmit"
          >
            <p>#{{ tag }}</p>
            <p class="hash-item-delete">x</p>
          </div>

          <input
            class="form-control input-tag"
            type="text"
            id="hashtag"
            v-model="hashtag"
            @keyup.space="onKeyUpSpace"
            @keyup.delete="onKeyUpBackspace"
            :placeholder="hashArr.length < 5 ? '해시태그를 스페이스바를 눌러 추가하세요 (최대 5개)' : '최대 5개까지만 입력이 가능합니다'"
            :disabled="hashArr.length >= 5"
          />
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">사진 업로드</label>
        <div class="d-flex gap-3">
          <div class="flex-grow-1">
            <input type="file" @change="handleImageUpload" class="form-control" ref="fileInput"/>
          </div>
      
          <div
            class="flex-shrink-0 position-relative d-flex justify-content-center align-items-center"
            style="width: 150px; height: 150px; cursor: pointer;"
            @click="triggerFileInput"
          >
            <img 
              v-if="imagePreview" 
              :src="imagePreview" 
              alt="Image preview" 
              class="img-thumbnail ms-auto" 
              style="width: 100%; height: 100%;" 
            />
            
            <div 
              v-if="!imagePreview" 
              class="img-thumbnail ms-auto d-flex justify-content-center align-items-center" 
              style="width: 150px; height: 150px"
            >
              <span class="display-4 text-muted">+</span>
            </div>
          </div>
        </div>
      </div>

      <div class="d-flex justify-content-end">
        <button type="submit" class="btn"
        :class="{ 
          'disabled': !isFormValid,
          'btn-outline-dark': !isFormValid,
          'btn-dark': isFormValid,
        }"
        @keyup.enter="preventSubmit"
        >POST</button>
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

const hashtag = ref(''); // 현재 입력 중인 태그
const hashArr = ref([]); // 태그 배열

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
});

const findSelectedHabit = computed(() => {
  return habitStore.habits.filter(habit => habit.myHabitId === formData.myHabitId);
});

// 이미지 미리보기 상태 저장
const imagePreview = ref(null);
const fileInput = ref(null);

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
  } else {
    imagePreview.value = null; // 파일이 없으면 미리보기 초기화
    formData.image = null; // 이미지 데이터도 초기화
  }
};

// 폼 제출 핸들러
const submitForm = async () => {
  formData.hashtag = hashArr.value.join(',');

  const data = new FormData();
  data.append('myHabitId', formData.myHabitId);
  data.append('userId', formData.userId);
  data.append('habitId', formData.habitId);
  data.append('content', formData.content);
  data.append('hashtag', formData.hashtag);
  data.append('image', formData.image);

  if (!isFormValid.value) {
    console.error('Error submitting form');
    return;
  }
  
  try {
    const response = await axios.post('http://localhost:8080/post-community/add', data, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });

    if (response.status === 200) {
      alert('Post created successfully!');
      await habitStore.getHabitsFromServer(formData.userId);
    }
  } catch (error) {
    console.error('Error submitting form', error);
    alert('Failed to create post.');
  }
};

// 이미지 클릭해서 파일 업로드
const triggerFileInput = () => {
  fileInput.value.click();
};

// 폼 제출 검증
const isFormValid = computed(() => {
  return !(
    formData.content.trim() === '' ||
    hashArr.value.length === 0     ||
    formData.image === null        ||
    findSelectedHabit.value[0].isCheckedToday
  );
});

// 엔터 이벤트 방지
const preventSubmit = (event) => {
  if (event.key === 'Enter') {
    event.preventDefault(); // Enter 키 제출 방지
  }
};

// 스페이스바로 태그 생성
const onKeyUpSpace = () => {
  if (hashtag.value.trim() && !hashArr.value.includes(hashtag.value.trim())) {
    if (hashArr.value.length < 5) {
      hashArr.value.push(hashtag.value.trim());
      hashtag.value = ''; // 입력값 초기화
    }
  }
};

// 태그 클릭 시 삭제
const removeHashTag = (index) => {
  if (index >= 0 && index < hashArr.value.length) {
    hashArr.value.splice(index, 1);
  }
};

// 백스페이스로 마지막 태그 삭제
const onKeyUpBackspace = (e) => {
  if (e.code === 'Backspace' && hashtag.value === '') {
    if (hashArr.value.length > 0) {
      hashArr.value.pop();
    }
  }
};

// 드래그 앤 드롭 이벤트
const onDragStart = (index, event) => {
  event.dataTransfer.setData('tagIndex', index);
};

const onDragOver = (index, event) => {
  event.preventDefault();
};

const onDrop = (index, event) => {
  event.preventDefault();
  const draggedIndex = event.dataTransfer.getData('tagIndex');
  const draggedTag = hashArr.value[draggedIndex];

  hashArr.value.splice(draggedIndex, 1);
  hashArr.value.splice(index, 0, draggedTag);
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
.disabled-list {
  pointer-events: none;
  opacity: 0.6;
  background-color: #eeefe9;
}
.list-group-item-custom {
  background-color: blue;
  color: gray;
}
.text-small-custom {
  color: white;
}
.hash-wrapper {
  display: flex;
  flex-wrap: wrap;
}
.hash-item {
  background-color: #e0e0e0;
  border-radius: 8px;
  padding: 5px 10px;
  margin-right: 5px;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
}
.hash-item-delete {
  margin-left: 10px;
  color: red;
  cursor: pointer;
}
.input-tag {
  padding: 5px;
  border: 1px solid #ddd;
}
</style>