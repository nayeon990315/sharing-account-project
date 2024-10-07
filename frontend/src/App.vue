<script setup>
// import Header from './components/Header.vue'
// import Footer from './components/Footer.vue'
import { useRoute } from 'vue-router';
import { computed } from 'vue';
const route = useRoute();
const routeKey = computed(() => route.fullPath);
// const headerKey = computed(() => route.fullPath);

import { ref } from 'vue';
import TodoList from './components/global/TodoList.vue';
import Header from './components/global/Header.vue';

const isTodoListVisible = ref(false);

// To-Do List를 토글하는 함수
const toggleTodoList = () => {
  isTodoListVisible.value = !isTodoListVisible.value;
};
</script>

<template>
  <!-- <Header :key="headerKey" /> -->
  <!-- <Header :key="headerKey" /> -->
  <Header />
  <router-view :key="routeKey"></router-view>
  <router-view>
    <!-- To-Do 아이콘 -->
    <button class="btn btn-primary floating-button" @click="toggleTodoList">
      Check<br />List
      <!-- <i class="bi bi-check2"></i>  -->
    </button>

    <!-- To-Do List 컴포넌트 -->
    <TodoList :show="isTodoListVisible" @close="toggleTodoList" />
  </router-view>

  <!-- <Footer /> -->
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
  /* display: inline-block; */
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
