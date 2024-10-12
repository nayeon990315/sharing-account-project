<template>
  <div id="post-community">
    <div class="content">
      <aside>
        <router-link to="/post-community/explore">Explore</router-link>
        <router-link to="/post-community/my-shots">My Shots</router-link>
        <router-link to="/post-community/upload">Upload</router-link>
      </aside>
      <main>
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

<style scoped>
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
  background-color: #F3F3F3;
}

.content {
  display: flex;
}

aside {
  width: 200px;
  background-color: #f0f4f8; /* 배경 색상 */
  padding: 20px;
  box-shadow: 2px 0px 5px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
}

aside a {
  display: block;
  margin-bottom: 10px;
  color: #333; /* 링크 색상 */
  text-decoration: none; /* 밑줄 제거 */
}

aside a:hover {
  color: #007bff; /* 호버 시 링크 색상 */
}

main {
  flex-grow: 1;
}
</style>
