import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/HomePage.vue';
import Explore from '@/pages/PostCommunity/Explore.vue';
import MyShots from '@/pages/PostCommunity/MyShots.vue';
import Upload from '@/pages/PostCommunity/Upload.vue';
import PostCommunity from '@/pages/PostCommunity/PostCommunity.vue';
import MeVersusPage from '@/pages/Versus/MeVersusPage.vue';
import MyRoutine from '@/pages/MyRoutine/MyRoutine.vue';
import RoutineCommunity from '../pages/RoutineCommunity/RoutineCommunity.vue';
import MyRoutineEdit from '@/pages/MyRoutine/MyRoutineEdit.vue';
import Login from '../pages/Login&SignUp/Login.vue'
import SignUp from '@/pages/Login&SignUp/SignUp.vue';
import MyPageEdit from '@/pages/Mypage/MyPageEdit.vue';
import Register from '@/pages/Login&SignUp/Register.vue';
import MyPageReward from '@/pages/Mypage/MyPageReward.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/signup',
    name: 'signup',
    component: SignUp
  },
  {
    path: '/register',
    name: 'register',
    component: Register
  },
  {
    path: '/myroutine',
    name: 'MyRoutine',
    component: MyRoutine,
  },
  {
    path: '/myroutine/edit',
    name: 'MyRoutineEdit',
    component: MyRoutineEdit
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
      {
        path: '/post-community/upload',
        name: 'upload',
        component: Upload,
      },
    ],
  },
  {
    path: '/myedit',
    name: 'myedit',
    component: MyPageReward,
  },
  {
    path: '/myreward',
    name: 'myreward',
    component: MyPageReward,
  },
]


const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
