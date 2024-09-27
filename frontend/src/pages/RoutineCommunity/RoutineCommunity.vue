<template>
  <nav class="filter">
    <div class="categoryFilter">
      <form action="#">
        <label for="category_filter">카테고리</label>
        <select
          name="category"
          id="category"
          @change="handleCategoryFilterChange"
        >
          <option value="all">전체</option>
          <option value="food">식비</option>
          <option value="dessert">카페/간식</option>
          <option value="beauty">뷰티</option>
        </select>
      </form>
    </div>

    <div class="otherFilter">
      <form action="#">
        <input
          type="radio"
          name="otherFilter"
          value="recent"
          @change="handleFilterChange"
        />
        최신 순
        <input
          type="radio"
          name="otherFilter"
          value="manyLikes"
          @change="handleFilterChange"
        />
        좋아요 순
        <input
          type="radio"
          name="otherFilter"
          value="manyParticipants"
          @change="handleFilterChange"
        />
        참여자 순
        <input
          type="radio"
          name="otherFilter"
          value="manyCompletions"
          @change="handleFilterChange"
        />
        달성자 순
        <input
          type="radio"
          name="otherFilter"
          value="myLikes"
          @change="handleFilterChange"
        />
        나의 좋아요
      </form>
    </div>

    <!-- <input type="submit" value="Submit" /> -->
  </nav>

  <div class="main">
    <!-- 루틴 커뮤니티 -->
    <div class="intro">
      <h3>Routine Share Community</h3>
      <p>
        나만의 '벌루틴'을 공유하고, <br />다른 사람들의 '벌루틴'을<br />내
        것으로 만들어보세요!
      </p>
    </div>

    <!-- 카드들 -->
    <!-- <div class="cards row row-cols-1 row-cols-md-3 g-4" v-if="(isMyLikesFilterActive ? checkMyLike() : routineCommunityArray).length > 0"> -->
    <!-- 좋아요 누른 게 없을 때 문구 띄우기 -->
    <div class="cards row row-cols-1 row-cols-md-3 g-4">
      <!-- 카드 하나 -->
      <!-- v-for 문 !!! -->
      <!-- <div class="col" v-for="routine in routineCommunityArray" :key="routine.community_id"> -->
      <div
        class="col"
        v-for="routine in filteredRoutines"
        :key="routine.community_id"
      >
        <div class="card h-100">
          <div class="card-body">
            <div class="subtitle">
              <span class="type card-subtitle">
                {{ routine.category_title }}
              </span>
              <span class="date card-subtitle">
                {{ routine.update_date }}
              </span>
              <!-- mb-2 text-body-secondary -->
            </div>

            <h5 class="card-title">{{routine.habit_title}}</h5>
          <div class="card-text">
            <div class="writer">
                <img class="avatar" src="@/assets/images/sample.jpg">
                <span class="writerName">{{routine.user_id}}</span>
            </div>
            <div class="likeContainer">
              <button class="likeButton" @click="toggleLike(routine.community_id)">

                  <img
                    class="likeImg"
                    :src="isLiked(routine.community_id) ? fullLike : emptyLike"
                    alt="like"
                  />
                </button>
                <!-- <span class="likeComment" v-if="isLiked">{{routine.habit_likes + (isLiked(routine.community_id) ? 1 : 0)}}</span> -->
                <span class="likeComment">{{ routine.habit_likes }}</span>
              </div>
            </div>

            <div class="challengeContainer">
              <p class="challengeComment">
                {{ routine.participants }}명의 루티너 중<br />
                오늘 {{ routine.complete }}명이 목표를 달성했어요. <br />
                <!-- {{routine.complete + 1}}번째 주인공이 되어보세요! -->
              </p>
              <a href="#" class="challengeButton btn btn-primary">
                <img
                  class="challengeIcon"
                  src="@/assets/icons/together_invertColor.png"
                />
                <span class="challengeButtonText"
                  >내 루틴에 담고 함께 도전하기</span
                >
              </a>
            </div>
          </div>
        </div>
      </div>
      <!-- 카드 하나 끝-->
      <!-- 카드 끝 -->
    </div>
    <!-- 좋아요한 루틴이 없을 때 표시할 메시지 -->
    <!-- <p v-else>좋아요한 루틴이 없습니다.</p>  -->
  </div>
