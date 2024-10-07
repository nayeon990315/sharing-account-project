<template>
  <div class="container">
    <h1>Daily Report</h1>

    <div class="report-section">
      <h2>오늘의 소비 내역 리포트</h2>
      <p>루틴 형성 정도를 한눈에 볼 수 있습니다.</p>
      <div class="flex-container">
        <div class="report-graph">리포트 그래프</div>
        <ul class="category-list">
          <li>식비</li>
          <li>카페/간식</li>
          <li>쇼핑</li>
          <li>교통</li>
          <li>문화/여가</li>
        </ul>
      </div>
    </div>


    <div class="report-section">
      <h2>오늘의 루틴 & 절약 리포트</h2>
      <p>루틴 형성 정도를 한눈에 볼 수 있습니다.</p>
      <div class="flex-container">
        <div class="report-graph">리포트 그래프</div>
        <!-- 추가적인 내용 -->
      </div>
    </div>

    <h2>Details</h2>
    <div class="flex-container">
      <ul class="routine-list">
        <li><input type="checkbox" /> 루틴 1</li>
        <li><input type="checkbox" /> 루틴 2</li>
        <li><input type="checkbox" /> 루틴 3</li>
        <!-- 추가 항목 -->
      </ul>
      <div class="details-list">관련 지출내역 리스트</div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            routinesArray: [
                {
                    "routine_name": "아침 식사 준비",
                    "category": "식비"
                },
                {
                    "routine_name": "저녁 외식",
                    "category": "식비"
                },
                {
                    "routine_name": "아침 커피 한 잔",
                    "category": "카페/간식"
                },
                {
                    "routine_name": "오후 간식 타임",
                    "category": "카페/간식"
                },
                {
                    "routine_name": "월간 필수 아이템 구매",
                    "category": "온라인쇼핑"
                },
                {
                    "routine_name": "할인 쿠폰으로 쇼핑하기",
                    "category": "온라인쇼핑"
                },
                {
                    "routine_name": "화장품 꼭 필요한 것만 사기",
                    "category": "뷰티"
                }
            ],
            expenses: [
                {
                    transactionDate: '2024-06-26T18:17',
                    type: '체크카드',
                    category: '식비',
                    recipient: '사나이뚝배기화곡역',
                    withdrawalAmount: 7800,
                    depositAmount: 0,
                    balance: 691,
                },
                {
                    transactionDate: '2024-06-26T14:39',
                    type: '체크카드',
                    category: '카페/간식',
                    recipient: '애크로매틱어린이대',
                    withdrawalAmount: 3500,
                    depositAmount: 0,
                    balance: 8491,
                },
                {
                    transactionDate: '2024-06-26T13:10',
                    type: '체크카드',
                    category: '카페/간식',
                    recipient: '매머드커피앱_발트_1',
                    withdrawalAmount: 2000,
                    depositAmount: 0,
                    balance: 11991,
                },
                {
                    transactionDate: '2024-06-26T12:50',
                    type: '체크카드',
                    category: '식비',
                    recipient: '홈플러스익스프레스',
                    withdrawalAmount: 2590,
                    depositAmount: 0,
                    balance: 13991,
                },
                {
                    transactionDate: '2024-06-26T12:00',
                    type: '체크카드',
                    category: '식비',
                    recipient: '맘스터치화곡점',
                    withdrawalAmount: 9800,
                    depositAmount: 0,
                    balance: 16581,
                },
                {
                    transactionDate: '2024-06-26T09:11',
                    type: '체크카드',
                    category: '카페/간식',
                    recipient: '메가엠지씨커피우장',
                    withdrawalAmount: 2000,
                    depositAmount: 0,
                    balance: 26381,
                },
                {
                    transactionDate: '2024-06-26T09:31',
                    type: '체크카드',
                    category: '뷰티',
                    recipient: '블랭크유',
                    withdrawalAmount: 74000,
                    depositAmount: 0,
                    balance: 28381,
                }
            ],
            selectedCategory: null
        };
    },
    mounted() {
        this.getUserIdFromToken();
    },

    computed: {
        // 카테고리별로 그룹화된 배열 생성
        routineCategories() {
            return [...new Set(this.routinesArray.map(routine => routine.category))];
        },
        // 선택된 카테고리에 따른 지출 내역 필터링 및 날짜순 정렬
        filteredExpenses() {
            if (!this.selectedCategory) return [];
            return this.expenses
                .filter(expense => expense.category === this.selectedCategory)
                .sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate)); // 날짜 내림차순으로 정렬
        },
        // 오늘의 지출 총액을 계산
        totalExpenses() {
            return this.expenses.reduce((total, expense) => total + expense.withdrawalAmount, 0);
        },
        // 카테고리별 지출총액 합
        expensesByCategory() {
            return this.expenses.reduce((acc, expense) => {
                if (!acc[expense.category]) {
                    acc[expense.category] = 0;
                }
                acc[expense.category] += expense.withdrawalAmount;
                return acc;
            }, {});
        },
        // 오늘의 최다 지출에 대한 카테고리, 총지출, 확률을 계산
        maxExpenseCategory() {
            const maxCategory = Object.keys(this.expensesByCategory).reduce((max, category) => {
                return this.expensesByCategory[category] > this.expensesByCategory[max] ? category : max;
            }, Object.keys(this.expensesByCategory)[0]);

            return {
                maxCategory,
                maxAmount: this.expensesByCategory[maxCategory],
                maxPercentage: this.expensesByCategory[maxCategory] / this.totalExpenses * 100
            };
        },
        // 선택한 카테고리의 총지출, 확률을 계산
        selectedCategoryExpenses() {
            const amount = this.expensesByCategory[this.selectedCategory] || 0;
            const percentage = (amount / this.totalExpenses) * 100;
            return { amount, percentage };
        }
    },

    methods: {
        // 카테고리별로 루틴 필터링
        filteredRoutines(category) {
            return this.routinesArray.filter(routine => routine.category === category);
        },
        // 카테고리 선택
        selectCategory(category) {
            this.selectedCategory = category;
        },
        // Spring의 Date타입(Timestamp)를 HH:mm로 변환
        formatTime(date) {
            const d = new Date(date);
            return d.toLocaleTimeString('ko-KR', {
                hour: '2-digit',
                minute: '2-digit',
            });
        },
        // 숫자를 한국식 원화로 변환
        formatCurrency(value) {
            return new Intl.NumberFormat('ko-KR').format(value);
        },
        // 확률을 소수점 1번째 자리까지 표시
        formatPercentage(value) {
            return value.toFixed(1) + '%';
        },
        async getUserIdFromToken() {
            const jwtToken = this.$cookies.get('jwtToken');
            if (!jwtToken) {
                alert('로그인이 필요합니다!');
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
                    alert('인증 오류: 로그인이 필요합니다.');
                    this.$router.push('/login');
                }
            }
        }
    }
};
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
}
.report-section {
  margin-bottom: 40px;
}
.flex-container {
  display: flex;
  justify-content: space-between;
}
.report-graph,
.details-list {
  width: 48%;
  height: 200px;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.category-list,
.routine-list {
  width: 48%;
  list-style-type: none; /* 리스트 아이콘 제거 */
}
.details-list {
  background-color: #f9f9f9;
}
</style>
