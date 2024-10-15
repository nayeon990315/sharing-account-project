<template>
  <div class="container">
    <section class="intro" ref="intro">
      <h3><strong>🐝 벌루틴</strong>과 함께 당신의 자산을 관리하세요.</h3>
      <br>
      <p>내 모든 지출 습관을 한눈에 관리하고 실천해 보세요.</p>
      <p>이제껏 경험하지 못한 쉽고 편리한 습관 관리 서비스,</p>
      <p>벌루틴과 함께라면 당신의 일상이 새로워질 거예요.</p>
    </section>
    <br><br>

    <section class="habit-list" ref="habitList">
      <div class="habit1">
        <h4><strong>🐝 마이 루틴 관리</strong></h4><br>
        <p>현재 진행 중인 모든 습관을 한 곳에서 조회할 수 있어요.</p>
        <p>카테고리별 지출 내역, 총 지출금액, 그리고 체크리스트까지 한 번에 관리하세요.</p>
        <p>오늘 절약할 수 있는 총 예상 금액과 실제로 절약한 금액도 확인해보세요.</p>
        <br>
      </div>
      <img :src="computerImageUrl" alt="Computer Picture" v-if="computerImageUrl" />
    </section>
    <br><br>

    <section class="habit-add" ref="habitAdd">
      <img :src="computerImageUrl" alt="Computer Picture" v-if="computerImageUrl" />
      <div class="habit2">
        <h4><strong>🐝 마이 루틴 편집</strong></h4><br>
        <p>지출을 줄이기 위한 새로운 습관을 작성해보세요.</p>
        <p>제목, 카테고리, 절약 예상 금액, 인증 방법을 입력하여 습관을 추가하고 관리할 수 있어요.</p>
        <br>
      </div>
    </section>
    <br><br>

    <section class="past-habits" ref="pastHabits">
      <div class="habit3">
        <h4><strong>🐝 과거의 나</strong></h4><br>
        <p>내가 과거에 진행했던 습관을 확인하고, 그 성과를 돌아보세요.</p>
        <p>날짜를 선택하여 과거 기록과 오늘의 성과를 비교할 수 있어요.</p>
        <p>오늘과 과거의 총 지출과 달성한 지출 습관을 확인할 수 있어요.</p>   
        <br>
      </div>
      <img :src="computerImageUrl" alt="Computer Picture" v-if="computerImageUrl" />  
    </section>
    <br><br>

    <section class="habit-sharing" ref="habitSharing">
      <img :src="computerImageUrl" alt="Computer Picture" v-if="computerImageUrl" />
      <div class="habit4">
        <h4><strong>🐝 공유 커뮤니티</strong></h4><br>
        <p>다양한 습관을 공유하고 다른 사람들의 지출 습관을 확인하세요.</p>
        <p>커뮤니티에서 서로의 경험을 나누며 동기를 얻을 수 있어요.</p>
        <p>사용자 간의 경험을 공유하여 더 나은 절약 습관을 형성할 수 있어요.</p>
        <p>스크랩한 습관은 My Routine 편집에 대기 상태로 담겨요.</p>
        <br>
      </div>
    </section>
    <br><br>

    <section class="community-certification" ref="communityCertification">
      <div class="habit5">
        <h4><strong>🐝 인증 커뮤니티</strong></h4><br>
        <p>습관을 인증하면 꿀을 1개 얻고, 인증 개수에 따라 레벨이 올라가요.</p>
        <p>닉네임 색깔로 서로의 인증 레벨을 확인할 수 있어요.</p>
        <p>인증을 통해 보상을 받고, 사용자 간의 경쟁심을 유도하여 참여를 촉진할 수 있어요.</p>
        <br>
      </div>
      <img :src="computerImageUrl" alt="Computer Picture" v-if="computerImageUrl" />
    </section>
    <br><br>

    <section class="intuitive-ui" ref="intuitiveUi">
      <div class="habit6">
        <h4><strong>🐝 벌루틴과 함께하세요! 🐝</strong></h4><br>
        <router-link to="/login"><button class="auth-button">벌루틴 시작하기</button></router-link>
      </div>
    </section>
    <br><br><br><br>
  </div>
</template>

<script>
import profileImage from '@/assets/computer.png';
export default {
  name: 'HomePage',
  data() {
    return {
      observer: null,
      computerImageUrl: profileImage,
    };
  },
  mounted() {
    this.initObserver();
  },
  methods: {
    initObserver() {
      const options = {
        root: null,
        rootMargin: '0px',
        threshold: 0.1,
      };

      this.observer = new IntersectionObserver(this.handleIntersect, options);

      const sections = [
        this.$refs.intro,
        this.$refs.habitList,
        this.$refs.habitAdd,
        this.$refs.pastHabits,
        this.$refs.habitSharing,
        this.$refs.communityCertification,
        this.$refs.intuitiveUi,
      ];

      sections.forEach((section) => {
        this.observer.observe(section);
      });
    },
    handleIntersect(entries) {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('fade-in');
        } else {
          entry.target.classList.remove('fade-in');
        }
      });
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: auto;
  overflow-y: hidden;
}

.intro {
  background-color: #fff4c2;
  padding: 20px;
  line-height: 1.5;
  text-align: center;
  margin-top: 50px;
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
}

.habit-list,
.past-habits,
.community-certification {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px;
  line-height: 1.5;
  margin-top: 50px;
  /* margin-left: 50px;
  margin-right: 50px; */
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
  background-color: #ffecb3;
  border-radius: 10px;
}

.habit-add,
.habit-sharing
 {
  display: flex;
  justify-content: space-between;
  text-align: center;
  align-items: center;
  padding: 20px;
  line-height: 1.5;
  margin-top: 50px;
  /* margin-left: 50px;
  margin-right: 50px; */
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
  background-color: #ffecb3;
  border-radius: 10px;
}

.intuitive-ui{
  /* display: flex; */
  justify-content: space-between;
  text-align: center;
  align-items: center;
  padding: 20px;
  line-height: 1.5;
  margin-top: 50px;
  /* margin-left: 50px;
  margin-right: 50px; */
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
  background-color: #ffecb3;
  border-radius: 10px;
}


.habit-list img,
.past-habits img,
.community-certification img {
  margin-right: 20px;
  width: 300px;
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.habit-add img,
.habit-sharing img,
.intuitive-ui img {
  margin-left: 20px;
  width: 300px;
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.fade-in {
  opacity: 1;
  transform: translateY(0);
}

p {
  margin: 10px 0;
  color: #3e2723;
}

h3, h4 {
  color: #ffca28;
}

.link {
  cursor: pointer;
  color: #ffca28;
  text-decoration: none;
}

.link:hover {
  color: #f57f17;
}
/* auth-button 스타일 */
.auth-button {
  background-color: #ffca28; /* 노란색 배경 */
  border: none;
  color: #3e2723; /* 텍스트 색상 */
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 30px; /* 둥근 모서리 */
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease; /* 부드러운 트랜지션 */
}

.auth-button:hover {
  background-color: #f57f17; /* 호버 시 더 진한 노란색 */
  transform: scale(1.05); /* 살짝 커지는 효과 */
}

.auth-button:active {
  background-color: #ffca28; /* 클릭 시 원래 색상 */
  transform: scale(1); /* 클릭 시 원래 크기 */
}
</style>