</template>

<script setup>
import Header from '@/components/global/Header.vue';
import Footer from '@/components/Footer.vue';
import { ref, reactive } from 'vue';

import fullLike from '@/assets/icons/fullLike.png';
import emptyLike from '@/assets/icons/emptyLike.png';

// routineCommunityArray 데이터
const routineCommunityArray = ref([
  {
    community_id: 1,
    habit_title: '커피 텀블러에 담아서 출근하기',
    category_title: '카페/간식',
    user_id: 'Money.java',
    habit_likes: 9, // 좋아요 누른 사용자 수
    participants: 1784,
    complete: 512,
    update_date: '2024-09-24T14:48:00Z',
  },
  {
    community_id: 2,
    habit_title: '하루 외식값 25000원 넘지 않기',
    category_title: '식비',
    user_id: 'user456',
    habit_likes: 5,
    participants: 980,
    complete: 350,
    update_date: '2024-09-23T09:12:00Z',
  },
  {
    community_id: 3,
    habit_title: '수제 모델링팩 만들기',
    category_title: '뷰티',
    user_id: 'beautylover',
    habit_likes: 6,
    participants: 620,
    complete: 420,
    update_date: '2024-09-22T16:20:00Z',
  },
  {
    community_id: 4,
    habit_title: '하루 커피 1잔 이하로 줄이기',
    category_title: '카페/간식',
    user_id: 'coffeelover',
    habit_likes: 9,
    participants: 1400,
    complete: 920,
    update_date: '2024-09-21T11:30:00Z',
  },
  {
    community_id: 5,
    habit_title: '아침 식사는 집에서 해결하기',
    category_title: '식비',
    user_id: 'user321',
    habit_likes: 7,
    participants: 740,
    complete: 520,
    update_date: '2024-09-20T10:15:00Z',
  },
  {
    community_id: 6,
    habit_title: '마스크팩 자주 하지 않고 필요한 날만 하기',
    category_title: '뷰티',
    user_id: 'skincareguru',
    habit_likes: 9,
    participants: 550,
    complete: 320,
    update_date: '2024-09-19T15:05:00Z',
  },
  {
    community_id: 7,
    habit_title: '카페 대신 집에서 차 마시기',
    category_title: '카페/간식',
    user_id: 'user654',
    habit_likes: 11,
    participants: 600,
    complete: 400,
    update_date: '2024-09-18T12:48:00Z',
  },
  {
    community_id: 8,
    habit_title: '하루에 필요한 만큼만 장보기',
    category_title: '식비',
    user_id: 'user543',
    habit_likes: 12,
    participants: 820,
    complete: 580,
    update_date: '2024-09-17T13:20:00Z',
  },
  {
    community_id: 9,
    habit_title: '화장품 꼭 필요한 것만 사기',
    category_title: '뷰티',
    user_id: 'makeupminimalist',
    habit_likes: 11,
    participants: 500,
    complete: 300,
    update_date: '2024-09-16T14:00:00Z',
  },
  {
    community_id: 10,
    habit_title: '디저트 대신 과일로 간식 해결하기',
    category_title: '카페/간식',
    user_id: 'user432',
    habit_likes: 15,
    participants: 640,
    complete: 420,
    update_date: '2024-09-15T11:30:00Z',
  },
]);

