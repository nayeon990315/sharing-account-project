<!-- TodoList.vue -->
<template>
    <div v-if="show" class="todo-list">

        <div class="todo-header">
        <h5>오늘의 달성 목록 {{activeCheckedHabits.length }}/{{activeHabits.length}}</h5>
        <button @click="closeTodoList" class="close-btn">&times;</button>
    </div>
    
    <ul class="list-group">
        <li class="list-group-item d-flex justify-content-between align-items-center" v-for="(habit, index) in activeHabits" :key="habit.myHabitId">
            <span class="habit-title" :class="{ checked: habit.isCheckedToday }">
                {{ habit.habitTitle }}
            </span>
            <button
                @click="goToUpload(habit)"
                class="btn"
                :class="{
                    'disabled': habit.isCheckedToday,
                    'btn-outline-secondary': habit.isCheckedToday,
                    'btn-dark': !habit.isCheckedToday
                
                }"
            >
                {{ habit.isCheckedToday ? '인증완료' : '인증하기' }}
            </button>
        </li>
    </ul>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useHabitStore } from '@/stores/habitStore';
const props = defineProps({
show: Boolean,
});
const emit = defineEmits(['close']);

// 라우터 사용
const router = useRouter();

// 습관 스토어 가져오기
const habitStore = useHabitStore();

// 진행 중인 습관만 필터링
const activeHabits = computed(() => {
    return habitStore.habits.filter(habit => habit.state === '진행');
});

const activeCheckedHabits = computed(() => {
    return activeHabits.value.filter(habit => habit.isCheckedToday)
});

// const findSelectedHabit = computed(() => {
//     return filteredHabits.filter(habit => habit.myHabitId === formData.myHabitId);
// });

// 모달 닫는 함수
const closeTodoList = () => {
emit('close');
}; 

// Upload.vue로 이동
const goToUpload = (habit) => {
habitStore.selectedMyHabitId = habit.myHabitId
console.log("selected", habit.myHabitId)

router.push({
    name: 'upload',
    state: { selectedMyHabitId: habit.myHabitId },
})

emit('close')
};
</script>

<style scoped>
.todo-list {
position: fixed;
bottom: 80px;
right: 60px;
background-color: rgba(255, 255, 255, 0.9);
box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
padding: 20px;
border-radius: 10px;
z-index: 1001;
width: 30%;
height: 50%;
min-width: 450px;
}

.todo-header {
display: flex;
justify-content: space-between;
align-items: center;
}

.checked {
text-decoration: line-through;
color: gray;
}

.close-btn {
background: none;
border: none;
font-size: 1.5rem;
cursor: pointer;
}

.habit-title {
    flex-grow: 1;
    margin-right: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: block; /* 블록 요소로 설정 */
    white-space: normal; /* 줄바꿈 허용 */
    word-wrap: break-word; /* 단어가 너무 길 경우 줄바꿈 */

    overflow: hidden;
    line-height: 1.2; /* 한 줄의 높이 */
    max-height: calc(1.2em * 2); /* 최대 3줄까지만 보이도록 */
    text-overflow: ellipsis;
}

.list-group-item {
display: flex;
align-items: center; /* 리스트 아이템 높이가 달라져도 가운데 정렬 */
padding: 10px; /* 패딩을 추가하여 아이템 간 간격 조정 */
}

button.btn {
flex-shrink: 0; /* 버튼이 줄어들지 않도록 고정 */
width: auto; /* 버튼이 고정된 크기를 가지도록 설정 */
white-space: nowrap; /* 버튼 안의 텍스트가 줄바꿈되지 않도록 설정 */
}

</style>
