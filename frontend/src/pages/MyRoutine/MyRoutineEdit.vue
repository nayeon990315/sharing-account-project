<template>
    <div class="container mt-5">
        <div class="row">
            <!-- 대기 리스트 -->
            <div class="col-6">
                <h4 class="d-flex align-items-center">
                    <i class="fa fa-circle px-2" style="font-size:16px; color:red;" aria-hidden="true"></i>
                    대기
                </h4>
                <draggable class="dragArea list-group" :list="waitingList" group="tasks" item-key="habitId"
                    @start="handleDragStart">
                    <template #item="{ element }">
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div class="d-flex align-items-center">
                                <span class="category-badge" :class="getCategoryClass(element.categoryTitle)">
                                    {{ element.categoryTitle }}
                                </span>
                                <span class="task-name mx-3">{{ element.habitTitle }}</span>
                            </div>
                            <div class="d-flex bd-highlight">
                                <button class="btn" data-bs-toggle="modal" data-bs-target="#editModal" @click="editItem(element)">
                                    <i class="fa fa-pencil text-primary" aria-hidden="true"></i>
                                </button>
                                <button class="btn" data-bs-toggle="modal" data-bs-target="#editModal" @click="removeItem('waiting', element.id)">
                                    <i class="fa fa-minus-circle text-danger" aria-hidden="true"></i>
                                </button>
                            </div>

                        </div>
                    </template>
                </draggable>
            </div>

            <!-- 진행 중 리스트 -->
            <div class="col-6">
                <h4 class="d-flex align-items-center">
                    <i class="fa fa-circle px-2" style="font-size:16px; color:green;" aria-hidden="true"></i>
                    진행중
                </h4>
                <draggable class="dragArea list-group" :list="inProgressList" group="tasks" item-key="habitId">
                    <template #item="{ element }">
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div class="d-flex align-items-center">
                                <span class="category-badge" :class="getCategoryClass(element.categoryTitle)">
                                    {{ element.categoryTitle }}
                                </span>
                                <span class="task-name mx-3">{{ element.habitTitle }}</span>
                            </div>
                            <div class="d-flex bd-highlight">
                                <button class="btn" data-bs-toggle="modal" data-bs-target="#editModal" @click="editItem(element)">
                                    <i class="fa fa-pencil text-primary" aria-hidden="true"></i>
                                </button>
                                <button class="btn" @click="removeItem('inProgress', element.habitId)">
                                    <i class="fa fa-minus-circle text-danger" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                    </template>
                </draggable>
            </div>
        </div>
        <button type="button" class="btn btn-dark mt-5 " data-bs-toggle="modal" data-bs-target="#myModal">
            습관 추가하기
        </button>
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
                    <label>카테고리 선택</label>
                    <select v-model="newHabitCategory" class="form-select" aria-label="Category select"
                        id="categorySelect">
                        <option disabled value="">카테고리를 선택하세요</option>
                        <option v-for="category in categories" :key="category.value" :value="category.value">
                            {{ category.label }}
                        </option>
                    </select>
                    <label>이름</label>
                    <input v-model="newHabitName" type="text" class="form-control" placeholder="이름 입력">
                    <label>금액</label>
                    <input v-model="newHabitSaveAmount" type="text" class="form-control" placeholder="금액 입력">
                    <label>달성 조건</label>
                    <input v-model="newHabitCertification" type="text" class="form-control" placeholder="달성 조건 입력">
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button class="btn" :class="isAddFormValid ? 'btn-dark' : 'btn-secondary'" @click="addHabit"
                        :disabled="!isAddFormValid">
                        추가하기
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Modal -->
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
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button class="btn" data-bs-dismiss="modal" :class="isEditFormValid ? 'btn-dark' : 'btn-secondary'" @click="editHabbit(element)"
                        :disabled="!isEditFormValid">
                        수정하기
                    </button>
                </div>
            </div>
        </div>

    </div>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</template>

<script>
import draggable from "vuedraggable";
import axios from 'axios';
import {ref} from 'vue';

