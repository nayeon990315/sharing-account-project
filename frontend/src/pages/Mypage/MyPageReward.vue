<template>
  <div class="my-page">
    <div class="profile-section">
      <img :src="profileImageUrl" alt="Profile" class="profile-image" />
      <div class="profile-info">
        <div class="nickname-container">
          <h2 id="nickname"><strong>{{ user.nickname }}</strong>님</h2>
          <button id="changeProfile" @click="openModal('profileEdit')">✎</button>
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
        <h3 @click="openModal('secondModal')"><strong>{{ user.completedRoutines }}</strong>건</h3>
      </div>
      <div class="stat">
        <p>지금까지 절약한 금액은</p>
        <h3><strong>{{ user.savedAmount }}</strong>원</h3>
      </div>
    </div>

    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <h4 id="modal-title"><strong>프로필 수정</strong></h4>
        <div class="image-upload">
          <img :src="profileImageUrl" 
               alt="Profile" 
               :class="{'modal-profile-image border-blue': user.avatar, 'modal-profile-image': !user.avatar}" 
               @click="triggerFileInput" />
          <input type="file" ref="fileInput" accept="image/*" @change="handleFileUpload" style="display: none;" />
          <img :src="profileImageUrl2" 
               alt="Profile Picture" 
               v-if="profileImageUrl2" 
               :class="{'default-profile-image border-blue': !user.avatar, 'default-profile-image': user.avatar}" 
               @click="submitForm('delete')" />
        </div>
        <div class="nickname-edit">
          <label for="nickname"></label>
          <input type="text" v-model="user.nickname" id="nickname" class="nickname-input" />
        </div>

        <div class="modal-actions">
          <button @click="saveProfile" class="btn btn-primary">저장</button>
          <button @click="closeModal" class="btn btn-secondary">취소</button>
        </div>
      </div>
    </div>

    <div>

    <div class="content">
      <h5 id="modal-title"><strong>지금까지 이행한 루틴 및 절약 내역</strong></h5>
      <ul>
        <li v-for="(item, index) in routine.title" :key="index">
          <hr>({{ routine.date[index] }})&nbsp;&nbsp; {{ item }}&nbsp; -&nbsp; {{ routine.save[index] }}원 절약
        </li>
      </ul>
  </div>
</div>

  </div>
</template>

<script>
import axios from 'axios';
import defaultProfileImage from '@/assets/profile.png'; // 기본 이미지
import profileImage from '@/assets/profile.png';