// likesArray 데이터
const likesArray = ref([
  { community_id: 1, habit_likes_id: 'Money.java' },
  { community_id: 1, habit_likes_id: 'user123' },
  { community_id: 1, habit_likes_id: 'user432' },
  { community_id: 1, habit_likes_id: 'user567' },
  { community_id: 1, habit_likes_id: 'user789' },
  { community_id: 1, habit_likes_id: 'user101' },
  { community_id: 1, habit_likes_id: 'user234' },
  { community_id: 1, habit_likes_id: 'user345' },
  { community_id: 1, habit_likes_id: 'user456' },

  { community_id: 2, habit_likes_id: 'Money.java' },
  { community_id: 2, habit_likes_id: 'user789' },
  { community_id: 2, habit_likes_id: 'user111' },
  { community_id: 2, habit_likes_id: 'user222' },
  { community_id: 2, habit_likes_id: 'user333' },

  { community_id: 3, habit_likes_id: 'beautylover' },
  { community_id: 3, habit_likes_id: 'user987' },
  { community_id: 3, habit_likes_id: 'user543' },
  { community_id: 3, habit_likes_id: 'user654' },
  { community_id: 3, habit_likes_id: 'user765' },
  { community_id: 3, habit_likes_id: 'user876' },

  { community_id: 4, habit_likes_id: 'coffeelover' },
  { community_id: 4, habit_likes_id: 'user567' },
  { community_id: 4, habit_likes_id: 'user876' },
  { community_id: 4, habit_likes_id: 'user999' },
  { community_id: 4, habit_likes_id: 'user111' },
  { community_id: 4, habit_likes_id: 'user222' },
  { community_id: 4, habit_likes_id: 'user333' },
  { community_id: 4, habit_likes_id: 'user444' },
  { community_id: 4, habit_likes_id: 'user555' },

  { community_id: 5, habit_likes_id: 'user321' },
  { community_id: 5, habit_likes_id: 'user654' },
  { community_id: 5, habit_likes_id: 'user123' },
  { community_id: 5, habit_likes_id: 'user345' },
  { community_id: 5, habit_likes_id: 'user567' },
  { community_id: 5, habit_likes_id: 'user789' },
  { community_id: 5, habit_likes_id: 'user101' },

  { community_id: 6, habit_likes_id: 'skincareguru' },
  { community_id: 6, habit_likes_id: 'user876' },
  { community_id: 6, habit_likes_id: 'user543' },
  { community_id: 6, habit_likes_id: 'user654' },
  { community_id: 6, habit_likes_id: 'user321' },
  { community_id: 6, habit_likes_id: 'user987' },
  { community_id: 6, habit_likes_id: 'user123' },
  { community_id: 6, habit_likes_id: 'user111' },
  { community_id: 6, habit_likes_id: 'user555' },

  { community_id: 7, habit_likes_id: 'user654' },
  { community_id: 7, habit_likes_id: 'user123' },
  { community_id: 7, habit_likes_id: 'user789' },
  { community_id: 7, habit_likes_id: 'user101' },
  { community_id: 7, habit_likes_id: 'user234' },
  { community_id: 7, habit_likes_id: 'user345' },
  { community_id: 7, habit_likes_id: 'user456' },
  { community_id: 7, habit_likes_id: 'user567' },
  { community_id: 7, habit_likes_id: 'user876' },
  { community_id: 7, habit_likes_id: 'user543' },
  { community_id: 7, habit_likes_id: 'user321' },

  { community_id: 8, habit_likes_id: 'user543' },
  { community_id: 8, habit_likes_id: 'user123' },
  { community_id: 8, habit_likes_id: 'user456' },
  { community_id: 8, habit_likes_id: 'user654' },
  { community_id: 8, habit_likes_id: 'user101' },
  { community_id: 8, habit_likes_id: 'user789' },
  { community_id: 8, habit_likes_id: 'user111' },
  { community_id: 8, habit_likes_id: 'user222' },
  { community_id: 8, habit_likes_id: 'user333' },
  { community_id: 8, habit_likes_id: 'user444' },
  { community_id: 8, habit_likes_id: 'user555' },
  { community_id: 8, habit_likes_id: 'user666' },

  { community_id: 9, habit_likes_id: 'makeupminimalist' },
  { community_id: 9, habit_likes_id: 'user999' },
  { community_id: 9, habit_likes_id: 'user654' },
  { community_id: 9, habit_likes_id: 'user321' },
  { community_id: 9, habit_likes_id: 'user765' },
  { community_id: 9, habit_likes_id: 'user987' },
  { community_id: 9, habit_likes_id: 'user543' },
  { community_id: 9, habit_likes_id: 'user876' },
  { community_id: 9, habit_likes_id: 'user654' },
  { community_id: 9, habit_likes_id: 'user123' },
  { community_id: 9, habit_likes_id: 'user101' },

  { community_id: 10, habit_likes_id: 'user432' },
  { community_id: 10, habit_likes_id: 'user123' },
  { community_id: 10, habit_likes_id: 'user987' },
  { community_id: 10, habit_likes_id: 'user543' },
  { community_id: 10, habit_likes_id: 'user111' },
  { community_id: 10, habit_likes_id: 'user222' },
  { community_id: 10, habit_likes_id: 'user333' },
  { community_id: 10, habit_likes_id: 'user444' },
  { community_id: 10, habit_likes_id: 'user555' },
  { community_id: 10, habit_likes_id: 'user666' },
  { community_id: 10, habit_likes_id: 'user777' },
  { community_id: 10, habit_likes_id: 'user888' },
  { community_id: 10, habit_likes_id: 'user999' },
  { community_id: 10, habit_likes_id: 'user101' },
  { community_id: 10, habit_likes_id: 'user234' },
  { community_id: 10, habit_likes_id: 'user345' },
]);

