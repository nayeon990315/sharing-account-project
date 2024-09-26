<template>
  <div id="post-community">
    <header>
      <div class="logo">LOGO</div>
      <input type="text" placeholder="#Search" />
    </header>
    <div class="content">
      <aside>
        <router-link to="post-community/explore">Explore</router-link>
        <router-link to="/post-community/my-shots">My Shots</router-link>
        <router-link to="/post-community/upload">Upload</router-link>
      </aside>
      <main>
        <!-- Upload 페이지가 아닐 때만 해시태그 표시 -->
        <div v-if="$route.path !== '/post-community/upload'" class="hashtags">
          <button>#식비</button>
          <button>#카페/간식</button>
          <button>#쇼핑</button>
          <button>#교통</button>
          <button>#전체</button>
          <button>#문화/여가</button>
          <button>#술/유흥</button>
          <button>#주거/공과금</button>
          <button>#기타</button>
        </div>
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PostCommunity',
};
</script>

<style>
body,
html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  font-family: Avenir, Helvetica, Arial, sans-serif;
}

#post-community {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #f0f4f8;
}

.logo {
  font-size: 24px;
  font-weight: bold;
}

.content {
  display: flex;
  flex-grow: 1;
}

aside {
  width: 200px;
  background-color: #e0e4e8;
  padding: 20px;
}

aside a {
  display: block;
  margin-bottom: 10px;
  text-decoration: none;
  color: #2c3e50;
}

aside a.router-link-active {
  color: #42b983;
}

main {
  flex-grow: 1;
}

.hashtags {
  margin: 20px;
}

.hashtags button {
  margin: 5px;
  padding: 10px;
  border-radius: 20px;
  border: none;
  background-color: #e0e4e8;
  color: black;
}

.hashtags button:hover {
  background-color: #42b983;
}
</style>
