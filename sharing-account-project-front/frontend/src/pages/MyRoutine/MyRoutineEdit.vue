<template>
    <div class="info">
        <h1 >My Routine Settings</h1>
        <h5>마이루틴 설정</h5>
        <p>원하는 루틴을 추가, 편집, 삭제하고, '벌루틴 공유 커뮤니티'에 공유해보세요. <br>또, 매일 실천할 루틴을 '활성화된 루틴'으로 드래그하세요. 이때 '완료한 루틴'으로 직접 이동시킬 수는 없어요.</p>
    </div>

    <div class="container mt-5">
        <div class="row">
            

            <!-- 대기 및 진행 중 리스트 -->

            <div class="col d-flex justify-content-between">
                    <div class="row">
                        <!-- 비활성화 리스트 -->
                        <div class="col-6 inactive-box" >
                            
                            <h4 class="d-flex align-items-center" >
                                <img class = "beeImg" src="@/assets/images/bee/bee_sleeping.png" alt="">
                                비활성화
                            </h4>
                            <p>준비 중인 벌루틴! 실행할 준비가 되면 활성화해 보세요.</p>

                            <h6 class="d-flex align-items-center mt-5 type">
                                <img src="@/assets/images/check/true.png" style="width: 12px; margin-right: 6px;">
                                비활성화된 루틴
                            </h6>
                            <draggable class="allInactive dragArea list-group" :list="waitingList" group="tasks" item-key="myHabitId"
                                @start="handleDragStart" @end="onDragEnd('inProgress')">
                                <template #item="{ element }">
                                    <div class="list-group-item d-flex justify-content-between align-items-center">
                                        <div class="d-flex align-items-center">
                                            <span class="category-badge" :class="getCategoryClass(element.categoryTitle)">
                                                {{ element.categoryTitle }}
                                            </span>
                                            <span class="task-name mx-3">{{ element.habitTitle }}</span>
                                        </div>
                                        <div class="d-flex bd-highlight">
                                            <button class="btn" @click="confirmAddCommunity(element)">
                                                <i class="fa fa-cloud-upload" aria-hidden="true"></i>
                                            </button>
                                            <button class="btn" @click="confirmEdit('waiting', element)">
                                                <img src="@/assets/icons/edit.png" alt="" class="icon" style="width: 14px">
                                            </button>
                                            <button class="btn" data-bs-toggle="modal" data-bs-target="#removeModal"
                                                @click="confirmRemove('waiting', element.myHabitId, element.habitId, element.habitTitle)">
                                                <img src="@/assets/icons/delete.png" alt="" class="icon" style="width: 14px">

                                            </button>
                                        </div>

                                    </div>
                                </template>
                            </draggable>
                        </div>

                        <!-- 활성화 리스트 -->
                        <div class="col-6 active-box">
                            <h4 class="d-flex align-items-center">
                                <img class = "beeImg" src="@/assets/images/bee/bee3.png" alt="" style="margin-top: -9px">
                                <!-- <i class="fa fa-circle px-2" style="font-size:16px; color:green;" aria-hidden="true"></i> -->
                                활성화
                            </h4>
                            <p>매일 실천할 벌루틴을 활성화된 루틴에 모아보세요.</p>

                            <h6 class="d-flex align-items-center mt-5 type">
                                <img src="@/assets/images/check/true.png" style="width: 12px; margin-right: 6px;">
                                활성화된 루틴
                            </h6>
                            <draggable class="allActive dragArea list-group" :list="inProgressList" group="tasks" item-key="myHabitId"
                                @change="checkListLength" @end="onDragEnd('waiting')">
                                <template #item="{ element }">
                                    <div class="list-group-item d-flex justify-content-between align-items-center">
                                        <div class="d-flex align-items-center">
                                            <span class="category-badge" :class="getCategoryClass(element.categoryTitle)">
                                                {{ element.categoryTitle }}
                                            </span>
                                            <span class="task-name mx-3">{{ element.habitTitle }}</span>
                                        </div>
                                        <div class="d-flex bd-highlight">
                                            <button class="btn" @click="confirmAddCommunity(element)">
                                                <i class="fa fa-cloud-upload" aria-hidden="true"></i>
                                            </button>
                                            <button class="btn" @click="confirmEdit('inProgress', element)">
                                                <img src="@/assets/icons/edit.png" alt=""  class="icon" style="width: 14px">
                                            </button>
                                            <button class="btn" data-bs-toggle="modal" data-bs-target="#removeModal"
                                                @click="confirmRemove('inProgress', element.myHabitId, element.habitId, element.habitTitle)">
                                                <img src="@/assets/icons/delete.png" alt=""  class="icon" style="width: 14px">

                                            </button>
                                        </div>
                                    </div>          
                                </template>
                            </draggable>

                            <!-- 완료된 리스트도 같은 박스 안에 배치 -->
                            <h6 class="allTodayChecked d-flex align-items-center mt-5 type">
                                <img src="@/assets/images/check/true.png" style="width: 12px; margin-right: 6px;">
                                오늘 완료한 루틴
                            </h6>
                            
                            <div class="dragArea noRoutine" v-if="completedList.length === 0" style="text-align: center;">
                                <p>오늘 완료한 루틴이 없습니다</p>
                            </div>

                            <draggable v-else class="dragArea list-group" :list="completedList" group="tasks" item-key="myHabitId" style="border-radius:0;">
                                <template #item="{ element }">
                                    <div class="list-group-item d-flex justify-content-between align-items-center">
                                        <div class="d-flex align-items-center">
                                            <span class="category-badge" :class="getCategoryClass(element.categoryTitle)">
                                                {{ element.categoryTitle }}
                                            </span>
                                            <span class="task-name mx-3">{{ element.habitTitle }}</span>
                                        </div>

                                    </div>
                                </template>
                            </draggable>
                        </div>
                    <!-- 완료된 리스트 아래로 배치 -->
                    <!-- <div class="row mt-5">
                        <div class="col-12">
                            <h4 class="d-flex align-items-center">
                                <i class="fa fa-circle px-2" style="font-size:16px; color:blue;" aria-hidden="true"></i>
                                오늘 완료한 루틴
                            </h4>
                            <draggable class="dragArea list-group" :list="completedList" group="tasks" item-key="myHabitId">
                                <template #item="{ element }">
                                    <div class="list-group-item d-flex justify-content-between align-items-center">
                                        <div class="d-flex align-items-center">
                                            <span class="category-badge" :class="getCategoryClass(element.categoryTitle)">
                                                {{ element.categoryTitle }}
                                            </span>
                                            <span class="task-name mx-3">{{ element.habitTitle }}</span>
                                        </div>
                                    </div>
                                </template>
                            </draggable>
                        </div>
                    </div> -->
                </div>
            </div>
        </div>

        <!-- 사이드바 버튼들 -->
    <div class="button-section col sidebar d-flex flex-column align-items-center justify-content-start">
                <div class="d-flex flex-column">
                    <button type="button" class="btn btn-dark mb-3" data-bs-toggle="modal" data-bs-target="#myModal">
                        루틴 추가
                    </button>
                    <button type="button" class="btn btn-dark mb-3" @click="updateRoutineState">
                        업데이트
                    </button>
                </div>
            </div>
    </div>

    


    <!-- 루틴 추가 Modal -->
    <div class="modal fade" id="myModal">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">루틴 추가하기</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    <label>🐝 카테고리를 선택하세요</label>
                    <select v-model="newHabitCategory" class="form-select" aria-label="Category select"
                        id="categorySelect">
                        <option disabled value="">카테고리</option>
                        <option v-for="category in categories" :key="category.value" :value="category.value">
                            {{ category.label }}
                        </option>
                    </select>
                    <label>🐝 루틴의 제목을 입력하세요</label>
                    <input v-model="newHabitName" type="text" class="form-control" placeholder="제목">
                    <label>🐝 얼마를 아낄 수 있을까요??</label>
                    <input v-model="newHabitSaveAmount" type="text" class="form-control" placeholder="예상 절약 금액">
                    <label>🐝 인증샷 조건을 알려주세요</label>
                    <input v-model="newHabitCertification" type="text" class="form-control" placeholder="인증샷 조건">
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button class="btn" :class="isAddFormValid ? 'btn-dark' : 'btn-secondary'" @click="addHabit"
                        :disabled="!isAddFormValid" data-bs-dismiss="modal">
                        추가하기
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- 루틴 수정 Modal -->
    <div class="modal fade" id="editModal">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">루틴 수정하기</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    <label>카테고리 선택</label>
                    <select v-model="editHabitCategory" class="form-select" aria-label="Category select"
                        id="categorySelect">
                        <option disabled value="">카테고리를 선택하세요</option>
                        <option v-for="category in categories" :key="category.value" :value="category.value">
                            {{ category.label }}
                        </option>
                    </select>
                    <label>이름</label>
                    <input v-model="editHabitName" type="text" class="form-control" placeholder="이름 입력">
                    <label>금액</label>
                    <input v-model="editHabitSaveAmount" type="text" class="form-control" placeholder="금액 입력">
                    <label>달성 조건</label>
                    <input v-model="editHabitCertification" type="text" class="form-control" placeholder="달성 조건 입력">
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button class="btn" data-bs-dismiss="modal" :class="isEditFormValid ? 'btn-dark' : 'btn-secondary'"
                        @click="editItem" :disabled="!isEditFormValid">
                        수정하기
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- 삭제 확인 모달 -->
    <div class="modal fade" id="removeModal">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">루틴 삭제하기</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    "{{ deleteHabitTitle }}" 루틴을 삭제하시겠습니까?
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button class="btn btn-danger" @click="removeItem" data-bs-dismiss="modal">삭제</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 공유 확인 모달 -->
    <div class="modal fade" id="communityModal">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">루틴 공유하기</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    "{{ addCommunityHabitTitle }}" 루틴을 공유하시겠습니까?
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    <button class="btn btn-danger" @click="addCommunity" data-bs-dismiss="modal">공유</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Alert Modal -->
    <CustomModal :isVisible="isModalVisible" title="알림" :message="modalMessage" @close="closeModal" />

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import CustomModal from '@/components/Modal.vue';
import { Modal } from 'bootstrap';
import { useHabitStore } from '@/stores/habitStore';
import draggable from "vuedraggable";
import axios from 'axios';