// 필터링된 데이터
const filteredRoutines = ref([...routineCommunityArray.value]); // 기본값으로 전체 데이터

// 각종 메소드

// 1. 좋아요 버튼 누르는 로직

// 현재 사용자 id를 임의로 Money.java로 해놓겠음
const currentId = 'Money.java';

function isLiked(communityId) {
  return likesArray.value.some(
    (like) =>
      like.community_id === communityId && like.habit_likes_id === currentId
  );
}

function toggleLike(communityId) {
  const routine = routineCommunityArray.value.find(
    (r) => r.community_id === communityId
  );

  if (isLiked(communityId)) {
    // 좋아요를 눌러놓은 상태면
    // 좋아요 취소
    const index = likesArray.value.findIndex(
      (like) =>
        like.community_id === communityId && like.habit_likes_id === currentId
    );
    if (index !== -1) {
      // index가 있는지 확인차 한번더 체크
      routine.habit_likes -= 1; // 1. routineCommunityArray에서 좋아요 수 -1
      likesArray.value.splice(index, 1); // 2. likesArray에서 삭제

      updateHabitLikes(communityId, -1); // DB 업데이트: 좋아요 수 감소
    }
  } else {
    routine.habit_likes += 1; // 1. 좋아요 수 +1
    likesArray.value.push({
      community_id: communityId,
      habit_likes_id: currentId,
    }); // 2. likesArray에 추가

    updateHabitLikes(communityId, 1); // DB 업데이트: 좋아요 수 증가
  }
}

function updateHabitLikes(communityId, value) {
  // DB 업데이트 로직
  // 1. '좋아요' 테이블에 community_id랑 habit_likes_id 현 사용자 id로 추가, 혹은 그 row를 삭제하기
  // 2. '루틴 커뮤니티' 테이블의 '좋아요 수' 필드에 +1 혹은 -1 처리하기
  console.log(`community_id: ${communityId}, like 변화: ${value}`);
}

// // 이미지 경로를 import하여 사용할 수 있음 (빌드 과정에서 경로 처리)
// const fullLikeImg = require('@/assets/icons/fullLike.png');
// const emptyLikeImg = require('@/assets/icons/emptyLike.png');

// 우측 필터링

// 오른쪽 필터 상태, 최신순이 기본값
const selectedOtherFilter = ref('recent');

// 2. 나의 좋아요만 필터링
// currentId 현재 사용자 id로 판단

// const isMyLikesFilterActive = ref(false); // 나의 좋아요 필터 상태 저장

