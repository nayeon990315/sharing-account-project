<template>
  <div class="comment-section">
    <h3>Comments</h3>
    <div
      v-for="(comment, index) in comments"
      :key="index"
      class="comment-item"
    >
      <div class="comment-header">
        <strong class="writer">{{ comment.commentWriter }}</strong>
        <span class="time"> {{ timeAgo(comment.createdAt) }}</span>
      </div>

      <div class="content" v-if="!comment.isEditing">{{ comment.commentContent }}</div>
      <input v-else v-model="comment.commentContent" />

      <button class="editBtn" v-if="!comment.isEditing" @click="editComment(index)">
        Edit
      </button>
      <button class="deleteBtn" v-if="!comment.isEditing" @click="deleteComment(index)">
        Delete
      </button>

      <!-- 수정 중일 때의 save / cancel 버튼 -->
      <button class="saveBtn" v-if="comment.isEditing" @click="saveEdit(index)">
        Save
      </button>
      <button class="cancelBtn" v-if="comment.isEditing" @click="cancelEdit(index)">
        Cancel
      </button>
    </div>

    <div class="comment-input">
      <textarea v-model="newComment" placeholder="Add a comment..."></textarea>
      <button class="comment-submit-button" @click="submitComment">
        <span class="arrow-icon">↑</span>
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    postId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      comments: [],
      newComment: '',
      currentUser: localStorage.getItem('nickname'), // 현재 로그인한 사용자 닉네임 가져오기
      // commentCounts: {}, // 게시물별 댓글 수 저장

    };
  },
  mounted() {
    this.fetchComments();
    // this.fetchCommentCounts(); // 페이지 로딩 시 댓글 수 불러오기
  },
  methods: {
    // 댓글 목록을 서버에서 불러오기
    fetchComments() {
      // const postId = 1;
      axios.get(`http://localhost:8080/post-community/posts/${this.postId}/comments`)
        .then((response) => {
          this.comments = response.data; // 서버로부터 댓글을 불러와 리스트에 추가
          console.log('comments:', this.comments); // comments 확인하기 ok
        })
        .catch((error) => {
          console.error('Error fetching comments:', error);
        });
    },

    // addComment() {
    //   if (this.newComment.trim()) {
    //     this.comments.push({
    //       id: this.comments.length + 1,
    //       user: 'current_user', // Replace with actual user
    //       text: this.newComment,
    //       isEditing: false,
    //     });
    //     this.newComment = '';
    //   }
    // },

    // 서버에 댓글 전송 및 추가
    submitComment() {
      // const postId = 1;
      // const commentWriter = localStorage.getItem('nickname'); // 사용자 닉네임
      const commentWriter = this.currentUser;

      if (this.newComment.trim()) {

        console.log('commentWriter:', commentWriter); // commentWriter 확인하기 ok 

        axios.post(`http://localhost:8080/post-community/posts/${this.postId}/comments`, {
          commentContent: this.newComment,
          commentWriter: commentWriter
        })
        .then((response) => {
          // 서버에 댓글이 성공적으로 추가되면 로컬에 추가
          this.comments.push({
            commentId: response.data.id, // 서버로부터 받은 댓글 ID
            commentWriter: commentWriter,
            commentContent: this.newComment,
            createdAt: new Date().toISOString(), // 현재 시간을 createdAt으로 임시 설정
            isEditing: false
          });
          this.fetchComments();
          
          // 부모에게 댓글 변경 이벤트 전송
          this.$emit('comment-change', this.postId); // 댓글이 추가되었음을 부모에게 알림

          this.newComment = ""; // 입력 필드 초기화
        })
        .catch((error) => {
          console.error("Error adding comment:", error);
        });
      }
    },

    // 댓글 수정 모드로 전환 및 수정 저장
    editComment(index) {
      const comment = this.comments[index];

      if (comment.commentWriter === this.currentUser) { // 본인 댓글만 수정 가능
        // 수정 모드 진입 시 원래 content를 저장해둠
        comment.originalContent = comment.commentContent;
        comment.isEditing = true; // 수정 모드로 전환
      } else {
        alert('본인 댓글만 수정할 수 있습니다.');
      }
    },

    // 수정 내용 저장
    saveEdit(index) {
      const comment = this.comments[index];

      if (comment.commentWriter === this.currentUser) {
        axios
          .put(`http://localhost:8080/post-community/posts/${this.postId}/comments/${comment.commentId}`, {
            commentContent: comment.commentContent, // 수정된 댓글 내용 전송
          })
          .then(() => {
            console.log('Comment updated successfully');
            comment.isEditing = false; // 수정 완료 후 편집 모드 해제
            delete comment.originalContent; // 임시 저장된 원본 삭제
          })
          .catch((error) => {
            console.error('Error updating comment:', error);
          });
      }
    },

    // 수정 취소
    cancelEdit(index) {
      const comment = this.comments[index];
      // 수정 취소 시 원래 내용 복원
      comment.commentContent = comment.originalContent;
      comment.isEditing = false; // 편집 모드 해제
      delete comment.originalContent; // 임시 저장된 원본 삭제
    },


    deleteComment(index) {
      const comment = this.comments[index];

      if (comment.commentWriter === this.currentUser) {
        axios
          .delete(`http://localhost:8080/post-community/posts/${this.postId}/comments/${comment.commentId}`)
          .then(() => {
            this.comments.splice(index, 1); // 서버에서 성공적으로 삭제된 후 로컬에서도 삭제

            this.fetchComments();
            // 부모에게 댓글 변경 이벤트 전송
            this.$emit('comment-change', this.postId); // 댓글이 삭제되었음을 부모에게 알림
          })
          .catch((error) => {
            console.error('Error deleting comment:', error);
          });
      } else {
        alert('본인 댓글만 삭제할 수 있습니다.');
      }
    },


    // 기타
    // 시간 차이 계산
    timeAgo(createdAt) {
      const createdDate = new Date(createdAt);
      const currentDate = new Date();
      const diffTime = Math.abs(currentDate - createdDate);
      const diffHours = Math.floor(diffTime / (1000 * 60 * 60)); // 시간 차이 계산, 분은 버림

      return diffHours === 0 ? "방금" : `${diffHours}시간`;
    },
  },

  // 게시물별 댓글 수 가져오기
  // fetchCommentCounts() {
  //   axios.get('http://localhost:8080/post-community/comments')
  //     .then((response) => {
  //       this.commentCounts = response.data; // 서버에서 받아온 댓글 수 저장
  //     })
  //     .catch((error) => {
  //       console.error('Error fetching comment counts:', error);
  //     });
  // },

};
</script>