export default {
    name: "TaskManager",
    components: {
        draggable,
        CustomModal
    },
    setup() {
        // 상태
        const habitStore = useHabitStore();

        // Pinia에서 waitingList와 inProgressList를 가져오기
        const waitingList = computed(() => habitStore.habits.filter(habit => habit.state === '대기'));
        const inProgressList = computed(() => habitStore.habits.filter(habit => habit.state === '진행'));
        const completedList = computed(() => habitStore.habits.filter(habit => habit.isCheckedToday === true));

        const newHabitCategory = ref("");
        const newHabitName = ref("");
        const newHabitSaveAmount = ref("");
        const newHabitCertification = ref("");
        const addCommunityHabitId = ref("");
        const addCommunityHabitTitle = ref("");
        const editHabitId = ref("");
        const editMyHabitId = ref("");
        const editHabitName = ref("");
        const editHabitCategory = ref("");
        const editHabitSaveAmount = ref("");
        const editHabitCertification = ref("");
        const deleteListType = ref("");
        const deleteMyHabitId = ref("");
        const deleteHabitTitle = ref("");
        const deleteHabitId = ref("");
        const categories = ref([
            { value: "식비", label: "식비 (Food)" },
            { value: "카페/간식", label: "카페/간식 (Cafe/Snacks)" },
            { value: "온라인쇼핑", label: "온라인쇼핑 (Online Shopping)" },
            { value: "패션/쇼핑", label: "패션/쇼핑 (Fashion/Shopping)" },
            { value: "문화/여가", label: "문화/여가 (Culture/Leisure)" },
            { value: "술/유흥", label: "술/유흥 (Alcohol/Entertainment)" },
            { value: "교육", label: "교육 (Education)" },
            { value: "의료/건강", label: "의료/건강 (Medical/Health)" },
            { value: "생활", label: "생활 (Living)" },
            { value: "주거/공과금", label: "주거/공과금 (Housing/Utilities)" },
            { value: "금융", label: "금융 (Finance)" },
            { value: "뷰티", label: "뷰티 (Beauty)" },
            { value: "자동차", label: "자동차 (Automobile)" },
            { value: "교통", label: "교통 (Transportation)" },
            { value: "반려동물", label: "반려동물 (Pets)" },
            { value: "여행", label: "여행 (Travel)" },
            { value: "경조사/회비", label: "경조사/회비 (Events/Fees)" }
        ]);
        // const waitingList = ref([]);
        // const inProgressList = ref([]);
        const isModalVisible = ref(false);
        const modalMessage = ref('');

        // Modal 표시 함수
        const openModal = (message) => {
            modalMessage.value = message;
            isModalVisible.value = true;
        };

        // Modal 닫기 함수
        const closeModal = () => {
            isModalVisible.value = false;
        };


        // Computed
        const isAddFormValid = computed(() => newHabitCategory.value && newHabitName.value);
        const isEditFormValid = computed(() => editHabitCategory.value && editHabitName.value);

        // 메서드
        const handleDragStart = (evt) => {
            console.log("Drag started:", evt);
        };

        const checkListLength = () => {
            if (inProgressList.value.length >= 5) {
                openModal("진행 목록에는 최대 5개의 항목만 추가할 수 있습니다.");
                return false; // 이동을 막음
            }
        };
        const onDragEnd = (listType) => {
            console.log("Drag Ended for:", listType);
            try {
                if (listType === 'waiting') {
                    waitingList.value.forEach(habit => {
                        habit.state = '대기';
                    });
                } else if (listType === 'inProgress') {
                    // inProgressList에 5개 이상이면 더 이상 추가하지 않음
                    if (inProgressList.value.length >= 6) {
                        // alert("진행 목록에는 최대 5개의 항목만 추가할 수 있습니다.");
                        return;
                    }
                    else {
                        inProgressList.value.forEach(habit => {
                            habit.state = '진행';
                        });
                    }
                }
            } catch (error) {
                console.error("드래그 후 상태 업데이트 중 오류 발생:", error);
            }
        };

        const getCategoryClass = (category) => {
            const classes = {
                "식비": "badge-food",
                "카페/간식": "badge-snack",
                "온라인쇼핑": "badge-online-shopping",
                "패션/쇼핑": "badge-fashion",
                "문화/여가": "badge-culture",
                "술/유흥": "badge-alcohol",
                "교육": "badge-education",
                "의료/건강": "badge-health",
                "생활": "badge-lifestyle",
                "주거/공과금": "badge-housing",
                "금융": "badge-finance",
                "뷰티": "badge-beauty",
                "자동차": "badge-automobile",
                "교통": "badge-transport",
                "반려동물": "badge-pet",
                "여행": "badge-travel",
                "경조사/회비": "badge-events",
            };
            return classes[category] || "badge-default";
        };
        const addHabit = async () => {
            try {
                const request = {
                    userId: localStorage.getItem("userId"),
                    habitTitle: newHabitName.value,
                    categoryTitle: newHabitCategory.value,
                    saveAmount: newHabitSaveAmount.value,
                    certification: newHabitCertification.value,
                    state: "대기"
                };

                const response = await axios.post('http://localhost:8080/habits/add/my', request, {
                    headers: { 'Content-Type': 'application/json' }
                });
                if (response.data === 'duplicate') {
                    // alert("이미 같은 이름의 루틴이 존재합니다!");
                    openModal("이미 같은 이름의 루틴이 존재합니다!");
                    return;
                }
                // alert("루틴이 성공적으로 추가되었습니다!");
                openModal("루틴이 성공적으로 추가되었습니다!");

                habitStore.habits.push({
                    myHabitId: response.data.myHabitId,
                    habitId: response.data.habitId,
                    writerId: localStorage.getItem("userId"),
                    habitTitle: request.habitTitle,
                    categoryTitle: request.categoryTitle,
                    state: "대기",
                    saveAmount: request.saveAmount,
                    certification: request.certification,
                    isCheckedToday: false,
                    checkDate: null
                })

                newHabitCategory.value = "";
                newHabitName.value = "";
                newHabitSaveAmount.value = "";
                newHabitCertification.value = "";

            } catch (error) {
                console.error("습관 추가 중 오류 발생:", error);
                openModal("습관 추가 중 오류가 발생했습니다.");
            }
        };

        const confirmEdit = async (type, element) => {
            if (type === 'inProgress') {
                // alert('진행중인 루틴은 수정할 수 없습니다!');
                openModal('진행중인 루틴은 수정할 수 없습니다!');
                return;
            }

            const storedUserId = localStorage.getItem('userId');
            if (element.writerId != storedUserId) {
                // alert('루틴의 작성자가 아닙니다!');
                openModal('루틴의 작성자가 아닙니다!');
                return;  // 모달을 띄우지 않음
            }

            const sharedHabitResponse = await axios.get(`http://localhost:8080/routine-community/${editHabitId.value}`);

            if (sharedHabitResponse != null){
                openModal('이미 공유된 루틴은 수정할 수 없습니다!');
                return;
            }

            else {
                editMyHabitId.value = element.myHabitId;
                editHabitId.value = element.habitId;
                editHabitName.value = element.habitTitle;
                editHabitCategory.value = element.categoryTitle;
                editHabitSaveAmount.value = element.saveAmount;
                editHabitCertification.value = element.certification;
                // Bootstrap의 JavaScript API를 사용해 수동으로 모달을 띄움
                const editModal = new Modal(document.getElementById('editModal'));
                editModal.show();
            }
        };

        const editItem = async () => {
            
            const request = {
                myHabitId: editHabitId.value,
                habitId: editHabitId.value,
                userId: localStorage.getItem("userId"),
                habitTitle: editHabitName.value,
                categoryTitle: editHabitCategory.value,
                saveAmount: editHabitSaveAmount.value,
                certification: editHabitCertification.value
            }
            try {
                const response = await axios.put("http://localhost:8080/habits/update", request, {
                    headers: { 'Content-Type': 'application/json' }
                });
                // Pinia store에서 수정된 루틴 값을 반영 (splice를 이용해 배열 업데이트)
                const habitIndex = habitStore.habits.findIndex(habit => habit.myHabitId === editHabitId.value);
                if (habitIndex !== -1) {
                    habitStore.habits.splice(habitIndex, 1, {
                        ...habitStore.habits[habitIndex],
                        habitTitle: editHabitName.value,
                        categoryTitle: editHabitCategory.value,
                        saveAmount: editHabitSaveAmount.value,
                        certification: editHabitCertification.value
                    });
                }
                openModal('루틴을 수정했습니다!');
            }
            catch (error) {
                console.error("루틴 수정 중 오류 발생:", error);
            }

        };

        const updateRoutineState = async () => {
            try {
                const combinedList = [...waitingList.value, ...inProgressList.value];
                const userId = localStorage.getItem("userId");

                const response = await axios.put("http://localhost:8080/habits/update/state", combinedList, {
                    params: { userId: userId },
                    headers: { 'Content-Type': 'application/json' }
                });
                openModal("루틴 상태가 성공적으로 업데이트되었습니다!");

            } catch (error) {
                console.error("루틴 상태 업데이트 중 오류 발생:", error);
                openModal("루틴 상태 업데이트 중 오류가 발생했습니다.");
            }
        };

        const confirmRemove = (listType, myHabitId, habitId, habitTitle) => {
            deleteListType.value = listType;
            deleteMyHabitId.value = myHabitId;
            deleteHabitId.value = habitId;
            deleteHabitTitle.value = habitTitle;
        };

        const removeItem = async () => {
            try {
                await axios.delete("http://localhost:8080/habits/delete", {
                    params: {
                        myHabitId: deleteMyHabitId.value,
                        habitId: deleteHabitId.value
                    }
                });

                habitStore.habits = habitStore.habits.filter(item => item.myHabitId !== deleteMyHabitId.value);

                openModal("루틴이 성공적으로 삭제되었습니다!");
            } catch (error) {
                console.error("루틴 삭제 중 오류 발생:", error);
                openModal("루틴 삭제 중 오류가 발생했습니다.");
            }

            deleteListType.value = "";
            deleteMyHabitId.value = "";
            deleteHabitTitle.value = "";
        };
        const confirmAddCommunity = async (element) => {
            const storedUserId = localStorage.getItem('userId');
            if (element.writerId != storedUserId) {
                openModal('루틴의 작성자가 아닙니다!');
                return;  // 모달을 띄우지 않음
            }
            const response = await axios.get(`http://localhost:8080/routine-community/find/${element.habitId}`)
            console.log(response.data);
            if (response.data > 0) {
                openModal('이미 공유된 루틴입니다!')
                return;
            }
            else {
                addCommunityHabitId.value = element.habitId;
                addCommunityHabitTitle.value = element.habitTitle;
                // Bootstrap의 JavaScript API를 사용해 수동으로 모달을 띄움
                const editModal = new Modal(document.getElementById('communityModal'));
                editModal.show();
            }
        };
        const addCommunity = async () => {
            const request = {
                habitId: addCommunityHabitId.value,
                habitLikes: 0,
                participants: 1,
                complete: 0
            }
            try {
                await axios.post("http://localhost:8080/routine-community/add",
                    request, {
                    headers: { 'Content-Type': 'application/json' }
                });
                openModal("루틴이 성공적으로 공유되었습니다!");
            } catch (error) {
                console.error("루틴 공유 중 오류 발생:", error);
                openModal("루틴 공유 중 오류가 발생했습니다.");
            }

            addCommunityHabitId.value = "";
            addCommunityHabitTitle.value = "";
        }

        // LifeCycle Hook
        // onMounted(() => {
        //     getHabitList();
        // });

        return {
            newHabitCategory,
            newHabitName,
            newHabitSaveAmount,
            newHabitCertification,
            addCommunityHabitId,
            addCommunityHabitTitle,
            confirmAddCommunity,
            addCommunity,
            editHabitCategory,
            editHabitName,
            editMyHabitId,
            editHabitId,
            editHabitCertification,
            editHabitSaveAmount,
            deleteListType,
            deleteMyHabitId,
            deleteHabitTitle,
            deleteHabitId,
            categories,
            waitingList,
            inProgressList,
            completedList,
            isAddFormValid,
            isEditFormValid,
            handleDragStart,
            onDragEnd,
            getCategoryClass,
            // getHabitList,
            addHabit,
            updateRoutineState,
            confirmEdit,
            editItem,
            confirmRemove,
            removeItem,
            checkListLength,
            openModal,
            closeModal,
            isModalVisible,
            modalMessage
        }
    }
}
</script>


