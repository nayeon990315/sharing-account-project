<template>

  <div class="honeyBg">
    <Header />



    <div class="dailyBRTN">
      <div class="dailyComment">
        <h1>오늘의 벌루틴 모으기</h1>
        <p>오늘의 꿀단지를 가득 채워보세요!</p>

        <!-- <img class = "boxImg" src="@/assets/images/bee/box.png" > -->
        <!-- <p>오늘, {{ todayActiveRoutinesCount }}개의 루틴에 대한 달성률은 {{ todayPercent }}%입니다.</p>
                <p></p>
                <p>{{ totalSavedToday }}원을 절약할 수 있었어요!</p> -->
        <div class="boxDiv">
          <!-- <h4>{{formattedDate}}의 벌루틴</h4> -->
          <h4>"조금만 더 힘내보세요!"</h4>
          <ul>
            <li v-for="(habit, index) in todayActiveRoutines" :key="index">
              <img v-if="habit.isCheckedToday" src="@/assets/images/check/true.png" alt="Checked">
              <img v-else src="@/assets/images/check/false.png" alt="Unchecked">
              <span :style="{ textDecoration: habit.isCheckedToday ? 'line-through' : 'none' }">
                {{ habit.habitTitle }}
              </span>
            </li>
          </ul>

        </div>
      </div>

      <div class="potBeeContainer">
        <img class="beeImg" src="@/assets/images/bee/bee_update.png">
        <img class="pot" :src="getImageForPercent">
        <div class="potComment">
          <p class="date">{{ month }}월 {{ day }}일</p>
          <!-- <p>{{ todayCheckedRoutinesCount }} / {{ todayActiveRoutinesCount }}개 달성</p> -->
          <p class="percent">{{ todayPercent }}% 달성</p>
        </div>
      </div>
    </div>
  </div>

  <div class="monthlyBRTN">


    <!-- 달 선택 -->
    <!-- <select name="month" id="month" v-model="selectedMonth" @change="handleMonthChange">
      <option value="jan">1월</option>
      <option value="feb">2월</option>
      <option value="mar">3월</option>
      <option value="apr">4월</option>
      <option value="may">5월</option>
      <option value="june">6월</option>
      <option value="july">7월</option>
      <option value="aug">8월</option>
      <option value="sep">9월</option>
      <option value="oct">10월</option>
      <option value="nov">11월</option>
      <option value="dec">12월</option>
    </select> -->
    <!-- 그라데이션 사각형 -->
    <img class="gradation" src="@/assets/images/honeycomb/gradation2.png">

    <!-- 벌집 -->
    <!-- <div class="honeycomb" v-show="!isLoading" > -->
    <div class="honeycomb">
      <!-- :style="{ background: `url('@/assets/images/honeycomb/0.png')` }" -->
      <div v-for="(day, index) in daysInMonth" :key="index" class="hexagon">
        <img :src="imageSources[index]" class="hexagon-image" />
        <span>{{ day }}</span>
      </div>
    </div>
    <div class="monthlyComment">
      <h1>이달의 벌루틴 모으기</h1>
      <p>이번 한달의 벌집은 얼마나 채워졌을까요?</p>
    </div>
  </div>

  <div class="slogan">
    <div class="image-container">
      <img v-for="(img, index) in imageCount" :key="index" :src="sloganSrc" />
    </div>
  </div>

  <div class="community">
    <h1>많이 찾는 벌루틴에 참여해보세요</h1>
    <div class="top-likes-section">
      <h3>Top 3 Most Liked Routines</h3>
      <div class="grid-container">
        <!-- 좋아요 많은 순서대로 상위 3개 루틴 표시 -->
        <div class="grid-item" v-for="routine in topLikedRoutines" :key="routine.communityId">
          <div class="card h-100">
            <div class="card-body">
              <div class="subtitle">
                <span class="type card-subtitle">{{ routine.categoryTitle }}</span>
                <span class="date card-subtitle">{{ routine.uploadDate }}</span>
              </div>
              <h5 class="card-title">{{ routine.habitTitle }}</h5>
              <div class="card-text">
                <div class="writer">
                  <img class="avatar" :src="routine.avatar || defaultAvatar" />
                  <span class="writerName">{{ routine.nickname }}</span>
                </div>
                <div class="likeContainer">
                  <button class="likeButton" @click="toggleLike(routine.communityId)">
                    <img class="likeImg" :src="isLiked(routine.communityId) ? fullLike : emptyLike" alt="like" />
                  </button>
                  <span class="likeComment">{{ routine.habitLikes }}</span>
                </div>
              </div>
              <div class="challengeContainer">
                <p class="challengeComment">{{ routine.participants }}명이 루틴에 참여 중</p>
                <button @click="addHabitToMyHabit(routine.habitId, routine.communityId)"
                  class="challengeButton btn btn-primary">
                  <img class="challengeIcon" src="@/assets/icons/together_invertColor.png" />
                  <span class="challengeButtonText">함께 도전하기</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Alert Modal -->
  <CustomModal :isVisible="isModalVisible" title="알림" :message="modalMessage" @close="closeModal" />

