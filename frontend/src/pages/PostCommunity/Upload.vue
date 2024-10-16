<template>
  <div class="full-screen-container">
    <div class="container upload-container border rounded">
      <form @submit.prevent="submitForm" class="p-4">
        <h3 class="upload-title">Upload</h3>
        <label class="input-label section-title">카테고리</label>
        <p class="subtitle">
          참여 중인 내 별루틴 중에서 인증할 1개의 카테고리를 골라주세요.
        </p>
        <div class="list-group mb-4">
          <div
            v-for="habit in filteredHabits"
            :key="habit.myHabitId"
            class="list-group-item list-group-item-action"
            :class="{
              'disabled-list': habit.isCheckedToday,
              'active border list-group-item-light':
                formData.myHabitId === habit.myHabitId && !habit.isCheckedToday,
            }"
            @click="
              !habit.isCheckedToday && (formData.myHabitId = habit.myHabitId)
            "
          >
            <div class="d-flex w-100 justify-content-between">
              <h6
                class="mb-1"
                :class="{
                  'text-decoration-line-through': habit.isCheckedToday,
                }"
              >
                {{ habit.habitTitle }}
              </h6>
            </div>
            <small
              :class="{ 'text-decoration-line-through': habit.isCheckedToday }"
            >
              {{ habit.certification || '자유롭게 작성해주세요.' }}
            </small>
          </div>
        </div>

        <div class="mb-4">
          <label for="content" class="form-label section-title">내용</label>
          <textarea
            v-model="formData.content"
            id="content"
            class="form-control"
            placeholder="인증 내용을 작성해주세요."
            required
          ></textarea>
        </div>

        <div class="mb-4">
          <label class="input-label section-title">해시태그</label>
          <p class="subtitle">
            인증할 벌루틴의 기본 카테고리는 해시태그로 자동
            입력되며(ex.카페/간식),<br />
            그 밖에 추가 해시태그는 자유롭게 작성해주세요.
          </p>
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
              @keyup.enter.prevent
            >
              <p>#{{ tag }}</p>
              <p class="hash-item-delete">x</p>
            </div>

            <input
              class="form-control input-tag"
              type="text"
              id="hashtag"
              v-model="hashtag"
              @keyup.space.prevent
              @keyup.delete.prevent
              :placeholder="
                hashArr.length < 5
                  ? '해시태그를 스페이스바를 눌러 추가하세요 (최대 5개)'
                  : '최대 5개까지만 입력이 가능합니다'
              "
              :disabled="hashArr.length >= 5"
            />
          </div>
        </div>

        <div class="mb-4">
          <label class="form-label section-title">사진 업로드</label>
          <div class="d-flex gap-3">
            <div class="flex-grow-1">
              <input
                type="file"
                @change="handleImageUpload"
                class="form-control"
                ref="fileInput"
              />
            </div>

            <div
              class="flex-shrink-0 position-relative d-flex justify-content-center align-items-center"
              style="width: 150px; height: 150px; cursor: pointer"
              @click.prevent.stop
            >
              <img
                v-if="imagePreview"
                :src="imagePreview"
                alt="Image preview"
                class="img-thumbnail ms-auto"
                style="width: 100%; height: 100%"
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

        <div class="d-flex justify-content-end mt-4">
          <button type="submit" class="btn btn-dark" :disabled="!isFormValid">
            POST
          </button>
        </div>
      </form>
    </div>
  </div>

  <!-- Alert Modal -->
  <RewardModal
    :isVisible="isModalVisible"
    title="알림"
    :message="modalMessage"
    :rewardBefore="originalReward"
    :rewardAfter="calculatedReward"
    @close.prevent.stop
  />
</template>

<script setup>
import { computed, reactive, ref, onMounted, watch } from 'vue';
import { useHabitStore } from '@/stores/habitStore';
import axios from 'axios';
import RewardModal from '@/components/RewardModal.vue';

