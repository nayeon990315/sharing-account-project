import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/pages/HomePage.vue';
import Explore from '@/pages/Explore.vue';
import MyShots from '@/pages/MyShots.vue';
import Upload from '@/pages/Upload.vue';
import PostCommunity from '@/pages/PostCommunity.vue';
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
