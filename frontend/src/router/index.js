import { createRouter, createWebHistory } from 'vue-router';
import MeVersusPage from '@/pages/Versus/MeVersusPage.vue';
import MyRoutine from '@/pages/MyRoutine.vue';


const routes = [
  {
    path: '/home1',
    name: 'MyRoutine',
    component : MyRoutine
  },
  {
    path: '/versus',
    name: 'MeVersus',
    component : MeVersusPage
  }
]

const router = createRouter({
  history:createWebHistory,
  routes
});


export default router;
