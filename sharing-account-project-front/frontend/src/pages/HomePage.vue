<template>
  <div class="container">
    <section class="intro" ref="intro">
      <h3><strong>🐝 벌루틴</strong>과 함께 당신의 자산을 관리하세요.</h3>
      <br />
      <p>내 모든 지출 루틴을 한눈에 관리하고 실천해 보세요.</p>
      <p>이제껏 경험하지 못한 쉽고 편리한 루틴 관리 서비스,</p>
      <p>벌루틴과 함께라면 당신의 일상이 새로워질 거예요.</p>
    </section>
    <br /><br />

    <section class="habit-list" ref="habitList">
      <div class="habit1">
        <h4><strong>🐝 루틴별 지출</strong></h4>
        <br />
        <p>현재 진행 중인 모든 루틴을 한 곳에서 조회할 수 있어요.</p>
        <p>
          카테고리별 지출 내역, 총 지출금액, 그리고 체크리스트까지 한 번에
          관리하세요.
        </p>
        <p>
          오늘 절약할 수 있는 총 예상 금액과 실제로 절약한 금액도 확인해보세요.
        </p>
        <br />
      </div>
      <img
        src="@/assets/images/homepage/myroutine1.png"
        alt="My Routine Management"
      />
    </section>
    <br /><br />

    <section class="habit-add" ref="habitAdd">
      <img src="@/assets/images/homepage/routineedit1.png" alt="Routine Edit" />
      <div class="habit2">
        <h4><strong>🐝 마이 루틴 설정</strong></h4>
        <br />
        <p>지출을 줄여보기 위한 새로운 루틴을 작성해보세요.</p>
        <p>
          루틴 추가 및 루틴 상태를 편집할 수 있고, 제목, 카테고리, 절약 예상
          금액, 인증 방법을 입력하여 루틴을 추가하고 관리할 수 있어요.
        </p>
        <br />
      </div>
    </section>
    <br /><br />

    <section class="past-habits" ref="pastHabits">
      <div class="habit3">
        <h4><strong>🐝 절약 : 나 vs 나 </strong></h4>
        <br />
        <p>내가 과거에 진행했던 루틴을 확인하고, 그 성과를 돌아보세요.</p>
        <p>특정 날짜를 선택하여 과거 기록과 오늘의 성과를 비교할 수 있어요.</p>
        <p>오늘과 과거의 총 지출과 달성한 지출 루틴을 확인해보세요.</p>
        <br />
      </div>
      <img
        src="@/assets/images/homepage/versus1.png"
        alt="Past Habits Comparison"
      />
    </section>
    <br /><br />

    <section class="habit-sharing" ref="habitSharing">
      <img
        src="@/assets/images/homepage/roucommunity1.png"
        alt="Sharing Community"
      />
      <div class="habit4">
        <h4><strong>🐝 벌루틴 공유 커뮤니티</strong></h4>
        <br />
        <p>다양한 루틴을 공유하고 다른 사람들의 지출 루틴을 확인하세요.</p>
        <p>커뮤니티에서 서로의 경험을 나누며 동기를 얻을 수 있어요.</p>
        <p>사용자 간의 경험을 공유하여 더 나은 절약 루틴을 형성할 수 있으며,</p>
        <p>가져온 루틴은 My Routine 설정에 대기 상태로 담겨요.</p>
        <br />
      </div>
    </section>
    <br /><br />

    <section class="community-certification" ref="communityCertification">
      <div class="habit5">
        <h4><strong>🐝 허니샷 인증 커뮤니티</strong></h4>
        <br />
        <p>
          루틴을 인증한 모든 사람들의 인증샷을 피드에서 한 눈에 확인해 보세요.
        </p>
        <p>
          '꿀 리워드' 제도를 통해 보상을 받고, 인증을 통한 참여 유도와 자산
          관리에 대한 동기 부여를 얻을 수 있어요.
        </p>
        <p>
          사용자가 인증을 완료한 날짜의 달력 칸에 인증한 사진이 삽입되어 각
          날짜별로 인증 여부를 직관적으로 확인할 수 있어요.
        </p>
        <br />
      </div>
      <img
        src="@/assets/images/homepage/postcomu1.png"
        alt="Certification Community"
      />
    </section>
    <br /><br />

    <section class="intuitive-ui" ref="intuitiveUi">
      <div class="habit6">
        <h4><strong>🐝 벌루틴과 함께하세요! 🐝</strong></h4>
        <br />
        <router-link to="/login"
          ><button class="auth-button">벌루틴 시작하기</button></router-link
        >
      </div>
    </section>
    <br /><br /><br /><br />
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
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
  background-color: #ffecb3;
  border-radius: 10px;
}

.habit-add,
.habit-sharing {
  display: flex;
  justify-content: space-between;
  text-align: center;
  align-items: center;
  padding: 20px;
  line-height: 1.5;
  margin-top: 50px;
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
  background-color: #ffecb3;
  border-radius: 10px;
}

.intuitive-ui {
  justify-content: space-between;
  text-align: center;
  align-items: center;
  padding: 20px;
  line-height: 1.5;
  margin-top: 50px;
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
  box-shadow: 04px8px rgba(0, 0, 0, 0.1);
}

.habit-add img,
.habit-sharing img,
.intuitive-ui img {
  margin-left: 20px;
  width: 300px;
  border-radius: 15px;
  box-shadow: 04px8px rgba(0, 0, 0, 0.1);
}

.fade-in {
  opacity: 1;
  transform: translateY(0);
}

p {
  margin-top: 0.625rem;
  margin-bottom: 0.625rem;
  color: #3e2723;
}

h3,
h4 {
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
  background-color: #ffca28;
  border: none;
  color: #3e2723;
  font-size: 16px;
  padding: 10px20px;
  border-radius: 30px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.auth-button:hover {
  background-color: #f57f17;
  transform: scale(1.05);
}

.auth-button:active {
  background-color: #ffca28;
  transform: scale(1);
}
</style>
