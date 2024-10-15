<template>
  <div class="post">
    <div class="user-info">
      <img :src="userIcon" alt="User Icon" class="user-icon" />
      <span class="username">{{ post.userName }}</span>
    </div>
    <p class="habit-title">{{ habitData.habitTitle }}</p>
    <p class="participants">
      현재 이 습관에 {{ participants }} 명이 참여 중이고,
    </p>
    <p class="habit-likes">
      지금까지 인증샷은 총 {{ checkedHabit }}개가 올라왔어요!
    </p>
    <img :src="post.imageURL" alt="Post Image" class="post-image" />
    <div class="post-details">
      <p class="post-content">{{ post.content }}</p>
      <p class="post-hashtag">{{ post.hashtag }}</p>
      <p class="post-date">
        {{ new Date(post.createdAt).toLocaleDateString() }}
      </p>

      <comment-section
        v-if="post.showComments"
        :post-id="post.postId"
        @comment-change="handleCommentChange"
      ></comment-section>
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

const shotCount = ref(0);
const habitData = ref({});
const participants = ref(0);
const checkedHabit = ref(0);

const existingPosts = ref([]);
const commentCounts = ref({});

const likeIcon = computed(() => {
  return props.post.isLiked ? likeFullIcon : likeEmptyIcon;
});

const commentIcon = ref(commentIconSrc);

const toggleLike = () => {
  emit('toggleLike', props.post);
};

const toggleComments = () => {
  emit('toggleComments', props.post);
};

const fetchData = async () => {
  try {
    const shotResponse = await axios.get(
      `http://localhost:8080/post-community/certification-count?userId=${props.post.userId}`
    );
    console.log('shot response' + shotResponse.data);
    shotCount.value = shotResponse.data;

    const habitResponse = await axios.get(
      `http://localhost:8080/habits/find?habitId=${props.post.habitId}`
    );

    console.log('Habit Data : ', habitResponse.data);
    habitData.value = habitResponse.data;

    const habitCommunityResponse = await axios.get(
      `http://localhost:8080/routine-community/${props.post.habitId}`
    );

    console.log('HabitCommunity 데이터 : ', habitCommunityResponse.data);
    participants.value = habitCommunityResponse.data.participants;

    const checkedHabitResponse = await axios.get(
      `http://localhost:8080/habits/checked/count?&habitId=${props.post.habitId}`
    );

    console.log('checkedHabitResponse : ', checkedHabitResponse.data);
    checkedHabit.value = checkedHabitResponse.data;
  } catch (error) {
    console.error('인증 횟수를 가져오는 중 오류가 발생했습니다:', error);
  }
};

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

onMounted(() => {
  fetchData();
  fetchCommentCounts();
});
</script>

<style scoped>
.post {
  width: 100%;
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #e6e6e6;
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

.habit-title,
.shot-count,
.participants,
.habit-likes {
  font-size: 14px;
  margin-bottom: 10px;
  color: #555;
}

.habit-title {
  font-weight: bold;
  font-size: 16px;
}

.shot-count {
  color: #ff5c5c;
}

.participants,
.habit-likes {
  color: #888;
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

.post-content {
  font-size: 14px;
  margin-bottom: 8px;
}

.post-hashtag {
  color: #1e90ff;
  margin-bottom: 8px;
}

.post-date {
  font-size: 12px;
  color: #aaa;
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
