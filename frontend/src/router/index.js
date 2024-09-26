import { createRouter, createWebHistory } from 'vue-router';
import MeVersusPage from '@/pages/Versus/MeVersusPage.vue';
import MyRoutine from '@/pages/MyRoutine.vue';


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/home',
      name: 'MyRoutine',
      component : MyRoutine
    },

    {
      path: '/versus',
      name: 'MeVersus',
      component : MeVersusPage
    }
  ],
});

export default router;
