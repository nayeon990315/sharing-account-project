import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../pages/HomePage.vue';
import HomePage2 from '../pages/HomePage2.vue';
import RoutineCommunity from '../pages/RoutineCommunity.vue';
import Card from '../pages/card.vue';
import Card2 from '../pages/card2.vue';


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
    {
        path: '/routine_community',
        name: 'routineCommunity',
        component: RoutineCommunity 
    },
    {
      path: '/test',
      name: 'card',
      component: Card 
    },
    {
      path: '/test2',
      name: 'card2',
      component: Card2
  }
  ],
});

export default router;
