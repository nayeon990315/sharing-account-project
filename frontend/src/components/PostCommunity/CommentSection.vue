<template>
  <div class="comment-section">
    <h3>Comments</h3>
    <div v-for="(comment, index) in comments" :key="comment.id">
      <strong>{{ comment.user }}</strong
      >:
      <span v-if="!comment.isEditing">{{ comment.text }}</span>
      <input v-else v-model="comment.text" />

      <button @click="editComment(index)">
        {{ comment.isEditing ? 'Save' : 'Edit' }}
      </button>
      <button @click="deleteComment(index)">Delete</button>
    </div>
    <textarea v-model="newComment" placeholder="Add a comment..."></textarea>
    <button @click="addComment">Post</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      comments: [],
      newComment: '',
    };
  },
  methods: {
    addComment() {
      if (this.newComment.trim()) {
        this.comments.push({
          id: this.comments.length + 1,
          user: 'current_user', // Replace with actual user
          text: this.newComment,
          isEditing: false,
        });
        this.newComment = '';
      }
    },
    editComment(index) {
      const comment = this.comments[index];
      if (comment.isEditing) {
        comment.isEditing = false;
      } else {
        comment.isEditing = true;
      }
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

.comment-section textarea,
.comment-section input {
  width: 100%;
  margin-bottom: 10px;
}

button {
  margin-left: 5px;
  padding: 5px;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #369f6b;
}
</style>