<style scoped>

/* 인포 */
.info {
    margin: 6% 8%;
}

.info h1{
    font-weight: 800;
}

.info p {
    /* font-weight: 700; */
    margin-top: 25px;
}

/* 활성화, 비활성화 박스 */
.col-6 {
    /* border: 1px solid black; */
    padding: 0px;
    margin: 0;
    width: 50%;
    flex-grow: 1; /* 두 박스가 가용 공간을 균등하게 나누도록 설정 */

}

.col-6 h4 {
    font-weight: 800;
}
.col-6 h4 img {
    margin-right: 15px;
}


/* 버튼을 옆에 */
.container {
    width: 100%;
    display: flex;
    flex-direction: row;
}

.button-section {
    width: 300px;
    flex: 0 0 10%;
    margin-top: 40px;
}

.container > .row {
    /* flex: 0 0 80%; */
    margin-right: -20px;
    /* width: 100%; */

    padding: 0;
}

.col-10 > .row {
    width: 100%;
    padding: 0;
    margin: 0;
}



/* 비활성화와 활성화 박스를 나란히 배치 */
.col-10 {
    display: flex;
    gap: 20px; 
    margin: 0;
    padding: 0;
}

.inactive-box, .active-box {
    /* border: 1px solid black; */
    padding: 10px;
    flex-grow: 1; /* 양쪽 박스를 균등하게 */
}

