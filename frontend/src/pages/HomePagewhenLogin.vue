<template>
    
    <div class="honeyBg">
        <Header/>

        <div class="slogan">
            <div class="image-container">
                <img v-for="(img, index) in imageCount" :key="index" :src="sloganSrc" />
            </div>
        </div>

        <div class="dailyBRTN">
            <div class="dailyComment">
                <h1>오늘의 벌루틴</h1>
                <p>오늘의 꿀단지를 가득 채워보세요!</p>
                <p>오늘, {{ todayActiveRoutinesCount }}개의 루틴에 대한 달성률은 {{ todayPercent }}%입니다.</p>
                <p></p>
                <p>{{ totalSavedToday }}원을 절약할 수 있었어요!</p>
                <div>
                    <p>{{formattedDate}}의 벌루틴</p>
                    <ul>
                        <li v-for="(habit, index) in todayActiveRoutines" :key="index">
                        <span :style="{ textDecoration: habit.isCheckedToday ? 'line-through' : 'none' }">
                            {{ habit.habitTitle }}
                        </span>
                        </li>
                    </ul>
                    
                </div>
            </div>
            
            <div class="potBeeContainer">
                <img class="beeImg" src="@/assets/images/bee/bee1.png">
                <img class="pot" :src="getImageForPercent">
            </div>
        </div>
    </div>

    <div class="monthlyBRTN">
        <!-- 그라데이션 사각형 -->
        <img class="gradation" src="@/assets/images/honeycomb/gradation.png">

        <!-- 달 선택 -->
        <select name="month" id="month" v-model="selectedMonth" @change="handleMonthChange">
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
        </select>

        <!-- 벌집 -->
        <!-- <div class="honeycomb" v-show="!isLoading" > -->
        <div class="honeycomb" >
            <!-- :style="{ background: `url('@/assets/images/honeycomb/0.png')` }" -->
            <div 
                v-for="(day, index) in daysInMonth" 
                :key="index" 
                class="hexagon"
            >
                <img :src="imageSources[index]" class="hexagon-image" />
                <span>{{ day }}</span>
            </div>
        </div>
        <div class="monthlyComment">
            <h1>이달의 벌루틴</h1>
            <p>이번 한달의 벌집은 얼마나 채워졌을까요?</p>    
        </div>
    </div>

    <div>
        <h1>많이 찾는 벌루틴에 참여해보세요</h1>
        <div>
            

        </div>
    </div>
</template>



<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import Header from '@/components/global/Header.vue';

// 벌집 캘린더
const daysInMonth = ref(31); // 기본 31일
// 각 날짜별로 체크한 루틴 개수를 담을 배열
const routineData = ref([]);
const isLoading = ref(true); // 로딩 상태 관리

const todayChecked = ref(0);
const formattedDate = `${new Date().getMonth() + 1}/${new Date().getDate()}`;

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
    const month = date.getMonth()+1 ;
    console.log(year, month, date)

  const storedUserId = localStorage.getItem('userId');
  if (storedUserId) {
    fetchRoutineData(year, month, 1)

  }


    
});


// 백엔드 axios
const fetchRoutineData = async (year, month, userId) => {
    try {
        isLoading.value = true; // 로딩 시작
        const response = await axios.get(`http://localhost:8080/habits/habit-checks/${year}/${month}`, {
            params: {
                userId: 1 // 꼭 바꿔야 됨 !!!!!
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
    const userId = 1; // 예시 userId

    // 선택된 달과 유저 ID에 맞춰 루틴 데이터 로드
    fetchRoutineData(year, month, userId);
};

// 페이지 로드 시 기본적으로 현재 달 데이터를 가져옴
// fetchRoutineData(new Date().getFullYear(), new Date().getMonth() + 1, 1);



// 오늘 '진행중인' 루틴 불러오기 - pinia & 달성 개수, 달성률
import { useHabitStore } from '@/stores/habitStore'; // pinia

const habitStore = useHabitStore();
const todayActiveRoutines = habitStore.activeHabits;
const todayActiveRoutinesCount = ref(todayActiveRoutines.length); // 개수

const todayPercent = computed(() => {
  return todayActiveRoutinesCount > 0 ? (todayActiveRoutinesCount.value / todayActiveRoutinesCount) * 100 : 0;
});

onMounted(async () => {
  await habitStore.getHabitsFromServer(1); // userId 넣고 !! 꼭 바꿔야 함 !!!!!
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

  if (percent === 0) return new URL('@/assets/images/pot/0.png', import.meta.url).href;
  else if (percent > 0 && percent <= 20) return new URL('@/assets/images/pot/20.png', import.meta.url).href;
  else if (percent > 20 && percent <= 40) return new URL('@/assets/images/pot/40.png', import.meta.url).href;
  else if (percent > 40 && percent <= 60) return new URL('@/assets/images/pot/60.png', import.meta.url).href;
  else if (percent > 60 && percent <= 80) return new URL('@/assets/images/pot/80.png', import.meta.url).href;
  else return new URL('@/assets/images/pot/100.png', import.meta.url).href;
});




// 슬로건 마퀴효과
const imageCount = 10;
const sloganSrc = new URL('@/assets/images/slogan/slogan_update.png', import.meta.url).href;


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
    height: 1000px;
    background-size: cover;


    /* padding-top: 30px; */
}

/* 슬로건 마퀴효과 */
.slogan {
    width: 100%; 
    height: 100px; 
    margin-top: 30px;
    margin-bottom: 50px;
    
    overflow: hidden; /* 넘치는 부분 숨김 */
    position: relative;
}

.slogan .image-container {
    display: flex; /* 이미지들이 옆으로 나란히 배치 */
    animation: marquee 100s linear infinite; /* 애니메이션 설정 */
    width: max-content; /* 이미지들이 옆으로 계속 이어지도록 설정 */
}

.slogan img {
    display: inline-block; 
    height: 100px;
    object-fit: contain; 
}

@keyframes marquee {
    0% {
        transform: translateX(0); /* 처음 위치 */
    }
    100% {
        transform: translateX(-100%); /* 이미지 컨테이너 전체를 이동 */
    }
}

/*벌 이미지 */
.beeImg {
    width: 150px;
}

/* 데일리 꿀단지 */
.dailyBRTN {
    display: grid;
    grid-template-columns: 1fr 2fr;
    justify-items: center;
    /* align-items: center; */

    margin: 10% 8%;
}

.dailyComment {
    grid-column: 1;
}

.dailyComment h1, .monthlyBRTN h1 {
    font-weight: 900;
}

.potBeeContainer {
    grid-column: 2;
    display: flex;
    align-items: center;
    gap: -10px;
}

.pot {
    width: 350px;
}


/* 먼슬리 벌집 */
.monthlyBRTN {
    display: grid;
    grid-template-columns: 1fr 5fr 4fr;
    align-items: center; /* 벌집이 수직으로 가운데 정렬되도록 조정 */

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
    grid-template-columns: repeat(6, calc(100px - 39px)); /*너비를 1fr이 아니라 딱 들어맞게 줄임 */
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




</style>

