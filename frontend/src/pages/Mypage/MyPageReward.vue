<template>
  <div class="my-page">
    <div class="profile-section">
      <img :src="profileImageUrl" alt="Profile" class="profile-image" />
      <div class="profile-info">
        <div class="nickname-container">
          <h2 id="nickname"><strong>{{ user.nickname }}</strong>님</h2>
          <button id="changeProfile" @click="openModal()">✎</button>
        </div>
        <p>오늘도 한 꿀씩 쌓아볼까요?</p>
      </div>
    </div>

    <div class="stats-section">
      <div class="stat">
        <p>지금까지 모은 꿀은</p>
        <h3><strong>{{ user.reward }}</strong>꿀</h3>
      </div>
      <div class="stat">
        <p>지금까지 이행한 루틴은</p>
        <h3><strong>{{ user.completedRoutines }}</strong>건</h3>
      </div>
      <div class="stat">
        <p>지금까지 절약한 금액은</p>
        <h3><strong>{{ user.savedAmount }}</strong>원</h3>
      </div>
    </div>

    <Modal :isVisible="isModalVisible" title="프로필 수정 완료" message="프로필 수정이 완료되었습니다." @close="closeAlertModal"/>

    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <h4 id="modal-title"><strong>프로필 수정</strong></h4>
        <div class="image-upload">
          <img :src="tempProfileImageUrl" 
               alt="Profile" 
               :class="{'modal-profile-image border-blue': user.avatar, 'modal-profile-image': !user.avatar}" 
               @click="triggerFileInput" />
          <input type="file" ref="fileInput" accept="image/*" @change="handleFileUpload" style="display: none;" />
          <img :src="profileImageUrl2" 
               alt="Profile Picture" 
               :class="{'default-profile-image border-blue': !user.avatar, 'default-profile-image': user.avatar}" 
               @click="updateImage('delete')" />
        </div>
        <div class="nickname-edit">
          <label for="nickname"></label>
          <input type="text" v-model="tempNickname" id="nickname" class="nickname-input" />
        </div>

        <div class="modal-actions">
          <button @click="saveProfile" class="btn btn-primary">저장</button>
          <button @click="closeModal" class="btn btn-secondary">취소</button>
        </div>
      </div>
    </div>

    <div class="content">
      <h5 id="modal-title"><strong>지금까지 이행한 루틴 및 절약 내역</strong></h5>
      <div class="routine-content">
        <ul>
          <li v-for="(item, index) in routine.title" :key="index">
           <hr>({{ routine.date[index] }})&nbsp;&nbsp; {{ item }}&nbsp; - {{ routine.save[index] }}원 절약
          </li>
        </ul>
      </div>
    </div>

    <div class="calendar-section">
      <Calendar :events="calendarEvents" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import defaultProfileImage from '@/assets/profile.png'; 
import profileImage from '@/assets/profile.png'; 
import Calendar from '@/components/Calendar.vue';
import Modal from '@/components/Modal.vue';

const API_URL = 'http://localhost:8080';

const user = ref({
  nickname: '',
  reward: 0,
  completedRoutines: 0,
  savedAmount: 0,
  avatar: '',
});

const routine = ref({
  date: [],
  title: [],
  save: [],
});

const calendarEvents = ref([]); 
const profileImageUrl = ref(defaultProfileImage);
const profileImageUrl2 = ref(profileImage);
const userId = ref(null);
const isModalOpen = ref(false);
const image = ref(null);
const tempNickname = ref('');
const tempProfileImageUrl = ref(null);
const isModalVisible = ref(false);

const getUserIdFromLocal = async () => {
  userId.value = localStorage.getItem('userId');
  await getUserInfo();
  await getCheckedHabitData();    
  await getCheckedHabit();         
};

const getUserInfo = async () => {
  if (!userId.value) {
    console.error('userId가 존재하지 않습니다.');
    return;
  }
  
  try {
    const response = await axios.get(`${API_URL}/users/mypage?userId=${userId.value}`);
    Object.assign(user.value, response.data);  
    tempNickname.value = user.value.nickname;
    profileImageUrl.value = user.value.avatar || defaultProfileImage;  
    console.log('아이디로 사용자 정보를 가져왔습니다.');
  } catch (error) {
    console.error('사용자 정보 요청 중 오류 발생:', error);
  }
};

