<template>
  <div class="feed-container">
    <div class="photo-card" v-for="(photo, index) in photos" :key="index">
      <div class="user-info">
        <span>{{ photo.username }}</span>
        <span>{{ photo.level }}</span>
      </div>
      <img :src="photo.src" :alt="photo.alt" class="photo-image" />
      <div class="photo-caption">{{ photo.caption }}</div>
      <div class="comments">
        <p v-for="(comment, index) in photo.comments" :key="index">
          {{ comment }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      photos: [
        {
          username: 'Money.java',
          src: require('@/assets/POST PNG (1).png'),
          alt: 'Coffee',
          caption: '출근 전 모닝커피! 스타벅스 대신 저렴한 커피로 마셔봐요.',
          comments: [
            '메가커피도 꽤 맛있더라구요. 파이팅입니다.',
            '저희 회사 바로 앞에는 스벅밖에 없어서 따로 사가기가 귀찮네요.',
            '저는 그래서 카페서 안틀고 매일 텀블러에 담아가요 ~',
          ],
        },
        // 추가 사진 데이터
      ],
    };
  },
};
</script>

<style scoped>
.feed-container {
  max-width: 600px;
  margin: auto;
}

.photo-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  justify-content: space-between;
  padding: 10px;
}

.photo-image {
  width: 100%;
}

.photo-caption {
  padding: 10px;
}

.comments {
  padding: 10px;
}
</style>
