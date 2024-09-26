<template>
  <div class="upload-container">
    <h1>UPLOAD</h1>
    <div class="form-group">
      <!-- <label for="title">제목</label> -->
      <!-- <input type="text" id="title" placeholder="제목을 입력하세요" /> -->
    </div>

    <div class="form-group">
      <label>카테고리</label>
      <p>나의 '벌루틴' 중 인증을 올릴 루틴 한 가지를 선택해주세요.</p>
      <div class="category">
        <div>
          <strong>#식비</strong>
          <div><input type="checkbox" /> 외식비 1만5천원 넘지 않기</div>
        </div>
        <div>
          <strong>#카페/간식</strong>
          <div><input type="checkbox" /> 저렴한 커피 대신 마시기</div>
        </div>
        <div>
          <strong>#교통</strong>
          <div><input type="checkbox" /> 택시 한 달에 1번만 타기</div>
          <div><input type="checkbox" /> 도보 30분 이하 걷기</div>
        </div>
      </div>
    </div>

    <div class="form-group">
      <label for="content">내용</label>
      <textarea
        id="content"
        placeholder="인증 내용을 작성해주세요. 인증할 루틴의 카테고리 태그는 자동 입력되며, 추가 해시태그는 자유롭게 입력할 수 있습니다!"
      ></textarea>
    </div>

    <div class="form-group">
      <label>사진 업로드</label>
      <!-- Hidden file input -->
      <input
        type="file"
        ref="fileInput"
        @change="handleFileUpload"
        style="display: none"
      />
      <!-- Button to trigger file input -->
      <button @click="triggerFileInput" class="photo-upload-button">+</button>
    </div>

    <button class="post-button">POST</button>

    <!-- Preview Section -->
    <div v-if="previewUrl">
      <h2>Preview:</h2>
      <img
        :src="previewUrl"
        alt="Preview"
        style="max-width: 100%; height: auto"
      />
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'Upload',
  setup() {
    const previewUrl = ref(null);

    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      if (file) {
        previewUrl.value = URL.createObjectURL(file);
      }
    };

    const triggerFileInput = () => {
      document.querySelector('input[type=file]').click();
    };

    return {
      handleFileUpload,
      triggerFileInput,
      previewUrl,
    };
  },
};
</script>

<style scoped>
.upload-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.form-group {
  margin-bottom: 20px;
}

label {
  font-weight: bold;
}

.category {
  margin-top: 10px;
}

.category div {
  margin-bottom: 5px;
}

textarea {
  width: 100%;
  height: 100px;
}

.photo-upload-button {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  background-color: #e0e4e8;
}

.post-button {
  background-color: #42b983;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
}
</style>