const openModal = () => {
  isModalOpen.value = true;
  tempNickname.value = user.value.nickname;
  tempProfileImageUrl.value = profileImageUrl.value;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const saveProfile = async (action) => {
  const updateNicknamePromise = tempNickname.value !== user.value.nickname ? updateNickname() : Promise.resolve();
  const updateImagePromise = (image.value || action === 'delete') ? updateImage(action) : Promise.resolve();

  try {
    await Promise.all([updateNicknamePromise, updateImagePromise]);
    await getUserInfo(); 

    localStorage.setItem('nickname', tempNickname.value);

    isModalVisible.value = true;
    closeModal();
  } catch (error) {
    console.error('프로필 수정 중 오류가 발생했습니다:', error);
    isModalVisible.value = true;
  }
};

const updateNickname = async () => {
  try {
    await axios.put(`${API_URL}/users/updateNickname`, null, {
      params: {
        nickname: tempNickname.value,
        userId: userId.value,
      }
    });
    console.log('닉네임 수정이 완료되었습니다.');
  } catch (error) {
    console.error('닉네임 업데이트 중 오류 발생:', error);
  }
};

const updateImage = async (action) => {
  const formData = new FormData();

  if (action === 'delete') {
    formData.append('avatar', null);  
    tempProfileImageUrl.value = defaultProfileImage;
    image.value = null;  
  } else if (image.value) {
    formData.append('image', image.value);
    if (tempProfileImageUrl.value) {
      tempProfileImageUrl.value = tempProfileImageUrl.value;
    }
  }

  try {
    await axios.post(`${API_URL}/users/updateImage`, formData, {
      headers: {
        'userId': userId.value,
        'Content-Type': 'multipart/form-data',
      },
    });
    console.log('이미지 수정이 완료되었습니다.');
  } catch (error) {
    console.error('이미지 수정 중 오류가 발생했습니다:', error);
    throw error;
  }
};

const triggerFileInput = () => {
  document.querySelector('input[type=file]').click(); 
};

const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    image.value = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      tempProfileImageUrl.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const getCheckedHabitData = async () => {
  try {
    const [countResponse, moneyResponse] = await Promise.all([
      axios.get(`${API_URL}/habits/count/checked?userId=${userId.value}`),
      axios.get(`${API_URL}/habits/money/checked/all?userId=${userId.value}`)
    ]);

    user.value.completedRoutines = countResponse.data;
    user.value.savedAmount = moneyResponse.data;

    console.log('인증한 루틴 개수:', user.value.completedRoutines);
    console.log('인증한 루틴 금액:', user.value.savedAmount);
  } catch (error) {
    console.error('루틴 데이터 조회 중 오류 발생:', error);
  }
};

const getCheckedHabit = async () => {
  try {
    const response = await axios.get(`${API_URL}/habits/checked/all?userId=${userId.value}`);
    const habits = response.data;

    // 이전 데이터 초기화
    routine.value.date = [];
    routine.value.title = [];
    routine.value.save = [];
    calendarEvents.value = []; 

    habits.forEach(habit => {
      const checkDate = new Date(habit.checkDate);
      const formattedDate = checkDate.toISOString().split('T')[0];
      routine.value.date.push(formattedDate);
      routine.value.title.push(habit.habitTitle);
      routine.value.save.push(habit.saveAmount);


      calendarEvents.value.push({
        date: checkDate, 
        title: habit.habitTitle,
        save: habit.saveAmount,
      });
    });

    console.log('인증한 루틴 내역:', routine.value);
    console.log('달력 이벤트:', calendarEvents.value); 
  } catch (error) {
    console.error('루틴 내역 조회 중 오류 발생:', error);
  }
};

const closeAlertModal = () => {
  isModalVisible.value = false;
};

onMounted(getUserIdFromLocal);
</script>

<style scoped>
.my-page {
  padding: 30px;
}

.profile-section {
  display: flex;
  align-items: center;
  margin: 20px;
  border: 2px solid #ddd; 
  border-radius: 8px; 
  padding: 20px; 
  background-color: #f9f9f9; 
}

.profile-image {
  width: 140px;
  height: 150px;
  border-radius: 50%;
  margin-right: 30px;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid #ddd;
  transition: transform 0.3s ease, border-color 0.3s ease;
}

.nickname-container {
    display: flex; 
}

#changeProfile {
  margin-left: 5px;
  border: none;
  background-color: white;
}

.stats-section {
  display: flex;
  justify-content: space-between;
  margin-left: 20px;
  margin-right: 20px;
}

.stat {
  background-color: #f0f0f0;
  padding: 20px;
  width: 32%;
  border-radius: 5px;
  border: 2px solid #ddd; 
  background-color: #f9f9f9; 
  text-align: center;
}

.content {
  align-items: center;
  margin: 20px;
  border: 2px solid #ddd; 
  border-radius: 8px; 
  padding: 20px; 
  background-color: #f9f9f9;
}

.content ul {
  list-style-type: none; 
  margin-top: 30px;
  text-align: center;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; 
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 400px;
  text-align: center;
}

#modal-title{
  text-align: center;
  margin-top: 10px;
}

.image-upload {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 10px;
}

.default-profile-image {
  width: 70px;
  height: 70px;
  border-radius: 50%;
}

.modal-profile-image {
  width: 130px;
  height: 130px;
  border-radius: 50%;
  margin: 10px;
  object-fit: cover;
  cursor: pointer;
}

.default-profile-image {
  width: 130px;
  height: 130px;
  border-radius: 50%;
  margin: 10px;
  object-fit: cover;
  cursor: pointer;
}

.nickname-edit {
  margin-top: 20px;
}

.nickname-input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 100%;
  text-align: center;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.btn {
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 49%;
  transition: background-color 0.3s ease;
}

.modal-actions .btn {
  width: 100%;
}

.btn-primary {
  border: 3px solid #90893dcd;
  background-color: #90893dcd;
  color: white;
}

.btn-secondary {
  border: 3px solid #6c757d;
  background-color: #6c757d;
  color: white;
}

.btn-primary:hover {
  background-color: white;
  color: #90893dcd;
}

.btn-secondary:hover {
  background-color: white;
  color: #5a6268;
}

.border-blue {
  border: 4px solid #a0983ccd; 
}

.routine-content {
  max-height: 300px;
  overflow-y: auto;
}

.calendar-section {
  margin-top: 20px;
}
</style>
