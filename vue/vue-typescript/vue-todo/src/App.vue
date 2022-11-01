<template>
  <div>
    <header>
      <h1>Vue Todo width ypeScript</h1>
    </header>
    <main>
      <TodoInput
        :item="todoText"
        v-on:input="updateTodoText"
        v-on:add="addTodoItem"
      ></TodoInput>
      <div>
        <ul>
          <TodoListItem
            v-for="(todoItem, index) in todoItems"
            :key="index"
            :index="index"
            :todoItem="todoItem"
            @toggle="toggleTodoItemComplete"
            @remove="removeTodoItem"
          ></TodoListItem>
          <!-- <li>아이템 1</li>
          <li>아이템 2</li>
          <li>아이템 3</li> -->
        </ul>
      </div>
    </main>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import TodoInput from "./components/TodoInput.vue";
import TodoListItem from "./components/TodoListItem.vue";

const STORAGE_KEY = "vue-todo-ts-v1";
const storage = {
  save(todoItems: Todo[]) {
    // 배열->문자열 변환 후 넣어야함
    const parsed = JSON.stringify(todoItems);
    localStorage.setItem(STORAGE_KEY, parsed);
  },
  fetch(): Todo[] {
    // item없으면 빈배열로 초기화
    const todoItems = localStorage.getItem(STORAGE_KEY) || "[]";
    const result = JSON.parse(todoItems);
    return result;
  }, // end fetch()
};

export interface Todo {
  title: String;
  done: boolean;
}

export default Vue.extend({
  name: "App",
  components: {
    TodoInput,
    TodoListItem,
  }, // end components

  data() {
    return {
      todoText: "",
      todoItems: [] as Todo[],
    };
  }, // end data

  methods: {
    updateTodoText(value: string) {
      this.todoText = value;
    }, // end updateTodoText()

    addTodoItem() {
      const value = this.todoText;
      const todo: Todo = {
        title: value,
        done: false,
      };
      this.todoItems.push(todo);
      storage.save(this.todoItems);
      //localStorage.setItem(value, value);
      this.initTodoText();
    }, // end addTodoItem()

    // input박스 초기화
    initTodoText() {
      this.todoText = "";
    }, // end initTodoText()

    // 데이터 가져오는 메소드
    fetchTodoItems() {
      this.todoItems = storage.fetch().sort((a, b) => {
        if (a.title < b.title) {
          return -1;
        }
        if (a.title > b.title) {
          return 1;
        }
        return 0;
      });
    }, // end fetchTodoItems()

    removeTodoItem(index: number) {
      console.log("remove", index);
      this.todoItems.splice(index, 1);
      storage.save(this.todoItems);
    }, // end removeTodoItem()

    toggleTodoItemComplete(todoItem: Todo, index: number) {
      this.todoItems.splice(index, 1, {
        ...todoItem,
        done: !todoItem.done,
      });
      storage.save(this.todoItems);
    },
  }, // end methods

  created() {
    this.fetchTodoItems();
  }, // end created
});
</script>

<style>
</style>
