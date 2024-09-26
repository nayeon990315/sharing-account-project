<template>
    <div class="card-container">
  <div class="card">Card 1</div>
  <div class="card">Card 2</div>
  <div class="card">Card 3</div>
  <div class="card">Card 4</div>
  <div class="card">Card 5</div>
</div>

</template>


<style scoped>
body {
  perspective: 1000px; /* 3D 효과를 더 극적으로 보이게 하기 위해 perspective 설정 */
}

.card-container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 400px;
  height: 300px;
  transform-style: preserve-3d; /* 3D 공간 유지 */
}

.card {
  position: absolute;
  width: 100%;
  height: 100%;
  background: lightgray;
  border: 1px solid #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  color: white;
  font-weight: bold;
  backface-visibility: hidden; /* 뒤쪽을 보이지 않게 설정 */
}

.card:nth-child(1) {
  transform: translateZ(-200px) rotateY(-15deg);
  background: #ff6b6b;
}

.card:nth-child(2) {
  transform: translateZ(-150px) rotateY(-10deg);
  background: #feca57;
}

.card:nth-child(3) {
  transform: translateZ(-100px) rotateY(-5deg);
  background: #48dbfb;
}

.card:nth-child(4) {
  transform: translateZ(-50px) rotateY(0deg);
  background: #1dd1a1;
}

.card:nth-child(5) {
  transform: translateZ(0px) rotateY(5deg);
  background: #ff9ff3;
}


@keyframes rotateCards {
  0% { transform: rotateY(0deg); }
  100% { transform: rotateY(360deg); }
}

.card-container {
  animation: rotateCards 5s infinite linear;
}

</style>