<template>
  <div class="comment-section">
    <h3>Comments</h3>
    <div
      v-for="(comment, index) in comments"
      :key="comment.id"
      class="comment-item"
    >
      <strong>{{ comment.user }}</strong
      >:
      <span v-if="!comment.isEditing">{{ comment.text }}</span>
      <input v-else v-model="comment.text" />

      <button v-if="!comment.isEditing" @click="editComment(index)">
        Edit
      </button>
      <button v-if="!comment.isEditing" @click="deleteComment(index)">
        Delete
      </button>
    </div>

    <div class="comment-input">
      <textarea v-model="newComment" placeholder="Add a comment..."></textarea>
      <button class="comment-submit-button" @click="addComment">
        <span class="arrow-icon">↑</span>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      comments: [],
      newComment: '',
      currentUser: 'user123', // Replace with actual logic to get the current user
    };
  },
  methods: {
    addComment() {
      if (this.newComment.trim()) {
        this.comments.push({
          id: this.comments.length + 1,
          user: this.currentUser, // Use the current user's ID
          text: this.newComment,
          isEditing: false,
        });
        this.newComment = '';
      }
    },
    editComment(index) {
      const comment = this.comments[index];
      comment.isEditing = !comment.isEditing;
    },
    deleteComment(index) {
      this.comments.splice(index, 1);
    },
  },
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
  background-color: #007bff; /* Blue background */
  border: none;
  display: flex;
  justify-content: center;
  align-items: center;
}

.arrow-icon {
  color: white; /* White arrow */
  font-size: 20px;
}
</style>
