<template>
  <div>
    <label for="todo-input">오늘 할 일: </label>
    <input id="todo-input" type="text" :value="item" @input="handleInput" />
    <button @click="addTodo" type="button">추가</button>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import {VueEvent} from "@/types/index"

export default Vue.extend({
  // props: ["item"],
  props: {
    item: {
      type: String,
      required: true,
    },
  },
  methods: {
    handleInput(event: VueEvent.Input<HTMLInputElement>) {

      // event.target.value; 을 사용하려는데 안됨 -> EventTarget | null <-> HTMLInputElement 호환안됨 ->  커스텀으로 만들어보자
      const eventTarget = event.target as HTMLInputElement;
      // this.$emit("input", eventTarget.value);

      // .value 접근 가능
      this.$emit("input", event.target.value);
    }, // end handleInput()

    addTodo() {
      this.$emit("add"); // -> 상위컴포넌트에서 v-on 이벤트로 받음
    }, // end addTodo()
  },
});
</script>

<style>
</style>
