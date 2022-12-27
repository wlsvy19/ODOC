<template>
  <li>
    <span class="item" :class="todoItemClass" @click="toggleItem">{{ todoItem.title }}</span>
    <button @click="removeItem">삭제</button>
  </li>
</template>
<script lang="ts">
import { Todo } from "@/App.vue";
import Vue, { PropType } from "vue";

export default Vue.extend({
  // ts로 넘어오면서 타입과 필수여부 설정 가능
  props: {
    todoItem: Object as PropType<Todo>,
    index: Number,
  }, // end props
  methods: {
    toggleItem() {
      this.$emit("toggle", this.todoItem, this.index);
    }, // end toggleItem()

    removeItem() {
      this.$emit("remove", this.index);
    }, // end removeItem()
  }, //  end methods

  // computed: 반환 타입 반드시 정의
  computed: {
    // 클래스 바인딩으로 토글 구현
    todoItemClass(): string | null {
      return this.todoItem.done ? "complete" : null;
    },
  },
});
</script>

<style scoped>
.item {
  cursor: pointer;
}
.complete {
  text-decoration: line-through;
}
</style>