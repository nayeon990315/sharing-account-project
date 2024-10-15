<template>
  <div class="mypage">
    <h1 v-if="!isEditing">{{ user.name }}님, 안녕하세요</h1>

    <div class="profile-container" v-if="!isEditing">
      <div class="profile-picture">
        <img :src="profileImageUrl" alt="Profile Picture" v-if="profileImageUrl" />
        <img v-else src="@/assets/images/bee/bee4.png">
      </div>

      <div class="profile-info">
        <p><strong>Name:</strong> {{ user.name }}</p>
        <p><strong>Nickname:</strong> {{ user.nickname }}</p> <!-- 추가된 부분 -->
        <p><strong>Email:</strong> {{ user.email }}</p>
        <p><strong>Phone:</strong> {{ user.phone }}</p>
        <button class="edit-btn" @click="enableEdit">정보 수정</button>
      </div>
    </div>

    <div v-else>
      <h2>회원 정보 수정</h2>
      <div class="profile-picture" @click="triggerFileInput">
        <img :src="profileImageUrl" alt="Profile Picture" v-if="profileImageUrl" />
        <p v-else>No profile picture</p>
      </div>

      <div class="profile-info-edit">
        <label><strong>Name:</strong></label>
        <input type="text" v-model="user.name" class="input-field" />
        
        <label><strong>Nickname:</strong></label> <!-- 추가된 부분 -->
        <input type="text" v-model="user.nickname" class="input-field" />
        
        <label><strong>Email:</strong></label>
        <input type="email" v-model="user.email" class="input-field" />
        
        <label><strong>Phone:</strong></label>
        <input type="text" v-model="user.phone" class="input-field" />
      </div>
      <div class="button-group">
        <button class="save-btn" @click="saveChanges">저장</button>
        <button class="cancel-btn" @click="cancelEdit">취소</button>
      </div>
    </div>

    <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" style="display: none;" />
  </div>
</template>

<script>
import profileImage from '@/assets/profile.jpg';

import axios from 'axios';


export default {
  data() {
    return {
      user: {

        name: 'John Doe',
        nickname: 'Johnny',
        email: 'john.doe@example.com',

        phone: '010-1234-5678',
      },
      profileImageUrl: profileImage,
      isEditing: false,
    };
  },
  methods: {
    enableEdit() {
      this.isEditing = true;  
    },
    saveChanges() {
      this.isEditing = false;  
    },
    cancelEdit() {
      this.isEditing = false;  
    },
    onFileChange(event) {
      const file = event.target.files[0];
      if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.profileImageUrl = e.target.result; 
        };
        reader.readAsDataURL(file);
      } else {
        alert('이미지를 선택해 주세요.');
      }
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    async getUserIdFromToken() {
            const jwtToken = this.$cookies.get('jwtToken');
            if (!jwtToken) {
                alert('로그인이 필요합니다!');
                this.$router.push('/login');
                return;
            }
            console.log(jwtToken); 
            try {
              
                const response = await axios.post('http://localhost:8080/users/findId', {}, {
                    headers: {
                        'Authorization': `Bearer ${jwtToken}` 
                    }
                });
                console.log(response.data)
            } catch (error) {
                console.error('사용자 정보를 가져오지 못했습니다:', error);
                if (error.response && error.response.status === 401) {
                    alert('인증 오류: 로그인이 필요합니다.');
                    this.$router.push('/login');
                }
            }
    }
  },
  mounted() {
        this.getUserIdFromToken();
  }
};
</script>

<style scoped>
.mypage {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
  font-family: 'Arial', sans-serif;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h1 {
  margin-bottom: 20px;
  color: #333;
}

.profile-container {
  display: flex;
  align-items: center; 
  margin-bottom: 20px;
}

.profile-picture {
  margin-right: 20px;
}

.profile-picture img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ddd;
}

.profile-info {
  text-align: left; 
}

.profile-info p {
  font-size: 16px;
  margin: 10px 0;
}

.edit-btn {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.edit-btn:hover {
  background-color: #0056b3;
}

.profile-info-edit {
  text-align: left;
  margin-bottom: 20px;
}

.profile-info-edit label {
  display: block;
  font-weight: bold;
  margin: 10px 0 5px;
}

.input-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  margin-bottom: 15px;
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.save-btn, .cancel-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.save-btn {
  background-color: #28a745;
  color: white;
}

.save-btn:hover {
  background-color: #218838;
}

.cancel-btn {
  background-color: #dc3545;
  color: white;
}

.cancel-btn:hover {
  background-color: #c82333;
}
</style>