// 로컬 스토리지에서 userId 가져오기
onMounted(() => {
  const storedUserId = localStorage.getItem('userId');
  if (storedUserId) {
    formData.userId = storedUserId;
  }
  formData.myHabitId = habitStore.selectedMyHabitId;
  formData.habitId = findSelectedHabit.value[0].habitId;
  hashArr.value.push(findSelectedHabit.value[0].categoryTitle);
  console.log('writerId: ', findSelectedHabit.value[0].writerId);
  console.log('category: ', findSelectedHabit.value[0].categoryTitle);
});

const isModalVisible = ref(false);
const modalMessage = ref('');
const originalReward = ref(0);
const calculatedReward = ref(0);
const userReward = ref(0);

// Modal 표시 함수
const openModal = (message, rewardBefore, rewardPlus) => {
  modalMessage.value = message;
  isModalVisible.value = true;
  originalReward.value = rewardBefore;
  calculatedReward.value = rewardBefore + rewardPlus;
};

// Modal 닫기 함수
const closeModal = () => {
  isModalVisible.value = false;
};

const hashtag = ref('');
const hashArr = ref([]);

const habitStore = useHabitStore();

const formData = reactive({
  myHabitId: '',
  userId: '',
  habitId: '',
  content: '',
  hashtag: '',
  image: null,
});

const filteredHabits = computed(() => {
  return habitStore.habits.filter((habit) => habit.state === '진행');
});

watch(
  () => habitStore.selectedMyHabitId,
  (newValue) => {
    formData.myHabitId = newValue;
    formData.habitId = findSelectedHabit.value[0].habitId;
    hashArr.value = [];
    hashArr.value.push(findSelectedHabit.value[0].categoryTitle);
    console.log(hashArr.value);
  }
);

const findSelectedHabit = computed(() => {
  return filteredHabits.value.filter(
    (habit) => habit.myHabitId === formData.myHabitId
  );
});

const imagePreview = ref(null);
const fileInput = ref(null);

const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    formData.image = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      imagePreview.value = e.target.result;
    };
    reader.readAsDataURL(file);
  } else {
    imagePreview.value = null;
    formData.image = null;
  }
};

const submitForm = async () => {
  formData.hashtag = hashArr.value
    .map((tag) => (tag.startsWith('#') ? tag : `#${tag}`))
    .join(' ');

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
    const response = await axios.post(
      'http://localhost:8080/post-community/add',
      data,
      {
        headers: { 'Content-Type': 'multipart/form-data' },
      }
    );

    if (response.status === 200) {
      console.log(response.data);
      const targetHabit = habitStore.habits.filter(
        (item) => item.habitId === formData.habitId
      )[0];

      const userResponse = await axios.get(
        'http://localhost:8080/users/mypage',
        {
          params: { userId: localStorage.getItem('userId') },
          headers: { 'Content-Type': 'application/json' },
        }
      );

      userReward.value = userResponse.data.reward;

      let message;
      if (response.data == 10) {
        message = targetHabit.habitTitle + ' 루틴을 인증해 꿀을 얻었습니다!';
        openModal(message, userReward.value, response.data);
      } else if (response.data == 20) {
        message = targetHabit.habitTitle + ' 루틴을 인증해 꿀을 얻었습니다!';
        openModal(message, userReward.value, response.data);
      }

      await habitStore.getHabitsFromServer(formData.userId);
    }
  } catch (error) {
    console.error('Error submitting form', error);
    alert('Failed to create post.');
  }
};

const triggerFileInput = () => {
  fileInput.value.click();
};

const isFormValid = computed(() => {
  return !(
    formData.content.trim() === '' ||
    hashArr.value.length === 0 ||
    formData.image === null ||
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
.full-screen-container {
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  background-color: rgb(255, 255, 255, 0.2);
}
.upload-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;

  background-color: rgb(249, 249, 249);
  border-radius: 10px;
}
.upload-title {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}
.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-top: 20px;
  margin-bottom: 10px;
}
.subtitle {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px; /* 소제목 아래 여백 */
}
.form-control {
  border-radius: 8px;
  margin-bottom: 15px;
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

.list-group-item-light {
  background-color: #f7d794;
  color: rgb(0, 0, 0);
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
  padding: 5px10px;
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
.list-group-item-light {
  background-color: #f7d794;
  color: rgb(0, 0, 0);
}
</style>