<style>
.comment-section {
  margin-top: 20px;
}

.comment-item {
  margin-bottom: 20px;
}

.comment-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.comment-item button {
  margin-left: 5px;
  padding: 5px;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
}

.comment-item button:hover {
  background-color: #369f6b;
}

.comment-input {
  display: flex;
  align-items: center;
}

.comment-input textarea {
  flex-grow: 1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.comment-submit-button {
  width: 40px;
  height: 40px;
  margin-left: 10px;
  border-radius: 50%;
  border: none;
  display: flex;
  justify-content: center;
  align-items: center;
}

.arrow-icon {
  color: white; /* White arrow */
  font-size: 20px;
}

/* 댓글 내 위치 조정 */
.comment-header {
  display: block;
}

.content {
  display: block; /* 줄바꿈을 위한 block 처리 */
  margin-top: 10px; /* 작성자 및 시간과의 간격 */
  font-size: 14px;
  line-height: 1.5;
  color: #444;
}

.time {
  margin-left: 4px;
  font-size: 10px;
}

.writer {
  font-weight: bold;
  color: #333;
}

.time {
  color: #888;
  font-size: 12px;
}

.content {
  margin: 10px 0;
  font-size: 14px;
  line-height: 1.5;
  color: #444;
}

.input {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

button.editBtn,
button.deleteBtn {
  padding: 5px 10px;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

button.editBtn:hover {
  background-color: #369f6b;
}

button.deleteBtn {
  background-color: #ff4d4d;
}

button.deleteBtn:hover {
  background-color: #cc3b3b;
}
</style>
