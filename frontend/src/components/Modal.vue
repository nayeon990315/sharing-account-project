<template>
    <div class="modal" v-if="isVisible">
      <div class="modal-content">
        <h4 id="modal-title"><strong>{{ title }}</strong></h4>
        <p id="modal-content">{{ message }}</p>
        <button @click="closeModal">확인</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
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
    },
    methods: {
      closeModal() {
        this.$emit('close');
      },
    },
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
    z-index: 1000;
  }
  
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
    text-align: center;
  }

  #modal-title{
    text-align: center;
    margin-top: 10px;
  }

  #modal-content {
    margin-top: 10px;
  }
  
  button {
    margin-top: 10px;
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    border: 3px solid #90893dcd;
    background-color: #90893dcd;
    color: white;
  }

  button:hover {
    background-color: white;
    color: #90893dcd;
  }
  </style>