/* 사이드바를 오른쪽으로 붙이기 */
.sidebar {
    align-items: flex-end; /* 사이드바 버튼들을 오른쪽에 정렬 */
}

.dragArea {
    min-height: 150px;
    /* border: 1px solid #ccc; */
    /* border-radius: 8px; */
    padding: 10px;

    background: url('@/assets/images/background/honey_textbg.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    padding: 30px;
}

/* .allActive, .allInactive {
    background-color: #ffd739b6;
} */

/* .allActive, .allInactive {
    background: url('@/assets/images/background/honey_textbg.png');
    background-size: cover;
    background-repeat: no-repeat;
    padding: 10px;
}

.allTodayChecked {
    background: url('@/assets/images/background/honey_textbg.png');
} */

.allActive, .allInactive, .sidebar button, .list-group-item {
    border-radius: 0 ;
}

.list-group-item {
    cursor: grab;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 3px;
    border-radius: 0px;

    /* background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='black' stroke-width='4' stroke-dasharray='11%2c 10' stroke-dashoffset='27' stroke-linecap='butt'/%3e%3c/svg%3e"); */
}

.list-group-item:active {
    cursor: grabbing;
}

/* Category badge styling */
.category-badge {
    padding: 5px 10px;
    /* border-radius: 12px; */
    color: white;
    font-size: 0.75rem;
    width: 70px;
    text-align: center;
    text-overflow: ellipsis;
}

/* Category specific styles */
.category-badge {
    background-color: black;
    font-size: 11px;
    min-width: 70px;
}
/* .badge-food {
    background-color: #5193F4;
}

.badge-snack {
    background-color: #8B6555;
}

.badge-online-shopping {
    background-color: #3EBAC2;
}

.badge-fashion {
    background-color: #F5778D;
}

.badge-culture {
    background-color: #8C46C2;
}

.badge-alcohol {
    background-color: #FFAD0D;
}

.badge-education {
    background-color: #A2C616;
}

.badge-health {
    background-color: #FA4949;
}

.badge-lifestyle {
    background-color: #5B9FF4;
}

.badge-housing {
    background-color: #6D5D3F;
}

.badge-finance {
    background-color: #757E95;
}

.badge-beauty {
    background-color: #E659E9;
}

.badge-automobile {
    background-color: #5B9FF4;
}

.badge-transport {
    background-color: #949BAD;
}

.badge-pet {
    background-color: #6D5D3F;
}

.badge-travel {
    background-color: #696E76;
}

.badge-events {
    background-color: #F39C77;
}

.badge-default {
    background-color: #ffdfba;
    color: #d08b44;
} */

/* 루틴 명 스타일 */
.task-name {
    /* font-weight: bold; */
    font-size: 1rem;
}


/* 벌 이미지 */
.beeImg {
    width: 60px;
}

/* .icon {
    margin: -10px;
} */

.type {
    font-weight: 700;
    font-size: 18px;
}

.modal-body {
  padding: 20px;
}
.modal-body label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
}

.modal-body input,
.modal-body select {
  margin-bottom: 20px; /* 구성 요소들 사이의 간격을 추가 */
  padding: 10px;
  width: 100%;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.modal-header {
  padding: 15px 20px;
}

.modal-footer {
  padding: 10px 20px;
}

.modal-footer button {
  margin-right: 10px;
}

/* 추가적인 모달 컨텐츠 간격 조정 */
.modal-body input {
  margin-bottom: 20px; /* 각 입력 필드 사이 간격 */
}

.modal-body select {
  margin-bottom: 20px; /* 셀렉트 박스와 다른 필드 간격 */
}
</style>
