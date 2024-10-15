<template>
  <div class="post">
    <div v-if="post" class="post-content">

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
        <!-- <p class="post-content">{{ post.content }}</p>
        <p class="post-hashtag">{{ post.hashtag }}</p>
        <p class="post-date">
          {{ new Date(post.createdAt).toLocaleDateString() }}
        </p>  -->


        <!-- <comment-section
          v-if="post.showComments"
          :post-id="post.postId"
          @comment-change="handleCommentChange"
        ></comment-section> -->

        <!-- <div class="interaction-buttons">
          <div class="like-button" @click="toggleLike">
            <span :class="{ liked: post.isLiked }">{{
              post.isLiked ? '❤️' : '🤍'
            }}</span>
          </div>
          <div class="comment-button" @click="toggleComments(post)">
            <span class="comment-icon">💬</span>
            {{ commentCounts[post.postId] || 0 }}
          </div>
        </div> -->
 
      </div> 
    </div>
    <div v-else>로딩 중...</div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, onMounted, watch } from 'vue';
import axios from 'axios';
// import CommentSection from '@/components/PostCommunity/CommentSection.vue';
import userIcon from '@/assets/icons8-user-64.png';

const props = defineProps({
  selectedYear: {// 혹은 Date 타입도 가능
    required: true,
  },
  selectedMonth: {
    // 혹은 Date 타입도 가능
    required: true,
  },
  selectedDt: {
    // 혹은 Date 타입도 가능
    required: true,
  },
});

const emit = defineEmits(['toggleLike', 'toggleComments']);

const post = ref(null); // 포스트 데이터
const habitData = ref({});
const participants = ref(0);
const checkedHabit = ref(0);
const commentCounts = ref({});

// 선택된 날짜에 맞는 포스트 데이터를 가져오는 함수
const fetchPostDataByDate = async (selectedYear, selectedMonth, selectedDt) => {
  try {
    // 예시 API: selectedDate로 해당 날짜의 포스트 데이터를 가져옴
    const userId = localStorage.getItem("userId");
    const response = await axios.get(`http://localhost:8080/post-community/date?userId=${userId}&selectedYear=${selectedYear}&selectedMonth=${selectedMonth}&selectedDt=${selectedDt}`);
    post.value = response.data;
    console.log("----------------");
    console.log(post.value)
    // 추가 데이터 (참여자 수, 습관 데이터 등) 가져오기
    fetchData();
  } catch (error) {
    console.error('포스트 데이터를 가져오는 중 오류가 발생했습니다:', error);
  }
};

// 추가 데이터를 가져오는 함수
const fetchData = async () => {
  if (post.value) {
    try {
      const shotResponse = await axios.get(
        `http://localhost:8080/post-community/certification-count?userId=${post.value.userId}`
      );
      const habitResponse = await axios.get(
        `http://localhost:8080/habits/find?habitId=${post.value.habitId}`
      );
      const habitCommunityResponse = await axios.get(
        `http://localhost:8080/routine-community/${post.value.habitId}`
      );
      // const checkedHabitResponse = await axios.get(
      //   `http://localhost:8080/habits/checked/count?&habitId=${post.value.habitId}`
      // );

      habitData.value = habitResponse.data;
      participants.value = habitCommunityResponse.data.participants;
      checkedHabit.value = checkedHabitResponse.data;
    } catch (error) {
      console.error('인증 횟수를 가져오는 중 오류가 발생했습니다:', error);
    }
  }
};

// const toggleLike = () => {
//   emit('toggleLike', post.value);
// };

// const toggleComments = () => {
//   emit('toggleComments', post.value);
// };

// 댓글 수를 가져오는 함수
const fetchCommentCounts = () => {
  axios
    .get('http://localhost:8080/post-community/posts/comments')
    .then((response) => {
      commentCounts.value = response.data;
    })
    .catch((error) => {
      console.error('댓글 수를 가져오는 중 오류가 발생했습니다:', error);
    });
};

// 자식 컴포넌트에서 댓글 변경 이벤트 처리
// const handleCommentChange = (postId) => {
//   axios
//     .get(`http://localhost:8080/post-community/posts/${postId}/comments/count`)
//     .then((response) => {
//       commentCounts.value[postId] = response.data;
//     })
//     .catch((error) => {
//       console.error('댓글 수를 가져오는 중 오류가 발생했습니다:', error);
//     });
// };

// selectedDate가 변경될 때마다 데이터를 가져오는 watch
watch(() => props.selectedDate, (newDate) => {
  fetchPostDataByDate(newDate);
});

// 컴포넌트 마운트 시 초기 데이터 가져오기
onMounted(() => {
  fetchPostDataByDate(props.selectedYear, props.selectedMonth, props.selectedDt); // 초기 날짜 데이터 가져오기
  // fetchCommentCounts(); // 댓글 수 가져오기
});
</script>



<style scoped>
/* 메인 포스트 컨테이너 */
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

/* 유저 정보 영역 */
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

/* 텍스트 및 기타 정보 */
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

/* 포스트 이미지 */
.post-image {
  width: 100%;
  height: 100%;
  height: auto;
  border-radius: 8px;
  margin-top: 15px;
  margin-bottom: 10px;
}

/* 포스트 상세 내용 */
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

/* 상호작용 버튼들 */
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

.like-button span {
  margin-right: 5px;
  font-size: 20px;
}

.liked {
  color: #ff5c5c;
}

.comment-icon {
  margin-right: 5px;
  font-size: 18px;
}
</style>
