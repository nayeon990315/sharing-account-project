<template>
  <div class="container">
    <h1>Explore</h1>
    <div class="top-bar">
      <div class="category-tags">
        <button
          v-for="category in categoryOptions"
          :key="category"
          @click="handleCategoryFilterChange(category)"
          :class="{ active: selectedCategory === category }"
        >
          {{ category }}
        </button>
      </div>
      <!-- 검색창 -->
      <div class="search-routine">
        <input
          type="text"
          v-model="displayedQuery"
          placeholder="해시태그 또는 내용"
          @keyup.enter="performSearch"
        />
        <button @click="performSearch">검색</button>
      </div>
    </div>
    <PostItem
      v-for="post in paginatedPosts"
      :key="post.postId"
      :post="post"
      @toggle-like="toggleLike"
      @toggle-comments="toggleComments"
    />
    <!-- 페이지네이션 컴포넌트 -->
    <paginate
      :page-count="totalPages"
      :click-handler="changePage"
      :prev-text="'<'"
      :next-text="'>'"
      :container-class="'pagination'"
      :page-class="'page-item'"
      :page-link-class="'page-link'"
      :active-class="'active'"
    />
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
  '기타',
];

const posts = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(5);

const searchQuery = ref('');
const displayedQuery = ref('');

// 모든 게시글을 가져오는 함수
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
  } catch (error) {
    console.error(error);
    alert('게시글을 불러오는 중 에러가 발생했습니다.');
  }
};

// 검색 수행 함수
const performSearch = () => {
  currentPage.value = 1;
  searchQuery.value = displayedQuery.value.trim().toLowerCase();
};

// 필터링된 게시글 계산
const filteredPosts = computed(() => {
  // 카테고리로 먼저 필터링
  let filtered =
    selectedCategory.value === '전체'
      ? posts.value
      : posts.value.filter((post) =>
          post.hashtag
            .toLowerCase()
            .includes(selectedCategory.value.toLowerCase())
        );

  // 검색어가 있을 경우 추가 필터링
  if (searchQuery.value) {
    filtered = filtered.filter(
      (post) =>
        post.content.toLowerCase().includes(searchQuery.value) ||
        post.hashtag.toLowerCase().includes(searchQuery.value)
    );
  }

  return filtered;
});

// 페이징 처리된 게시글 계산
const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredPosts.value.slice(start, end);
});

// 총 페이지 수 계산
const updateTotalPages = computed(() => {
  return Math.ceil(filteredPosts.value.length / pageSize.value);
});

// 카테고리 변경 핸들러
const handleCategoryFilterChange = (category) => {
  selectedCategory.value = category;
  currentPage.value = 1;
};

// 좋아요 토글 핸들러
const toggleLike = (post) => {
  post.isLiked = !post.isLiked;
  post.postLikes += post.isLiked ? 1 : -1;
};

// 댓글 토글 핸들러
const toggleComments = (post) => {
  post.showComments = !post.showComments;
};

// 페이지 변경 핸들러
const changePage = (page) => {
  currentPage.value = page;
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
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fffbee;
  border-radius: 0; /* 수정됨 */
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: left;
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
}

.category-tags button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 12px 20px;
  border-radius: 0; /* 수정됨 */
  border: none;
  background-color: #f7d794;
  color: #333;
  /* 
  
  */
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
  padding: 10px 15px;
  border-radius: 0; /* 수정됨 */
  border: none;
  background-color: #f3f3f3;
  margin-right: 10px;
  border-width: 2px;
  border-style: dashed;
  border-color: #ccc;
}

.search-routine button {
  padding: 10px 15px;
  border-radius: 0; /* 수정됨 */
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
  padding: 8px 12px;
  text-align: center;
  margin-right: 3px;
  border-radius: 0; /* 수정됨 */
  border: none;
}
</style>
