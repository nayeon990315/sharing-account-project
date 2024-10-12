<!-- TodoList.vue -->
<template>
    <div v-if="show" class="todo-list">

        <div class="todo-header">
        <h5>오늘의 할 일</h5>
        <button @click="closeTodoList" class="close-btn">&times;</button>
    </div>
    <ul>
        <li v-for="(habit, index) in activeHabits" :key="habit.myHabitId">
        <span :class="{ checked: habit.isCheckedToday }">{{ habit.habitTitle }}</span>
        <button
            v-if="!habit.isCheckedToday"
            @click="goToUpload(habit)"
            class="btn btn-primary"
        >
            인증하러가기
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
background-color: white;
box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
padding: 20px;
border-radius: 10px;
z-index: 1001;
width: 25%;
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
</style>
