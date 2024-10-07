<template>
  <div class="my-page">
    <h1>My Page</h1>

    <div class="profile-section">
      <img :src="profileImageUrl" alt="Profile" class="profile-image" @click="triggerFileInput" />
      <div class="profile-info">
        <h2>{{ user.nickname }} 님</h2>
        <p>오늘도 한 꿀씩 쌓아볼까요?</p>
        <button @click="isEditing = true">닉네임 수정</button>
      </div>
    </div>

    <div v-if="isEditing" class="nickname-edit">
      <label for="nickname">닉네임 수정:</label>
      <input type="text" v-model="user.nickname" id="nickname" />
      <button @click="saveNickname">저장</button>
      <button @click="isEditing = false">취소</button>
    </div>

    <div class="stats-section">
      <div class="stat">
        <p>지금까지 벌꿀님이 모은 꿀은</p>
        <h3>{{ user.reward }} 꿀</h3>
      </div>
      <div class="stat">
        <p>지금까지 이행한 루틴은</p>
        <h3>{{ user.completedRoutines }}건</h3>
      </div>
      <div class="stat">
        <p>지금까지 절약한 금액은</p>
        <h3>{{ user.savedAmount }}원</h3>
      </div>
    </div>

    <button class="save-button">꿀 단지 넣기</button>
    <input type="file" ref="fileInput" accept="image/*" @change="handleFileUpload" style="display: none;" />
  </div>
</template>

<script>
import axios from 'axios';
import profileImage from '@/assets/images/profile-image.jpg'; 

export default {
  data() {
    return {
      user: {
        name: '',
        nickname: '', // 초기값은 빈 문자열
        reward: '',
        completedRoutines: 0,
        savedAmount: 0,
        avatar: '',
      },
      profileImageUrl: profileImage, 
      userId: null, 
      isEditing: false, 
    };
  },
  methods: {
    saveNickname() {
      // user.nickname을 사용하여 닉네임 업데이트
      axios.put(`http://localhost:8080/users/updateNickname`, null, {
        params: {
          nickname: this.user.nickname, // 수정된 닉네임 사용
          userId: this.userId, 
        }
      })
      .then(response => {
        console.log('닉네임이 업데이트되었습니다:', response.data);
        this.isEditing = false; // 수정 모드 종료
      })
      .catch(error => {
        console.error('닉네임 업데이트 중 오류 발생:', error);
      });
    },
    triggerFileInput() {
      this.$refs.fileInput.click(); 
    },
    handleFileUpload(event) {
      const file = event.target.files[0]; 
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.profileImageUrl = e.target.result; 
        };
        reader.readAsDataURL(file); 
      }
    },
    async getUserIdFromToken() {
      const jwtToken = this.$cookies.get('jwtToken');
      if (!jwtToken) {
        alert('로그인이 필요합니다!');
        this.$router.push('/login');
        return;
      }
      try {
        const response = await axios.post('http://localhost:8080/users/findId', {}, {
          headers: {
            'Authorization': `Bearer ${jwtToken}` 
          }
        });
        this.userId = response.data.userId;    
        this.getUserInfo();                    
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
        console.log(response.data);
      } catch (error) {
        console.error('사용자 정보 요청 중 오류 발생:', error);
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
.nickname-edit {
  margin-top: 20px;
}
.nickname-edit label {
  display: block;
  margin-bottom: 5px;
}
.nickname-edit input {
  margin-bottom: 10px;
  padding: 5px;
}
.save-button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 20px;
}
</style>