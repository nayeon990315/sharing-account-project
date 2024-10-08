// import './assets/main.css'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import VueCookies from 'vue-cookies';


const app = createApp(App)
const pinia = createPinia();
app.use(router)
app.use(VueCookies);
app.use(pinia);
app.mount('#app')