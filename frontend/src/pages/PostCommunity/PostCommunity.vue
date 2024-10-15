<template>
  <div id="post-community">
    <div class="content">
      <aside>
        <router-link to="/post-community/explore" class="nav-item">
          <img src="@/assets/icons/myshot.png" alt="피드 탐색" class="icon" />
          피드 탐색
        </router-link>
        <router-link to="/post-community/my-shots" class="nav-item">
          <img src="@/assets/icons/explore.png" alt="내 인증샷" class="icon" />
          내 인증샷
        </router-link>
        <router-link to="/post-community/upload" class="nav-item">
          <img src="@/assets/icons/upload.png" alt="업로드" class="icon" />
          업로드
        </router-link>
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
}

aside {
  width: 200px; /* 고정 너비 */
  min-width: 200px; /* 최소 너비 설정 */
  background-color: #ffe26c;
  padding: 20px;
  border-radius: 0px;
  box-shadow: 2px 0px 5px rgba(0, 0, 0, 0.1);
}

.nav-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  color: #2d2d2d;
  text-decoration: none;
  background-color: #ffffff;
  border-radius: 0px;
  text-align: left;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s, color 0.3s, box-shadow 0.3s;
}

.nav-item:hover {
  color: #f8c952;
  background-color: #e9ecef;
  box-shadow: none;
}

.icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

main {
  flex-grow: 1;
  min-width: 0; /* 추가: 최소 너비 설정 */
}
</style>
