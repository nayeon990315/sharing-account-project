<template>
  <div class="post">
    <img src="@/assets/POST PNG (1).png" alt="Post Image" />
    <div class="post-details">
      <p>출근 전 모닝커피! 스타벅스 대신 저렴한 커피로 마셔봐요.</p>
      <p>#카페/간식 #커피 #저렴한커피대신마시기 #모닝커피</p>

      <!-- 댓글 섹션 -->
      <comment-section></comment-section>

      <!-- 좋아요 및 댓글 수 -->
      <div class="like-button">❤️ 131 💬 27</div>
    </div>
  </div>
</template>

<script>
// CommentSection 컴포넌트 가져오기
import CommentSection from './CommentSection.vue';

export default {
  name: 'PostItem',
  components: {
    CommentSection,
  },
};
</script>

<style>
.post {
  margin-top: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  max-width: 600px; /* 적절한 너비 설정 */
  margin-left: auto;
  margin-right: auto;
}

.post img {
  width: 100%;
  border-radius: 10px;
}

.post-details {
  padding-top: 10px;
}

.like-button {
  margin-top: 10px;
  cursor: pointer;
}

.comment-section {
  margin-top: 20px;
}

.comment-section textarea {
  width: 100%;
  margin-bottom: 10px;
}
</style>
