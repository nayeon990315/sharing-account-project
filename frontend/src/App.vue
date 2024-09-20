<script setup>
// import Header from './components/Header.vue'
// import Footer from './components/Footer.vue'
import { useRoute } from 'vue-router';
import { computed } from 'vue';
const route = useRoute();

const routeKey = computed(() => route.fullPath);
// const headerKey = computed(() => route.fullPath);

</script>

<template>
  <!-- <Header :key="headerKey" /> -->
  <router-view :key="routeKey"></router-view>
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
</style>
