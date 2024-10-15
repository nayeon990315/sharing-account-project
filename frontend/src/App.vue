<script setup>
import { useRoute } from 'vue-router';
import { computed, ref, onMounted } from 'vue';
import TodoList from './components/global/TodoList.vue';
import Header from './components/global/Header.vue';

const route = useRoute();
const routeKey = computed(() => route.fullPath);
const isTodoListVisible = ref(false);
const isHomePagewhenLogin = computed(() => route.name === 'HomePagewhenLogin');
const userId = ref(null); // userId를 저장할 ref

// To-Do List를 토글하는 함수
const toggleTodoList = () => {
  isTodoListVisible.value = !isTodoListVisible.value;
};

// 컴포넌트가 마운트될 때 localStorage에서 userId 확인
onMounted(() => {
  userId.value = localStorage.getItem('userId'); // localStorage에서 userId 가져오기
});

</script>

<template>
  <!-- 헤더는 로그인 페이지가 아닐 때만 표시 -->
  <Header v-if="!isHomePagewhenLogin"/>
  
  <!-- 페이지의 라우터 뷰 -->
  <router-view :key="routeKey"></router-view>
  
  <!-- To-Do List는 userId가 있을 때만 보이도록 처리 -->
  <router-view v-if="userId">
    <!-- To-Do 아이콘 -->
    <button class="btn btn-primary floating-button" @click="toggleTodoList">
      Check<br />List
    </button>
    <!-- To-Do List 컴포넌트 -->
    <TodoList :show="isTodoListVisible" @close="isTodoListVisible = false" />
  </router-view>
</template>

<style scoped>
* {
  box-sizing: border-box;
}

section {
  border: 1px solid;
}

section {
  height: 500px;
}

article {
  width: 75%;
  background-color: azure;
  margin: 10px;
  height: 90%;
  float: left;
  overflow: auto;
}

aside {
  width: 20%;
  background-color: #f4f4f1;
  height: 90%;
  float: right;
}

.floating-button {
  width: 100px;
  height: 100px;
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  text-transform: uppercase;
  border-radius: 50%;
  background-color: black;
  color: white;
  border: none;
}
</style>