function checkMyLike() {
  // currentId 사용자가 좋아요한 community_id 리스트 추출
  const likedCommunityIds = likesArray.value
    .filter((like) => like.habit_likes_id === currentId)
    .map((like) => like.community_id);

  // routineCommunityArray에서 해당 community_id를 가진 루틴만 필터링
  const likedRoutines = routineCommunityArray.value.filter((routine) =>
    likedCommunityIds.includes(routine.community_id)
  );

  // 필터링된 결과가 없을 경우
  // if (likedCommunityIds.length === 0) {
  //     return []; // 또는 전체 루틴을 반환하거나 원하는 값을 반환
  // }

  return likedRoutines;
}

// // 3. 좋아요순
// function filterByMostLiked() {
//     return routineCommunityArray.value.slice().sort((a, b) => b.habit_likes - a.habit_likes);
// }

// // 4. 최신순
// function filterByRecent() {
//     return routineCommunityArray.value.slice().sort((a, b) => new Date(b.update_date) - new Date(a.update_date));
// }

// // 5. 참여자순
// function filterByMostParticipants() {
//     return routineCommunityArray.value.slice().sort((a, b) => b.participants - a.participants);
// }

// // 6. 달성자순
// function filterByMostCompletions() {
//     return routineCommunityArray.value.slice().sort((a, b) => b.complete - a.complete);
// }

// ******** 오른쪽 필터 처리
function handleFilterChange(event) {
  selectedOtherFilter.value = event.target.value;
  applyFilters(); // 필터가 변경될 때마다 필터를 적용
}

// 카테고리 필터링

// 카테고리 필터 상태, 디폴트는 전체 카테고리
const selectedCategory = ref('all');

// 카테고리 맵 (카테고리 필터와 데이터의 매칭)
const categoryMap = {
  food: '식비',
  dessert: '카페/간식',
  beauty: '뷰티',
};

// ******** 카테고리 필터 처리
function handleCategoryFilterChange(event) {
  selectedCategory.value = event.target.value;
  applyFilters(); // 필터가 변경될 때마다 필터를 적용
}

// 최종 필터 - 카테고리 필터 + 오른쪽 필터
function applyFilters() {
  let result = [...routineCommunityArray.value];

  // 1. '카테고리 필터' 적용
  if (selectedCategory.value !== 'all') {
    result = result.filter(
      (routine) =>
        routine.category_title === categoryMap[selectedCategory.value]
    );
    // 루틴 데이터의 카테고리 타이틀이 '식비', selectedCategory가 select form의 value (food) (이벤트니까)
  }

  // 2. '오른쪽 필터' 적용
  if (selectedOtherFilter.value === 'myLikes') {
    result = result.filter((routine) => checkMyLike().includes(routine));
  } else if (selectedOtherFilter.value === 'manyLikes') {
    result = result.sort((a, b) => b.habit_likes - a.habit_likes);
  } else if (selectedOtherFilter.value === 'recent') {
    result = result.sort(
      (a, b) => new Date(b.update_date) - new Date(a.update_date)
    );
  } else if (selectedOtherFilter.value === 'manyParticipants') {
    result = result.sort((a, b) => b.participants - a.participants);
  } else if (selectedOtherFilter.value === 'manyCompletions') {
    result = result.sort((a, b) => b.complete - a.complete);
  }

  // 필터링된 결과를 `filteredRoutines`에 할당
  filteredRoutines.value = result;
}

// ****** 다 모아져있는 '오른쪽 필터' 핸들러
// function handleFilterChange(event) {
//     const filterValue = event.target.value;

//     // 필터 상태에 따라 데이터 필터링
//     if (filterValue === 'myLikes') { // 2. 나의 좋아요
//         filteredRoutines.value = checkMyLike();
//     } else if (filterValue === 'manyLikes') { // 3. 좋아요순
//         filteredRoutines.value = filterByMostLiked();
//     } else if (filterValue === 'recent') { // 4. 최신순
//         filteredRoutines.value = filterByRecent();
//     } else if (filterValue === 'manyParticipants') { // 5. 참여자순
//         filteredRoutines.value = filterByMostParticipants();
//     } else if (filterValue === 'manyCompletions') { // 6. 달성자순
//         filteredRoutines.value = filterByMostCompletions();
//     } else {
//         // 기본값으로 전체 데이터
//         filteredRoutines.value = [...routineCommunityArray.value]; // 전체 데이터를 복사
//     }
// }
</script>