</template>



<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import Header from '@/components/global/Header.vue';
import CustomModal from '@/components/Modal.vue';

// 벌집 캘린더
const daysInMonth = ref(31); // 기본 31일
// 각 날짜별로 체크한 루틴 개수를 담을 배열
const routineData = ref([]);
const isLoading = ref(true); // 로딩 상태 관리

const todayChecked = ref(0);
const formattedDate = `${new Date().getMonth() + 1}/${new Date().getDate()}`;
const month = new Date().getMonth() + 1;
const day = new Date().getDate();

const isModalVisible = ref(false);
const modalMessage = ref('');
// Modal 표시 함수
const openModal = (message) => {
  modalMessage.value = message;
  isModalVisible.value = true;
};

// Modal 닫기 함수
const closeModal = () => {
  isModalVisible.value = false;
};



const imageSources = ref([]);
const getImageForRoutine = (completedRoutines) => {
  const timestamp = new Date().getTime();

  if (completedRoutines === 0) return new URL('@/assets/images/honeycomb/0.png', import.meta.url).href;
  else if (completedRoutines === 1) return new URL('@/assets/images/honeycomb/20.png?timestamp=${timestamp}', import.meta.url).href;
  else if (completedRoutines === 2) return new URL('@/assets/images/honeycomb/40.png?timestamp=${timestamp}', import.meta.url).href;
  else if (completedRoutines === 3) return new URL('@/assets/images/honeycomb/60.png?timestamp=${timestamp}', import.meta.url).href;
  else if (completedRoutines === 4) return new URL('@/assets/images/honeycomb/80.png?timestamp=${timestamp}', import.meta.url).href;
  else return new URL('@/assets/images/honeycomb/100.png?timestamp=${timestamp}', import.meta.url).href;
};

// // 초기화 시 이미지 경로 설정
// const setImageSources = () => {
//     for (let i = 0; i < daysInMonth.value; i++) {
//         imageSources.value.push(getImageForRoutine(routineData.value[i]));
//     }
// };

// // 페이지 로드 시 이미지 경로 설정
// setImageSources();
onMounted(() => {
  const date = new Date();

  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  console.log(year, month, date)

  const storedUserId = localStorage.getItem('userId');
  if (storedUserId) {
    fetchRoutineData(year, month, storedUserId)

  }



});


// 백엔드 axios
const fetchRoutineData = async (year, month, userId) => {
  try {
    isLoading.value = true; // 로딩 시작
    const response = await axios.get(`http://localhost:8080/habits/habit-checks/${year}/${month}`, {
      params: {
        userId: userId // 꼭 바꿔야 됨 !!!!!
      }
    });

    console.log("응답 데이터:", response.data);
    routineData.value = response.data.map(item => item.habit_count);

    const today = new Date().getDate();
    // 오늘 달성한 루틴 개수 저장
    todayChecked.value = routineData.value[today - 1] || 0;
    console.log(todayChecked);

    // 이미지 경로 설정
    await setImageSources();

    // 달에 맞게 daysInMonth 설정 (31일, 30일, 28일 등)
    daysInMonth.value = routineData.value.length;
  } catch (error) {
    console.error("Error fetching routine data:", error);
  } finally {
    isLoading.value = false; // 로딩 완료
  }
};


// 이미지 경로 설정 함수
const setImageSources = () => {
  imageSources.value = []; // 기존 이미지를 초기화

  // 오늘 날짜
  const today = new Date();
  const currentDay = today.getDate();


  // 1일부터 오늘까지
  for (let i = 0; i < currentDay; i++) {
    const routineCount = routineData.value[i] || 0; // 값이 없으면 0으로 처리
    imageSources.value.push(getImageForRoutine(routineCount));
  }

  // 오늘 이후 날짜에는 "notYet"
  for (let i = currentDay; i < daysInMonth.value; i++) {
    imageSources.value.push(new URL('@/assets/images/honeycomb/notYet.png?timestamp=${new Date().getTime()}', import.meta.url).href);
  }
};

