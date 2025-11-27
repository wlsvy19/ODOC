<template>
  <!-- <div>{{ message }}</div>
  <div>{{ counter }}</div>
  <div>{{ num }}</div>

  <div>
    <button @click="increment">+</button>
    <button @click="decrement">-</button>
  </div> -->

  <div>
    <h2>반응형 메시지</h2>
    <p>{{ reactiveMsg }}</p>
    <button @click="reactiveBtn">reactive</button>
    <h2>일반 메시지</h2>
    <p>{{ normalMsg }}</p>
    <button @click="normalBtn">normal</button>
  </div>
</template>

<script>
import { isReactive, isRef, ref } from 'vue';

export default {
  setup() {
    const counter = ref(2);
    const num = 4;

    const reactiveMsg = ref('reactive msg');

    const normalMsg = 'normal msg';

    const increment = () => {
      counter.value++;
      num.valueOf++;
    };
    function decrement() {
      counter.value--;
    }

    function reactiveBtn() {
      reactiveMsg.value = reactiveMsg.value + '!';
    }
    const normalBtn = () => {
      normalMsg = normalMsg + '!';
    };
    return {
      counter,
      //message,
      increment,
      decrement,
      num,
      reactiveMsg,
      normalMsg,
      reactiveBtn,
      normalBtn,
    };
  },
};
</script>

<style scoped></style>
