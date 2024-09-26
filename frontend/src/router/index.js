import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/pages/HomePage.vue';
import Explore from '@/pages/Explore.vue';
import MyShots from '@/pages/MyShots.vue';
import Upload from '@/pages/Upload.vue';
import PostCommunity from '@/pages/PostCommunity.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
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