// 달이 선택될 때마다 데이터 로드를 트리거하는 함수
const handleMonthChange = (event) => {
  const selectedMonth = event.target.value; // 선택된 달 값 ('jan', 'feb' 등)
  const monthMapping = {
    jan: 1,
    feb: 2,
    mar: 3,
    apr: 4,
    may: 5,
    june: 6,
    july: 7,
    aug: 8,
    sep: 9,
    oct: 10,
    nov: 11,
    dec: 12
  };

  const year = new Date().getFullYear(); // 현재 연도
  const month = monthMapping[selectedMonth]; // 선택된 달에 해당하는 숫자 (1 ~ 12)
  const userId = localStorage.getItem('userId');

  // 선택된 달과 유저 ID에 맞춰 루틴 데이터 로드
  fetchRoutineData(year, month, userId);
};

// 페이지 로드 시 기본적으로 현재 달 데이터를 가져옴
// fetchRoutineData(new Date().getFullYear(), new Date().getMonth() + 1, 1);



// 오늘 '진행중인' 루틴 불러오기 - pinia & 달성 개수, 달성률
import { useHabitStore } from '@/stores/habitStore'; // pinia

const habitStore = useHabitStore();
const todayActiveRoutines = habitStore.activeHabits;
const todayActiveRoutinesCount = ref(todayActiveRoutines.length); // 오늘 진행 중인 루틴 개수
const todayCheckedRoutinesCount = todayActiveRoutines.filter(habit => habit.isCheckedToday).length; // 오늘 달성한 루틴 개수 


const todayPercent = computed(() => {
  return todayActiveRoutinesCount > 0 ? Math.floor((todayCheckedRoutinesCount / todayActiveRoutinesCount) * 100) : 0;
});

const getUserIdFromToken = async () => {
  const jwtToken = $cookies.get('jwtToken');
  if (!jwtToken) {
    openModal('로그인이 필요합니다!');
    this.$router.push('/login'); // Vue Router를 사용하여 리다이렉트
    return;
  }
  console.log(jwtToken); try {
    // axios를 이용해 백엔드의 findId 컨트롤러에 JWT 토큰을 전송
    const response = await axios.post('http://localhost:8080/users/findId', {}, {
      headers: {
        'Authorization': `Bearer ${jwtToken}` // JWT 토큰을 Authorization 헤더에 추가
      }
    });
    console.log(response.data)
    // 응답으로 받은 userId를 localStorage에 저장
    const userId = response.data.userId;
    const nickname = response.data.nickname;
    localStorage.setItem('userId', userId);
    localStorage.setItem('nickname', nickname);
    console.log('사용자 ID가 localStorage에 저장되었습니다:', userId);
  } catch (error) {
    console.error('사용자 정보를 가져오지 못했습니다:', error);
    if (error.response && error.response.status === 401) {
      openModal('인증 오류: 로그인이 필요합니다.');
      this.$router.push('/login');
    }
  }
};

onMounted(async () => {

  await getUserIdFromToken();
  await habitStore.getHabitsFromServer(localStorage.getItem('userId')); // userId 넣고 !! 꼭 바꿔야 함 !!!!!
  //   todayActiveRoutinesCount.value = habitStore.activeHabits.length;
  console.log("진행 중인 루틴:", todayActiveRoutines);
  console.log("진행 중인 루틴 개수:", todayActiveRoutinesCount);
});

// 오늘 절약한 금액 계산
const totalSavedToday = computed(() => {
  return todayActiveRoutines
    .filter(habit => habit.isCheckedToday) // 완료된 루틴만 필터링
    .reduce((sum, habit) => sum + habit.saveAmount, 0); // saveAmount 합산
});






