// stores/auth.js
import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isLoggedIn: false,
    userName: '',
  }),
  actions: {
    login(userName) {
      this.isLoggedIn = true;
      this.userName = userName;
    },
    logout() {
      this.isLoggedIn = false;
      this.userName = '';
      localStorage.removeItem('nickname');
      localStorage.removeItem('userId');
    },
    checkLoginStatus() {
      const storedNickName = localStorage.getItem('nickname');
      if (storedNickName) {
        this.isLoggedIn = true;
        this.userName = storedNickName;
      } else {
        this.isLoggedIn = false;
        this.userName = '';
      }
    },
  },
});