export default {
  data() {
    return {
      user: {
        nickname: '',
        reward: 0,
        completedRoutines: 0,
        savedAmount: 0,
        avatar: '',
      },
      routine: {
        date: [],
        title: [],
        save: [],
      },
      profileImageUrl: defaultProfileImage,
      profileImageUrl2: profileImage,
      userId: null,
      isModalOpen: false,
      isSecondModalOpen: false,  
      image: null,
    };
  },
  methods: {
    async getUserIdFromToken() {
      const jwtToken = this.$cookies.get('jwtToken');
      if (!jwtToken) {
        alert('로그인이 필요합니다.');
        this.$router.push('/login');
        return;
      }
      try {
        const response = await axios.post('http://localhost:8080/users/findId', {}, {
          headers: {
            'Authorization': `Bearer ${jwtToken}`,
          },
        });
        this.userId = response.data.userId;
        this.getUserInfo();  
        await this.getCheckedHabitCount(); 
        this.getCheckedHabitMoney();
        this.getCheckedHabit();
      } catch (error) {
        console.error('사용자 정보를 가져오지 못했습니다:', error);
        if (error.response && error.response.status === 401) {
          alert('인증 오류: 로그인이 필요합니다.');
          this.$router.push('/login');
        }
      }
    },

    async getUserInfo() {
      if (!this.userId) {
        console.error('userId가 존재하지 않습니다.');
        return;
      }
      try {
        const response = await axios.get(`http://localhost:8080/users/mypage?userId=${this.userId}`);
        this.user = response.data;  

        if (this.user.avatar) {
          this.profileImageUrl = this.user.avatar;
        } else {
          this.profileImageUrl = defaultProfileImage;  
        }
        console.log('아이디로 사용자 정보를 가져왔습니다.');
      } catch (error) {
        console.error('사용자 정보 요청 중 오류 발생:', error);
      }
    },

    openModal(modalType) {
      if (modalType === 'profileEdit') {
        this.isModalOpen = true;
      } else if (modalType === 'secondModal') {
        this.getCheckedHabit();
        this.isSecondModalOpen = true;
      }
    },

    closeModal() {
      this.isModalOpen = false;
    },

    closeSecondModal() {
      this.isSecondModalOpen = false;
    },

    async saveProfile() {
      await this.saveNickname();
      await this.submitForm();
      this.closeModal(); 
    },

    saveNickname() {
      return axios.put(`http://localhost:8080/users/updateNickname`, null, {
        params: {
          nickname: this.user.nickname,
          userId: this.userId,
        }
      })
      .then(response => {
        console.log('닉네임이 업데이트되었습니다:', response.data);
      })
      .catch(error => {
        console.error('닉네임 업데이트 중 오류 발생:', error);
      });
    },

    async submitForm(action) {
      const jwtToken = this.$cookies.get('jwtToken');
      if (!jwtToken) {
        alert('로그인이 필요합니다.');
        this.$router.push('/login');
        return;
      }

      const formData = new FormData();
      
      if (action === 'delete') {
        formData.append('avatar', null);  
        this.profileImageUrl = defaultProfileImage;  
        this.image = null;  
      } else if (this.image) {
        formData.append('image', this.image);
      }

      try {
        const response = await axios.post('http://localhost:8080/users/updateImage', formData, {
          headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'multipart/form-data',
          },
        });

        console.log('Profile updated:', response.data);
        if (action !== 'delete') {
          alert('프로필 수정이 완료되었습니다.');
        }
        this.getUserInfo();  
      } catch (error) {
        console.error('Error updating profile:', error);
        alert('프로필 수정 중 오류가 발생했습니다.');
      }
    },

    triggerFileInput() {
      this.$refs.fileInput.click();
    },

    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.image = file;
        const reader = new FileReader();
        reader.onload = (e) => {
          this.profileImageUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },

    async getCheckedHabitCount() {
      try {
        const response = await axios.get(`http://localhost:8080/habits/count/checked?userId=${this.userId}`);
        this.user.completedRoutines = response.data;
        console.log('인증한 습관 개수:', this.user.completedRoutines);
      } catch (error) {
        console.error('습관 개수 조회 중 오류 발생:', error);
      }
    },

    async getCheckedHabitMoney() {
      try {
        const response = await axios.get(`http://localhost:8080/habits/money/checked?userId=${this.userId}`);
        this.user.savedAmount = response.data;
        console.log('인증한 습관 금액:', this.user.savedAmount);
      } catch (error) {
        console.error('습관 금액 조회 중 오류 발생:', error);
      }
    },

    async getCheckedHabit() {
      try {
        const response = await axios.get(`http://localhost:8080/habits/checked/all?userId=${this.userId}`);
        const habits = response.data;

        this.routine.date = [];
        this.routine.title = [];
        this.routine.save = [];

        habits.forEach(habit => {
          const checkDate = new Date(habit.checkDate);  
          const formattedDate = checkDate.toISOString().split('T')[0];
          this.routine.date.push(formattedDate);
          this.routine.title.push(habit.habitTitle);
          this.routine.save.push(habit.saveAmount);
        });

        console.log('날짜:', this.routine.date);
        console.log('날짜:', this.routine.title);
        console.log('금액:', this.routine.save);

      } catch (error) {
        console.error('습관 조회 중 오류 발생:', error);
      }
    },
  },

  mounted() {
    this.getUserIdFromToken(); 
  },
};
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

.modal-content2 {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 500px;
  text-align: center;
}

.modal-content2 ul {
  list-style-type: none; 
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
</style>/