<style scoped>
@font-face {
  font-family: 'Wanted Sans';
  src: url('@/assets/fonts/WantedSans-Regular.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

* {

    font-family: "Wanted Sans";

}

/* 화면 줄이면 !!*/
@media (max-width: 850px) {
  .main {
    grid-template-columns: 1fr; /* 한 줄로 변경 */
  }

  /* intro는 헤더 바로 아래에 */
  .intro {
    /* grid-column: 1; 첫 번째 열에 위치 */
    display: none;
  }

  /* cards는 화면 가운데 */
  .cards {
    grid-column: 1; /* cards도 1열 */
    display: flex;
    justify-content: center;
    width: 100%; /* 가로 너비 채우기 */
  }
}

/* 전체 배치 */

.filter {
  margin: 3% 8%;
  display: grid;
  grid-template-columns: 2fr;
}

.main {
  margin: 2% 8%;

  display: grid;
  /* grid-template-columns: 20% 80%; */
  grid-template-columns: 1fr 3fr;
}

.categoryFilter {
  grid-column: 1;
}

.otherFilter {
  grid-column: 2;
}

.intro {
  margin: 0 1%;

  grid-column: 1;
}

/* .col {
    flex: 1 1 30%; 
    min-width: 200px; 
} */

.cards {
  /* width: 80%; */
  /* justify-self: end; 오른쪽에 정렬 */
  grid-column: 2;
  /* min-width: 200px; 비었을 때 최소 높이 */
}

/* ************ 인트로 세부 ************ */
.intro p {
  margin-top: 10%;
}

/* ************ 카드 세부 ************ */
.card {
  border-radius: 0;
}

.card-title {
  margin-top: 5%;
  font-weight: 900;
}

.subtitle {
  display: flex;
}
.subtitle .type {
  background-color: black;
  color: white;
  font-size: 12px;
  padding: 2%;
  margin-bottom: 10%;
}
.subtitle .date {
  font-size: 12px;
  color: rgb(82, 82, 82);
  margin-left: auto;
}

/* 카드 안의 작성자, 좋아요 배치 */
.card-text {
  display: flex;
  justify-content: space-between;
  /* 양쪽 끝에 붙이고, 가운데에 균등한 공간을 배치!! 부모 영역에 space-between 걸면 자식요소들이..*/
  /* |Item 1            Item 2            Item 3| */
  align-items: center;
}

/* 작성자 아바타 & 닉네임 */
.avatar {
  width: 18px;
  height: 18px;
  border-radius: 50%;
}

.writerName {
  font-size: 12px;
  margin-left: 5px;
}

/* 좋아요 컨테이너 */
.likeContainer {
  display: flex;
  flex-direction: column; /* 세로 정렬 */
  align-items: flex-end; /* 오른쪽 정렬 */
}

.likeButton {
  border: none;
  background-color: transparent;
}

.likeImg {
  width: 22px; /* 아이콘 크기 조절 */
  margin-bottom: -3px; /* like와 likeComment 사이의 간격 */
  margin-right: 0px;
  /* border: 0px; */
}

.likeComment {
  font-size: 11px; /* 텍스트 크기 */
  text-align: right; /* 오른쪽 정렬 */
  margin-right: 13px;
}

/* 도전 컨테이너 -- 멘트 & 버튼 */
.challengeContainer {
  display: flex; /* flexbox 레이아웃 활성화 */
  flex-direction: column; /* 요소들을 수직으로 쌓기 */
  justify-content: center;
}
.challengeComment {
  font-size: 13px;
  margin-top: 20%;
  margin-bottom: 5px;
  text-align: center;
}
.challengeIcon {
  width: 18px;
  margin-right: 2px;
}

.challengeButton {
  background-color: rgb(0, 0, 0);
  border: 0;
  border-radius: 0;
}
.challengeButtonText {
  font-size: 14px;
}
</style>
