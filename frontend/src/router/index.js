import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/HomePage.vue';
import Explore from '@/pages/PostCommunity/Explore.vue';
import MyShots from '@/pages/PostCommunity/MyShots.vue';
import Upload from '@/pages/PostCommunity/Upload.vue';
import PostCommunity from '@/pages/PostCommunity/PostCommunity.vue';
import MeVersusPage from '@/pages/Versus/MeVersusPage.vue';
import MyRoutine from '@/pages/MyRoutine/MyRoutine.vue';
import RoutineCommunity from '../pages/RoutineCommunity/RoutineCommunity.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
  },
  {
    path: '/myroutine',
    name: 'MyRoutine',
    component: MyRoutine,
  },
  {
    path: '/versus',
    name: 'MeVersus',
    component: MeVersusPage,
  },
  {
    path: '/routine-community',
    name: 'routineCommunity',
    component: RoutineCommunity,
  },
  {
    path: '/post-community',
    name: 'PostCommunity',
    component: PostCommunity,
    children: [
      {
        path: 'explore',
        name: 'explore',
        component: Explore,
      },
      {
        path: 'my-shots',
        name: 'myShots',
        component: MyShots,
      },
      {
        path: 'upload',
        name: 'upload',
        component: Upload,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
