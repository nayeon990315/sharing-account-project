<template>
  <div class="container">
    <h1>Welcome! Please complete your profile</h1>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="nickname">Nickname:</label>
        <input type="text" id="nickname" v-model="nickname" class="form-control" placeholder="Enter your nickname"
          required />
      </div>

      <div class="form-group">
        <label for="image">Profile Image:</label>
        <input type="file" id="image" @change="onFileChange" class="form-control" accept="image/*" required />
      </div>

      <button type="submit" class="btn btn-primary mt-3">Submit</button>
    </form>

    <div v-if="previewImage">
      <h3>Preview:</h3>
      <img :src="previewImage" alt="Profile Image Preview" class="img-fluid" />
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Register',
  data() {
    return {
      nickname: '', // 닉네임
      image: null, // 업로드된 이미지 파일
      previewImage: null // 이미지 미리보기 URL
    };
  },
  methods: {
    // 파일 선택 시 호출되는 메서드
    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.image = file;
        this.previewImage = URL.createObjectURL(file); // 이미지 미리보기 URL 생성
        console.log("Selected file:", this.image);
      }
    },
    // 폼 제출 시 호출되는 메서드
    async submitForm() {
      if (!this.nickname || !this.image) {
        alert('Please complete the form.');
        return;
      }

      // 쿠키에서 jwtToken 가져오기 (this.$cookies.get())
      //토큰 없으면 로그인 페이지로 redirect
      const jwtToken = this.$cookies.get('jwtToken');
      if (!jwtToken) {
        alert('로그인이 필요합니다!');
        this.$router.push('/login'); // Vue Router를 사용하여 리다이렉트
        return;
      }
      console.log(jwtToken);

      // FormData 객체를 생성하여 닉네임과 이미지 파일을 추가
      const formData = new FormData();
      formData.append('nickname', this.nickname);
      formData.append('image', this.image);
      console.log('FormData:', formData);

      try {
        // axios로 서버에 요청을 보냄. 헤더에 Authorization에 Bearer 토큰을 포함
        const response = await axios.post('http://localhost:8080/users/updateProfile', formData, {
          headers: {
            'Authorization': `Bearer ${jwtToken}`, // JWT 토큰을 Authorization 헤더에 추가
            'Content-Type': 'multipart/form-data' // FormData 전송 시 Content-Type 설정
          }
        });

        console.log('Profile updated:', response.data);
        alert('프로필 수정이 완료되었습니다!');
        this.$router.push('/myroutine');
      } catch (error) {
        console.error('Error updating profile:', error);
        alert('Error updating profile.');
      }
    }
  }
};
</script>