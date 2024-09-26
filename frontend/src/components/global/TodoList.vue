<!-- TodoList.vue -->
<template>
    <div v-if="show" class="todo-list">
        <div class="todo-header">
            <h5 class="">오늘의 할 일</h5>
            <button @click="closeTodoList" class="close-btn">&times;</button>
        </div>
        <ul>
            <li v-for="(task, index) in tasks" :key="index">
                <input type="checkbox" v-model="task.done" /> {{ task.title }}
            </li>
        </ul>
    </div>
</template>

<script setup>
import { ref } from 'vue';
// import { defineProps, defineEmits } from 'vue';

// props와 emit을 정의합니다.
const props = defineProps({
    show: Boolean
});
const emit = defineEmits(['close']);

// 할 일 목록을 ref로 정의
const tasks = ref([
    { title: '아침 식사 준비', done: false },
    { title: '저녁 외식', done: false },
    { title: '아침 커피 한 잔', done: false },
    { title: '오후 간식 타임', done: false },
    { title: '월간 필수 아이템 구매', done: false },
    { title: '할인 쿠폰으로 쇼핑', done: false },
    { title: '화장품 꼭 필요한 것만 사기', done: false }
]);

// 할 일 목록을 닫는 함수
const closeTodoList = () => {
    emit('close');
};
</script>

<style>
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

.close-btn {
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    padding-bottom: 15px;
}
</style>