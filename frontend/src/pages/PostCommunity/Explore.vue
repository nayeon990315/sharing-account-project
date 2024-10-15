<template>
  <div class="container">
    <h1>Explore</h1>
    <div class="top-bar">
      <div class="category-tags">
        <button v-for="category in categoryOptions" :key="category" @click="handleCategoryFilterChange(category)"
          :class="{ active: selectedCategory === category }">
          {{ category }}
        </button>
      </div>
      <!-- 검색창 -->
      <div class="search-routine">
        <input type="text" v-model="displayedQuery" placeholder="해시태그 또는 내용" />
        <button @click="performSearch">검색</button>
      </div>
    </div>
    <PostItem v-for="post in paginatedPosts" :key="post.postId" :post="post" @toggle-like="toggleLike"
      @toggle-comments="toggleComments" />
    <!-- 페이지네이션 컴포넌트 -->
    <paginate :page-count="totalPages" :click-handler="changePage" :prev-text="'<'" :next-text="'>'"
      :container-class="'pagination'" :page-class="'page-item'" :page-link-class="'page-link'"
      :active-class="'active'" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import PostItem from '@/components/PostCommunity/PostItem.vue';
import Paginate from 'vuejs-paginate-next';

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
  "금융",
  "뷰티",
  "자동차",
  "교통",
  "반려동물",
  "여행",
  "경조사/회비",
];

const posts = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(5);

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

    totalPages.value = Math.ceil(posts.value.length / pageSize.value);

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

const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredPosts.value.slice(start, end);
});

const handleCategoryFilterChange = (category) => {
  if (category == '참여루틴') {
    selectedCategory.value = '전체';
  }
  else {
    selectedCategory.value = category;
  }
  console.log(selectedCategory.value);
};

const toggleLike = (post) => {
  post.isLiked = !post.isLiked;
  post.postLikes += post.isLiked ? 1 : -1;
};

const toggleComments = (post) => {
  post.showComments = !post.showComments;
};

const changePage = (page) => {
  currentPage.value = page;
};

const searchQuery = ref('');
const displayedQuery = ref('');

const performSearch = () => {
  console.log('Search button clicked. Keyword:', searchQuery.value);
  currentPage.value = 1;
  searchQuery.value = displayedQuery.value;
};

// 초기 데이터 로드
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
  padding: 20px;
  background-color: #fffbee;
  border-radius: 10px;
  box-shadow: 0px4px10pxrgba (0, 0, 0, 0.1);
}

h1 {
  text-align: left;
  /* 텍스트를 왼쪽으로 배치 */
  color: #f39c12;
  font-size: 2.5em;
  margin-bottom: 20px;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  /* 버튼들을 중앙으로 정렬 */
}

.category-tags button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 12px20px;
  border-radius: 20px;
  border: none;
  background-color: #f7d794;
  color: #333;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s, color 0.3s, box-shadow 0.3s;
}

.category-tags button.active {
  background-color: #f6b93b;
  color: #fff;
}

.category-tags button:hover {
  background-color: #fa983a;
  color: #fff;
  box-shadow: none;
}

.category-tags button:nth-child(1) {
  background-color: #000000;
  color: white;
}

.search-routine input[type='text'] {
  padding: 10px15px;
  border-radius: 20px;
  border: none;
  background-color: #f3f3f3;
  margin-right: 10px;
  border-width: 2px;
  border-style: dashed;
  border-color: #ccc;
}

.search-routine button {
  padding: 10px15px;
  border-radius: 20px;
  border: none;
  background-color: #f39c12;
  color: white;
  cursor: pointer;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.page-item {
  min-width: 32px;
  padding: 8px12px;
  text-align: center;
  margin-right: 3px;
  border-radius: 6px;
  border: none;
}
</style>
