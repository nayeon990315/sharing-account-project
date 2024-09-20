import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../pages/HomePage.vue';
import HomePage2 from '../pages/HomePage2.vue';


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
        path: '/home2',
        name: 'home2', 
        component: HomePage2 },
  ],
});

export default router;