let id = 7;
export default {
    name: "TaskManager",
    components: {
        draggable
    },
    data() {
        return {
            newHabitCategory: "",
            newHabitName: "",
            newHabitSaveAmount: "",
            newHabitCertification: "",
            editHabitCategory: "",
            editHabitName: "",
            editHabbitId: 0,
            categories: [
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
            ],
            waitingList: [],
            inProgressList: []
            // waitingList: [
            //     { id: 1, name: "구독 서비스 점검하기", category: "생활" },
            //     { id: 2, name: "반려동물 수제 장난감 만들기", category: "반려동물" },
            //     { id: 3, name: "퇴근길에 택시 말고 버스 타기", category: "교통" },
            // ],
            // inProgressList: [
            //     { id: 4, name: "커피 텀블러에 담아가기", category: "카페/간식" },
            //     { id: 5, name: "헌혈하고 영화 보기", category: "문화/여가" },
            // ],
        };
    },
    mounted(){
        this.getHabitList();
    },
    computed: {
        isAddFormValid() {
            // 카테고리와 이름이 모두 존재할 때만 true
            return this.newHabitCategory && this.newHabitName
        },
        isEditFormValid() {
            // 카테고리와 이름이 모두 존재할 때만 true
            return this.editHabitCategory && this.editHabitName
        }
    },
    methods: {
        handleDragStart(evt) {
            console.log("Drag started:", evt);
        },
        removeItem(listType, id) {
            if (listType === "waiting") {
                this.waitingList = this.waitingList.filter(item => item.id !== id);
            } else if (listType === "inProgress") {
                this.inProgressList = this.inProgressList.filter(item => item.id !== id);
            }
        },
        // removeItem('inProgress', element.id)
        editItem(element) {
            // const id = element.id
            
            // let tempList;
            // if (listType === "waiting") {
            //     tempList = this.waitingList.filter(item => item.id === id);
            // } else if (listType === "inProgress") {
            //     tempList = this.inProgressList.filter(item => item.id === id);
            // }
            // console.log(tempList[0])
            this.editHabitCategory = element.category
            this.editHabitName = element.name
            this.editHabbitId = element.id
        },
        getCategoryClass(category) {
            switch (category) {
                case "식비":
                    return "badge-food";
                case "카페/간식":
                    return "badge-snack";
                case "온라인쇼핑":
                    return "badge-online-shopping";
                case "패션/쇼핑":
                    return "badge-fashion";
                case "문화/여가":
                    return "badge-culture";
                case "술/유흥":
                    return "badge-alcohol";
                case "교육":
                    return "badge-education";
                case "의료/건강":
                    return "badge-health";
                case "생활":
                    return "badge-lifestyle";
                case "주거/공과금":
                    return "badge-housing";
                case "금융":
                    return "badge-finance";
                case "뷰티":
                    return "badge-beauty";
                case "자동차":
                    return "badge-automobile";
                case "교통":
                    return "badge-transport";
                case "반려동물":
                    return "badge-pet";
                case "여행":
                    return "badge-travel";
                case "경조사/회비":
                    return "badge-events";
                default:
                    return "badge-default";
            }
        },
        async getHabitList() {
            try {
                const response = await axios.get('http://localhost:8080/habits/', {
                    params: {
                        userId: localStorage.getItem("userId")
                    }
                });
                this.inProgressList = response.data; // ref 대신 바로 할당
                console.log(this.inProgressList);
            } catch (error) {
                console.error("습관 가져오기 중 오류 발생:", error);
                alert("습관 가져오기 중 오류가 발생했습니다.");
            }
        },
        async addHabit() {
            try {
                // 데이터 구성
                const request = {
                    userId: localStorage.getItem("userId"), // 사용자 ID는 localStorage에서 가져옴
                    habitTitle: this.newHabitName,          // 습관 제목
                    categoryTitle: this.newHabitCategory,   // 카테고리 제목
                    saveAmount: this.newHabitSaveAmount,    // 금액 저장
                    certification: this.newHabitCertification, // 달성 조건
                    state: "waiting"  // 상태를 대기로 설정
                };
                
                // 서버로 POST 요청 전송
                const response = await axios.post('http://localhost:8080/habits/add/my', request, {
                    headers: {
                        'Content-Type': 'application/json',
                    }
                });
                console.log("응답 받음:", response.data);
                alert("루틴이 성공적으로 추가되었습니다!");
                console.log(request);
                // 입력 필드 초기화
                this.newHabitCategory = "";
                this.newHabitName = "";
                this.newHabitSaveAmount = "";
                this.newHabitCertification = "";

                // 대기 목록에 새 항목 추가
                this.waitingList.push({
                    id: response.data.myHabitId, // 서버에서 반환된 ID 사용
                    name: data.habitTitle,
                    category: data.categoryTitle
                });

            } catch (error) {
                console.error("습관 추가 중 오류 발생:", error);
                alert("습관 추가 중 오류가 발생했습니다.");
            }
        },
        editHabbit() {
            let habit = this.waitingList.find(item => item.id === this.editHabbitId);
            if (!habit) {
                habit = this.inProgressList.find(item => item.id === this.editHabbitId);
            }
            
            if (habit) {
                habit.category = this.editHabitCategory;
                habit.name = this.editHabitName;

                this.editHabitCategory = "";
                this.editHabitName = "";
                this.editHabbitId = 0;
            }
        }

    }
};
</script>

<style scoped>
.dragArea {
    min-height: 150px;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
}

.list-group-item {
    cursor: grab;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.list-group-item:active {
    cursor: grabbing;
}

/* Category badge styling */
.category-badge {
    padding: 5px 10px;
    border-radius: 12px;
    color: white;
    font-size: 0.75rem;
    width: 70px;
    text-align: center;
    text-overflow: ellipsis;
}

/* Category specific styles */
.badge-food {
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
}

/* 루틴 명 스타일 */
.task-name {
    font-weight: bold;
    font-size: 1rem;
}
</style>
