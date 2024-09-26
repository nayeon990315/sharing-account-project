<template>
  <header class="header">
    <nav>
      <div class="logo">LOGO</div>
      <ul class="nav-links">
        <li><a href="#">Explore</a></li>
        <li><a href="#">My Shots</a></li>
        <li><a href="#">Upload</a></li>
      </ul>
      <div class="search-bar">
        <input type="text" placeholder="#Search" />
      </div>
    </nav>
  </header>
</template>

<style scoped>
.header {
  background-color: #dfe6e9;
  padding: 10px;
}

nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 24px;
}

.nav-links {
  list-style-type: none;
}

.nav-links li {
  display: inline;
  margin-right: 15px;
}

.search-bar input {
  padding: 5px;
}
</style>
