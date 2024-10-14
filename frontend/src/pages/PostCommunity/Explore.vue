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
    <div>{{ shot }}</div>
    <PostItem
      v-for="post in filteredPosts"
      :key="post.postId"
      :post="post"
      @toggle-like="toggleLike"
      @toggle-comments="toggleComments"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import PostItem from '@/components/PostCommunity/PostItem.vue';

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
      userName: post.userName,
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

    console.log(response.data);
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
};

onMounted(() => {
  getAllPost();
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
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
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
  background-color: #fcf5ab;
}
</style>