// 꿀단지 바꾸기
// 달성률에 따른 이미지 변경 로직
const getImageForPercent = computed(() => {
  const percent = todayPercent.value;

  if (percent === 0) return new URL('@/assets/images/pot/0_update2.png', import.meta.url).href;
  else if (percent > 0 && percent < 30) return new URL('@/assets/images/pot/20~30.png', import.meta.url).href;
  else if (percent >= 30 && percent < 50) return new URL('@/assets/images/pot/31~49.png', import.meta.url).href;
  else if (percent >= 50 && percent < 60) return new URL('@/assets/images/pot/50~59.png', import.meta.url).href;
  else if (percent >= 60 && percent < 70) return new URL('@/assets/images/pot/60~70.png', import.meta.url).href;
  else if (percent >= 70 && percent < 100) return new URL('@/assets/images/pot/71~99.png', import.meta.url).href;
  else return new URL('@/assets/images/pot/100.png', import.meta.url).href;
});




// 슬로건 마퀴효과
const imageCount = 10;
const sloganSrc = new URL('@/assets/images/slogan/slogan_update.png', import.meta.url).href;





// 커뮤니티 섹션
import fullLike from '@/assets/icons/fullLike.png';
import emptyLike from '@/assets/icons/emptyLike.png';
import defaultAvatarImg from '@/assets/images/sample.jpg';

const defaultAvatar = defaultAvatarImg;

const topLikedRoutines = ref([]); // 상위 3개의 루틴 저장

// 컴포넌트 로드 시 좋아요가 많은 루틴 상위 3개를 가져옴
const fetchTopLikedRoutines = async () => {
  try {
    const response = await axios.get('http://localhost:8080/routine-community/top-liked', {

    });
    topLikedRoutines.value = response.data;
  } catch (error) {
    console.error('상위 루틴 가져오기 오류:', error);
  }
};

// 좋아요 토글 함수
async function toggleLike(communityId) {
  const routine = topLikedRoutines.value.find(r => r.communityId === communityId);
  const userId = localStorage.getItem('userId');

  if (isLiked(communityId)) {
    routine.habitLikes -= 1;
    topLikedRoutines.value = topLikedRoutines.value.filter(like => like.communityId !== communityId);
    try {
      await axios.delete('http://localhost:8080/routine-community/like', {
        data: { userId, communityId },
      });
    } catch (error) {
      routine.habitLikes += 1;
      topLikedRoutines.value.push(communityId);
    }
  } else {
    routine.habitLikes += 1;
    topLikedRoutines.value.push(communityId);
    try {
      await axios.post('http://localhost:8080/routine-community/like', { userId, communityId });
    } catch (error) {
      routine.habitLikes -= 1;
      topLikedRoutines.value = topLikedRoutines.value.filter(like => like.communityId !== communityId);
    }
  }
}

// 좋아요 여부 확인
function isLiked(communityId) {
  return topLikedRoutines.value.some(like => like.communityId === communityId);
}

async function addHabitToMyHabit(habitId, communityId) {
  const userId = localStorage.getItem('userId');  // 실제 로그인된 사용자 ID로 변경

  // 먼저 habitStore에 해당 habitId가 이미 존재하는지 확인
  const existingHabit = habitStore.habits.find(habit => habit.habitId === habitId);

  //이미 공유한 루틴인지 확인
  if (existingHabit) {
    openModal('이미 추가한 루틴입니다!');
    return;
  }
  try {
    // POST 요청에서 params를 통해 userId와 habitId 전달
    await axios.post('http://localhost:8080/routine-community/challenge', null, {
      params: {
        userId: userId,
        habitId: habitId,
        communityId: communityId  // communityId를 habitId로 전달
      }
    });
    console.log('내 루틴에 추가되었습니다.');

    const response = await axios.get(`http://localhost:8080/habits/find`, {
      params: {
        habitId: habitId
      }
    }
    )

    habitStore.habits.push({
      myHabitId: response.data.myHabitId,
      habitId: response.data.habitId,
      writerId: localStorage.getItem("userId"),
      habitTitle: response.data.habitTitle,
      categoryTitle: response.data.categoryTitle,
      state: "대기",
      saveAmount: response.data.saveAmount,
      certification: response.data.certification,
      isCheckedToday: false,
      checkDate: null
    });

    openModal('습관이 MyHabit에 추가되었습니다.');  // 성공 시 알림 표시
  } catch (error) {
    console.error('MyHabit 추가 중 오류 발생:', error);
  }
}

onMounted(() => {
  fetchTopLikedRoutines(); // 컴포넌트가 로드될 때 상위 3개 루틴을 가져옴
});
</script>




