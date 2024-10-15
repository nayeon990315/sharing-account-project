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
  "식비",
    "카페/간식",
    "온라인쇼핑",
    "패션/쇼핑",
    "문화/여가",
    "술/유흥",
    "교육",
    "의료/건강",
    "생활",
    "주거/공과금",
    "금융",
    "뷰티",
    "자동차",
    "교통",
    "반려동물",
    "여행",
    "경조사/회비"
];

const posts = ref([]);
const currentPage = ref(1);
const pageSize = ref(5);

const searchQuery = ref('');
const displayedQuery = ref('');

// 모든 게시글을 가져오는 함수
const getAllPost = async () => {
  try {
    const response = await axios.get(
      'http://localhost:8080/post-community/all'
    );

    // 각 게시물에 대해 habitTitle을 가져와서 추가
    const postsWithHabitTitle = await Promise.all(
      response.data.map(async (post) => {
        try {
          const habitResponse = await axios.get(
            `http://localhost:8080/habits/find?habitId=${post.habitId}`
          );
          return {
            ...post,
            habitTitle: habitResponse.data.habitTitle, // 습관 제목 추가
            isLiked: false,
            comments: 0,
            showComments: false,
          };
        } catch (error) {
          console.error(
            `habitId ${post.habitId}에 대한 습관 제목을 가져오는 중 오류 발생:`,
            error
          );
          return {
            ...post,
            habitTitle: '', // 습관 제목을 가져오지 못한 경우 빈 문자열
            isLiked: false,
            comments: 0,
            showComments: false,
          };
        }
      })
    );

    posts.value = postsWithHabitTitle;
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
  const query = searchQuery.value;

  // 카테고리 필터링
  let filtered =
    selectedCategory.value === '전체'
      ? posts.value
      : posts.value.filter((post) =>
          post.hashtag
            .toLowerCase()
            .includes(selectedCategory.value.toLowerCase())
        );

  // 검색어가 있을 경우 추가 필터링
  if (query) {
    filtered = filtered.filter(
      (post) =>
        (post.content && post.content.toLowerCase().includes(query)) ||
        (post.hashtag && post.hashtag.toLowerCase().includes(query)) ||
        (post.habitTitle && post.habitTitle.toLowerCase().includes(query))
    );
  }

  return filtered;
});

// 총 페이지 수 계산 (필터링된 게시글 기준)
const totalPages = computed(() => {
  return Math.ceil(filteredPosts.value.length / pageSize.value);
});

// 페이징 처리된 게시글 계산
const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredPosts.value.slice(start, end);
});

// 카테고리 변경 핸들러
const handleCategoryFilterChange = (category) => {
  if (category == "참여루틴") {
    selectedCategory.value = "전체";
  }
  else {
    selectedCategory.value = category;
  } 
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
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fffbee;
  border-radius: 0px;
}

h1 {
  text-align: left; /* 텍스트를 왼쪽으로 배치 */
  color: #000000;
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
  justify-content: center; /* 버튼들을 중앙으로 정렬 */
}

.category-tags button,
.search-routine button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 8px 16px; /* 높이를 일관되게 설정 */
  border-radius: 0px;
  border: none;
  background-color: #f7d794;
  color: #333;
  font-size: 1em; /* 폰트 크기를 동일하게 설정 */
  line-height: normal; /* 라인 높이 설정 */
  cursor: pointer;
  transition: background-color 0.3s; /* 부드러운 전환 효과 */
}

.category-tags button.active {
  background-color: #f6b93b;
  color: #fff;
}

.category-tags button:hover,
.search-routine button:hover {
  background-color: #fa983a;
  color: #fff;
  box-shadow: none;
}

.category-tags button:nth-child(1) {
  background-color: #000000;
  color: white;
}

.search-routine input[type='text'] {
  padding: 6px 12px; /* 공백 추가 */
  border-radius: 0px;
  border: none;
  background-color: #f3f3f3;
  margin-right: 10px;
  border-width: px;
  border-style: dashed;
  border-color: #ccc;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.page-item {
  min-width: 32px;
  padding: 8px 12px; /* 공백 추가 */
  text-align: center;
  margin-right: 3px;
  border-radius: 0px;
  border: none;
}

.no-results {
  text-align: center;
  color: #888;
  font-size: 1.2em;
  margin-top: 20px;
}
</style>
