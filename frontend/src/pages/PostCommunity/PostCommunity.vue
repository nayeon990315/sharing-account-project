<template>
  <div id="post-community">
    <div class="content">
      <aside>
        <router-link to="/post-community/explore" class="nav-item"
          >🧭 피드 탐색</router-link
        >
        <router-link to="/post-community/my-shots" class="nav-item"
          >📸 인증샷</router-link
        >
        <router-link to="/post-community/upload" class="nav-item"
          >📤 업로드</router-link
        >
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
  background-color: #e2e2e285;
}

.content {
  display: flex;
  min-height: 100vh;
}

aside {
  width: 200px;
  background-color: #ffe270;
  padding: 20px;
  border-radius: 0; /* 둥근 모서리를 제거하고 각진 모서리로 변경 */
  box-shadow: 2px 0px 5px rgba(0, 0, 0, 0.1);
}

.nav-item {
  display: block;
  margin-bottom: 15px;
  padding: 10px;
  color: #2d2d2d;
  text-decoration: none;
  background-color: #ffffff;
  border-radius: 0; /* 둥근 모서리를 제거하고 각진 모서리로 변경 */
  text-align: center;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s, color 0.3s, box-shadow 0.3s;
}

.nav-item:hover {
  color: #f8c952;
  background-color: #e9ecef;
  box-shadow: none;
}

main {
  flex-grow: 1;
}
</style>
