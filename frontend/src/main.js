// import './assets/main.css'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

import { createApp } from 'vue'
import App from './App.vue'
// createApp(App).mount('#app')

import router from './router/index.js'
const app = createApp(App)
app.use(router) //vue인스턴스에 router등록
app.mount('#app')