<style scoped>
/* 폰트 */
@font-face {
  font-family: 'Wanted Sans';
  src: url('@/assets/fonts/WantedSans-Regular.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

* {

  font-family: "Wanted Sans";

}


/* 꿀 배경화면 */
.honeyBg {
  background: url('@/assets/images/background/background.png');
  height: 800px;
  width: 100%;
  background-size: cover;


  /* padding-top: 30px; */
}

/* 슬로건 마퀴효과 */
.slogan {
  width: 100%;
  height: 30px;
  margin-top: 30px;
  margin-bottom: 0px;

  overflow: hidden;
  /* 넘치는 부분 숨김 */
  position: relative;
}

.slogan .image-container {
  display: flex;
  /* 이미지들이 옆으로 나란히 배치 */
  animation: marquee 100s linear infinite;
  /* 애니메이션 설정 */
  width: max-content;
  /* 이미지들이 옆으로 계속 이어지도록 설정 */
}

.slogan img {
  display: inline-block;
  height: 30px;
  object-fit: contain;
}

@keyframes marquee {
  0% {
    transform: translateX(0);
    /* 처음 위치 */
  }

  100% {
    transform: translateX(-100%);
    /* 이미지 컨테이너 전체를 이동 */
  }
}



/* 데일리 꿀단지 */
.dailyBRTN {
  display: grid;
  grid-template-columns: 1fr 2fr;
  justify-items: center;
  /* align-items: center; */

  margin: 6% 8%;
  position: relative;
}

.dailyComment {
  grid-column: 1;
}

.dailyComment h1,
.monthlyBRTN h1,
.community h1 {
  font-weight: 900;
}

/* .boxImg {
    width: 280px;
    position: absolute;
    ;
} */

.boxDiv {
  width: 290px;
  height: 305px;
  background: url('@/assets/images/bee/box.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;

  padding-right: 30px;
}

.boxDiv h4 {
  font-weight: 700;
  padding-top: 40px;
  padding-left: 30px;
}

.boxDiv ul {
  margin-top: 30px;
  margin-left: 20px;
  list-style: none;
}

.boxDiv ul li span {
  padding-left: 5px;
  padding-right: 30px;
}

.boxDiv ul li img {
  width: 15px;
}

.potBeeContainer {
  grid-column: 2;
  display: flex;
  align-items: center;
  gap: -10px;
  position: relative;
}

.beeImg {
  width: 300px;
  position: absolute;
  left: -250px;
  top: 60px;
}

.pot {
  width: 320px;
}

.potComment {
  position: absolute;
  top: 280px;
  left: 102px;
  text-align: center;
}

.potComment .date {
  font-weight: 600;
}

.potComment .percent {
  margin-top: -10px;
  font-weight: 800;
  font-size: 20px;
}


/* 먼슬리 벌집 */
.monthlyBRTN {
  display: grid;
  grid-template-columns: 1fr 5fr 4fr;
  align-items: center;
  /* 벌집이 수직으로 가운데 정렬되도록 조정 */

  margin: 5% 8%;
}

.gradation {
  width: 70px;
  grid-column: 1;
}

#month {
  grid-column: 1;
}

.honeycomb {
  display: grid;
  grid-column: 2;
  grid-template-columns: repeat(6, calc(100px - 39px));
  /*너비를 1fr이 아니라 딱 들어맞게 줄임 */
  grid-gap: 8px;
  justify-content: left;
}

.hexagon {
  width: 80px;
  height: 70px;
  /* background: url('@/assets/images/honeycomb/0.png');
    background-size: cover; */
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.hexagon-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hexagon span {
  position: absolute;
  color: white;
  font-weight: bold;
  font-size: 16px;
}

.honeycomb div:nth-child(odd) {
  transform: translateY(40px);
}


/* 벌집 monthly comment */
.monthlyComment {
  grid-column: 3;
}









/* 커뮤니티 섹션 */
.community {
  margin: 80px 8%;
}

.top-likes-section {
  margin: 3% 8%;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 20px;
}

.grid-item {
  display: flex;
  justify-content: center;
}

.card {
  border-radius: 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.card-title {
  font-weight: 900;
}

.subtitle {
  display: flex;
  justify-content: space-between;
}

.avatar {
  width: 18px;
  height: 18px;
  border-radius: 50%;
}

.writerName {
  margin-left: 5px;
}

.likeContainer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.likeButton {
  border: none;
  background-color: transparent;
}

.likeImg {
  width: 22px;
}

.challengeButton {
  background-color: rgb(0, 0, 0);
  border: 0;
  border-radius: 0;
}
</style>
