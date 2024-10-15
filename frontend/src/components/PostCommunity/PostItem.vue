<template>
  <div class="post">
    <div class="user-info">
      <img :src="userIcon" alt="User Icon" class="user-icon" />
      <span class="username">{{ post.userName }}</span>
    </div>
    <p class="habit-title">{{ habitData.habitTitle }}</p>

    <!-- 통합된 participants와 checkedHabit 정보 -->
    <div class="habit-info">
      <p class="participants">
        현재 이 습관에 {{ participants }} 명이 참여 중이고,<br />
        지금까지 인증샷은 총 {{ checkedHabit }}개가 올라왔어요!
      </p>
    </div>

    <img :src="post.imageURL" alt="Post Image" class="post-image" />
    <div class="post-details">
      <p class="post-content">{{ post.content }}</p>
      <p class="post-hashtag">{{ post.hashtag }}</p>
      <p class="post-date">
        {{ new Date(post.createdAt).toLocaleDateString() }}
      </p>

      <!-- 댓글 섹션 -->
      <comment-section
        v-if="post.showComments"
        :post-id="post.postId"
        @comment-change="handleCommentChange"
      ></comment-section>

      <!-- 좋아요 및 댓글 버튼 -->
      <div class="interaction-buttons">
        <div class="like-button" @click="toggleLike">
          <img :src="likeIcon" alt="Like" class="icon" />
          {{ post.postLikes }}
        </div>
        <div class="comment-button" @click="toggleComments(post)">
          <img :src="commentIcon" alt="Comment" class="icon" />
          {{ commentCounts[post.postId] || 0 }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, onMounted, computed } from 'vue';
import axios from 'axios';
import CommentSection from '@/components/PostCommunity/CommentSection.vue';
import userIcon from '@/assets/icons8-user-64.png';
import likeEmptyIcon from '@/assets/icons/like.png';
import likeFullIcon from '@/assets/icons/fulllike2.png';
import commentIconSrc from '@/assets/icons/comment.png';

const props = defineProps({
  post: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['toggleLike', 'toggleComments']);

// 좋아요 및 댓글 관련 데이터
const shotCount = ref(0);
const habitData = ref({});
const participants = ref(0);
const checkedHabit = ref(0);

const existingPosts = ref([]);
const commentCounts = ref({});

// 좋아요 아이콘 설정
const likeIcon = computed(() => {
  return props.post.isLiked ? likeFullIcon : likeEmptyIcon;
});

const commentIcon = ref(commentIconSrc);

// 좋아요 버튼 클릭 시 이벤트 발생
const toggleLike = () => {
  emit('toggleLike', props.post);
};

// 댓글 버튼 클릭 시 이벤트 발생
const toggleComments = () => {
  emit('toggleComments', props.post);
};

// 데이터 가져오는 함수
const fetchData = async () => {
  try {
    const shotResponse = await axios.get(
      `http://localhost:8080/post-community/certification-count?userId=${props.post.userId}`
    );
    shotCount.value = shotResponse.data;

    const habitResponse = await axios.get(
      `http://localhost:8080/habits/find?habitId=${props.post.habitId}`
    );
    habitData.value = habitResponse.data;

    const habitCommunityResponse = await axios.get(
      `http://localhost:8080/routine-community/${props.post.habitId}`
    );
    participants.value = habitCommunityResponse.data.participants;

    const checkedHabitResponse = await axios.get(
      `http://localhost:8080/habits/checked/count?&habitId=${props.post.habitId}`
    );
    checkedHabit.value = checkedHabitResponse.data;
  } catch (error) {
    console.error('인증 횟수를 가져오는 중 오류가 발생했습니다:', error);
  }
};

// 댓글 개수 가져오는 함수
const fetchCommentCounts = () => {
  axios
    .get('http://localhost:8080/post-community/posts/comments')
    .then((response) => {
      commentCounts.value = response.data;
      const postIds = Object.keys(commentCounts.value);
      existingPosts.value = postIds.map((id) => ({ id: Number(id) }));
    })
    .catch((error) => {
      console.error('Error fetching comment counts:', error);
    });
};

// 댓글 업데이트 핸들러
const handleCommentChange = (postId) => {
  axios
    .get(`http://localhost:8080/post-community/posts/${postId}/comments/count`)
    .then((response) => {
      commentCounts.value[postId] = response.data;
    })
    .catch((error) => {
      console.error('Error fetching comment count:', error);
    });
};

// 컴포넌트 마운트 시 데이터 로드
onMounted(() => {
  fetchData();
  fetchCommentCounts();
});
</script>

<style scoped>
.post {
  width: 100%;
  max-width: 700px;
  margin: 10px auto;
  padding: 20px;
  background-color: #fff;
  border: 0.5px solid #e6e6e6;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.user-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.username {
  font-weight: bold;
  color: #333;
  font-size: 14px;
}

.habit-title {
  background-color: #fdf0ca;
  border-radius: 8px;
  padding: 10px 20px;
  display: inline-block;
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 15px;
  position: relative;
}

.habit-title::before {
  content: '';
  position: absolute;
  top: -10px;
  left: 20px;
  border-width: 0 10px 10px;
  border-style: solid;
  border-color: transparent transparent #fdf0ca transparent;
}

.habit-info {
  background-color: #f5f5f5;
  border-radius: 8px;
  padding: 10px;
  margin-top: -5px;
  text-align: left;
  width: max-content;
  max-width: 100%;
}

.participants {
  margin: 0;
  font-size: 14px;
}

.post-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  margin-top: 15px;
  margin-bottom: 10px;
}

.post-details {
  margin-top: 10px;
}

.post-content,
.post-hashtag,
.post-date {
  margin-bottom: 8px;
}

.interaction-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.like-button,
.comment-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #888;
  font-size: 14px;
}

.icon {
  width: 20px;
  height: 20px;
  margin-right: 5px;
}
</style>
