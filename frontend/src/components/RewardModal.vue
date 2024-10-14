<template>
    <div class="modal" v-if="isVisible">
        <div ref="modalContent" class="modal-content">
            <h4 id="modal-title"><strong>{{ title }}</strong></h4>
            <p id="modal-content">{{ message }}</p>
            <p id="reward-content">총 {{ tweenedReward.number.toFixed(0) }} 꿀</p>
            <button @click="closeModal">확인</button>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, watch, nextTick } from 'vue';
import gsap from 'gsap';

// Props 정의
const props = defineProps({
    isVisible: {
        type: Boolean,
        default: false,
    },
    title: {
        type: String,
    },
    message: {
        type: String,
    },
    rewardBefore: {
        type: Number,
        default: 0,
    },
    rewardAfter: {
        type: Number,
        default: 0,
    }
});

// Emits 정의
const emit = defineEmits(['close']);

// 애니메이션을 적용할 숫자 상태 정의
const tweenedReward = reactive({ number: props.rewardBefore });

// 모달 콘텐츠를 참조하기 위한 ref
const modalContent = ref(null);

// reward 값이 변할 때마다 애니메이션 적용
watch(
    () => [props.rewardBefore, props.rewardAfter],
    ([newRewardBefore, newRewardAfter]) => {
        console.log("Initial Reward Before:", newRewardBefore); // 로그로 초기 값 확인

        // rewardBefore로 다시 초기화 후 gsap 애니메이션 적용
        tweenedReward.number = newRewardBefore;
        gsap.to(tweenedReward, { duration: 2.3, number: newRewardAfter });
    },
    { immediate: true }
);

// 모달 열릴 때 애니메이션 적용
watch(() => props.isVisible, async (newVal) => {
    if (newVal) {
        // nextTick을 사용하여 DOM이 렌더링된 후에 애니메이션 적용
        await nextTick();
        gsap.fromTo(modalContent.value, 
            { scale: 0.8, opacity: 0 }, 
            { scale: 1, opacity: 1, duration: 0.5, ease: 'power2.out' }
        );
    }
});

// 모달 닫기 함수
const closeModal = () => {
    gsap.to(modalContent.value, {
        scale: 0.8,
        opacity: 0,
        duration: 0.3,
        ease: 'power2.in',
        onComplete: () => {
            emit('close');
        }
    });
};
</script>

<style scoped>
.modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 2000;
    background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 500px;
    height: 300px;
    text-align: center;
    align-items: center;
    background-image: url('@/assets/images/background/background.png');
    background-size: cover;
    background-position: center;
    transform: scale(0.8);
    opacity: 0;
}

#reward-content {
    margin-top: 10px;
    font-size: 2rem;
}

#modal-title {
    text-align: center;
    margin-top: 10px;
}

#modal-content {
    margin-top: 10px;
    font-size: 1.2rem;
}

button {
    margin-top: 10px;
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    border: 3px solid #f7bb14;
    background-color: #f7bb14;
    color: white;
}

button:hover {
    background-color: white;
    color: #c29519;
}
</style>
