<template>
  <div class="container">
    <h1>Explore</h1>
    <div class="category-tags">
      <button
        v-for="category in categoryOptions"
        :key="category"
        @click="handleCategoryFilterChange(category)"
        :class="{ active: selectedCategory === category }"
      >
        {{ category === '참여루틴' ? category : `#${category}` }}
      </button>
    </div>
    <div v-for="post in filteredPosts" :key="post.postId" class="post">
      <div class="user-info">
        <img :src="userIcon" alt="User Icon" class="user-icon" />
        <span>{{ post.userName }}</span>
        <!-- userId 대신 userName 사용 -->
      </div>
      <img :src="post.imageURL" alt="Post Image" class="post-image" />
      <div class="post-details">
        <p>{{ post.content }}</p>
        <p>{{ post.hashtag }}</p>
        <p>{{ new Date(post.createdAt).toLocaleDateString() }}</p>
        <!-- 댓글 섹션 -->
         <!-- 자식 comment-section import해서 전달받음  -->
        <comment-section v-if="post.showComments" :post-id="post.id" @comment-change="handleCommentChange"></comment-section>
        <div class="interaction-buttons">
          <div class="like-button" @click="toggleLike(post)">
            <span :class="{ liked: post.isLiked }">{{
              post.isLiked ? '❤️' : '🤍'
            }}</span>
            {{ post.postLikes }}
          </div>
          <div class="comment-button" @click="toggleComments(post)">
            <span class="comment-icon">💬</span>
            {{ commentCounts[post.id] || 0 }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed,  onMounted } from 'vue';
import axios from 'axios';

import CommentSection from '@/components/PostCommunity/CommentSection.vue';
import userIcon from '@/assets/icons8-user-64.png'; // 사용자 아이콘 불러오기

const existingPosts = ref([]); // 게시물 목록
const commentCounts = ref({}); // 게시물별 댓글 수 저장

const selectedCategory = ref('전체');

const categoryOptions = [
  '참여루틴',
  '식비',
  '카페/간식',
  '쇼핑',
  '교통',
  '문화/여가',
  '술/유흥',
  '주거/공과금',
  '기타',
];

const posts = ref([]);

const getAllPost = async () => {
  try {
    const response = await axios.get(
      'http://localhost:8080/post-community/all'
    );
    posts.value = response.data.map((post) => ({
      postId: post.postId,
      userId: post.userId,
      userName: post.userName, // 유저 이름 추가
      habitId: post.habitId,
      postLikes: post.postLikes,
      imageURL: post.imageURL,
      content: post.content,
      hashtag: post.hashtag,
      createdAt: post.createdAt,
      isLiked: false,
      comments: 0,
      showComments: false,
    }));
  } catch (error) {
    console.error(error);
    alert('게시글을 불러오는 중 에러가 발생했습니다.');
  }
};

const filteredPosts = computed(() => {
  if (selectedCategory.value === '전체') {
    return posts.value;
  } else {
    return posts.value.filter((post) =>
      post.hashtag.includes(selectedCategory.value)
    );
  }
});

const handleCategoryFilterChange = (category) => {
  selectedCategory.value = category;
};

const toggleLike = (post) => {
  post.isLiked = !post.isLiked;
  post.postLikes += post.isLiked ? 1 : -1;
};

const toggleComments = (post) => {
  post.showComments = !post.showComments;
}


// 게시물별 댓글 수 가져오기 및 existingPosts 업데이트
const fetchCommentCounts = () => {
  axios.get('http://localhost:8080/post-community/posts/comments')
    .then(response => {
      commentCounts.value = response.data; // 댓글 수 업데이트

      // 해시맵의 키로 existingPosts 업데이트
      const postIds = Object.keys(commentCounts.value); // commentCounts의 키 값 (postId)
      existingPosts.value = postIds.map(id => ({ id: Number(id) })); // id만 가진 객체 배열로 변환
    })
    .catch(error => {
      console.error('Error fetching comment counts:', error);
    });
};

// 자식 컴포넌트에서 댓글 변경 이벤트를 받으면 댓글 수 다시 불러오기
const handleCommentChange = (postId) => {
  // 특정 게시물에 대한 댓글 수만 가져오기
  axios.get(`http://localhost:8080/post-community/posts/${postId}/comments/count`)
    .then(response => {
      // 해당 게시물의 댓글 수만 업데이트
      commentCounts.value[postId] = response.data; // 댓글 수 업데이트
    })
    .catch(error => {
      console.error('Error fetching comment count:', error);
    });
};

// 컴포넌트가 마운트될 때 호출
onMounted(() => {
  getAllPost();
  fetchCommentCounts();
});

</script>

<style scoped>
body,
html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  font-family: Avenir, Helvetica, Arial, sans-serif;
}

.container {
  max-width: 1200px; /* Set a maximum width */
  margin: 0 auto; /* Center the container */
  padding: 0 20px; /* Add horizontal padding */
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.category-tags button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 5px 10px;
  border-radius: 20px;
  border: none;
  background-color: #f0f0f0;
}

.category-tags button.active {
  background-color: #71b5fe;
}

.category-tags button:nth-child(1) {
  background-color: #fcf5ab; /* Light yellow for 참여루틴 */
}

.post {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #ddd; /* 테두리 색상 */
  border-radius: 10px; /* 둥근 테두리 모양 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 약간의 그림자 */
  background-color: #fff; /* 배경 색상 */
}

.user-info {
  display: flex;
}

.user-icon {
  width: 30px;
}

.post-image {
  width: 100%;
  border-radius: 10px;
}

.post-details {
  padding-top: 10px;
}

.interaction-buttons {
  display: flex;
  justify-content: flex-start;
  gap: 20px;
}

.like-button,
.comment-button {
  cursor: pointer;
}

.like-button span,
.comment-icon {
  font-size: 1.2em;
}

.like-button .liked {
  color: #ff4136;
}
</style>
