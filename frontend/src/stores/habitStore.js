import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios';

export const useHabitStore = defineStore('habitStore', () => {
  // State
  const habits = ref([]);
  const selectedMyHabitId = ref(null);

  // Action
  const useFilteredHabit = computed((myHabitId) => {
    console.log(habits.value)
    return habits.value.find(habit => habit.myHabitId === myHabitId.value);
  })

  const activeHabits = computed(() => habits.value.filter(habit => habit.state === '진행'))

  const getHabitsFromServer = async (userId) => {
    try {
      const response = await axios.post(
        'http://localhost:8080/habits/my-today-info',
        { userId },
        {
          headers: {
            'Content-Type': 'application/json',
          }
        }
      );

      const rawHabits = response.data;

      habits.value = rawHabits.map((habit) => {
        return {
          ...habit,
          checkDate: new Date(habit.checkDate), // 날짜 파싱
          isCheckedToday: habit.isCheckedToday === 'true' ? true : false,
        };
      });
    } catch (error) {
      console.error('Error fetching habits:', error);
    }
  }
  return {
    habits,
    selectedMyHabitId,
    getHabitsFromServer,
    activeHabits,
    useFilteredHabit
  };
  }, {
    persist: true,  // persist 활성화
});

