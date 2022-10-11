<template>
  <div id="app">
    <TodoHeader />
    <TodoInput v-on:addTodoItem ="addOneItem"/> <!--하위컴포넌트 emit받음-->
    <TodoList v-bind:propsdata="todoItems" 
      v-on:removeItem="removeOneItem" 
      v-on:toggleItem="toggleOneItem"/>
    <TodoFooter v-on:clearAll="clearAllItems"/>
  </div>
</template>

<script>
import TodoHeader from "./components/TodoHeader.vue";
import TodoInput from "./components/TodoInput.vue";
import TodoList from "./components/TodoList.vue";
import TodoFooter from "./components/TodoFooter.vue";

export default {
  data: function(){
    return {
      todoItems: [] // 배열-> TodoList로 보냄
    }
  },
  methods: { 
    addOneItem: function (todoItem) { // todoItem을 인자로 받음
        var obj = { completed: false, item: todoItem};
        //JS객체->String변환
        localStorage.setItem(todoItem, JSON.stringify(obj));
        //!!리팩토링 결과 추가하면 App.vue컴포넌트에 바로 출력 됨
        this.todoItems.push(obj); // push: JS API, 배열 맨끝에 값 넣음
    },
    removeOneItem: function(todoItem, index) {
      localStorage.removeItem(todoItem.item); 
      this.todoItems.splice(index, 1);
    },
    toggleOneItem: function (todoItem, index) {
      this.todoItems[index].completed =! this.todoItems[index].completed;
      localStorage.removeItem(todoItem.item);
      localStorage.setItem(todoItem.item, JSON.stringify(todoItem));
    },
    clearAllItems : function() {
      localStorage.clear();
      // 빈 배열로 만들어서 화면에 표출
      this.todoItems = []; 
    }
  },
  created: function () {
    if (localStorage.length > 0) {
      for (var i = 0; i < localStorage.length; i++) {
        // 로컬스토리지에 웹펙 저장되어 있는건 출력 안함
        if (localStorage.key(i) !== "loglevel:webpack-dev-server") {

          // 키만 넣음
          // this.todoItems.push(localStorage.key(i));

          // value에서 item을 맞춰줘야 completed접근 가능
          // String->JSON(객체) 변환
          this.todoItems.push(JSON.parse(localStorage.getItem(localStorage.key(i))));
        }
      }
    }
  },
  components: {
    // 컴포넌트 태그명: 컴포넌트 이름
    'TodoHeader': TodoHeader,
    'TodoInput': TodoInput,
    'TodoList': TodoList,
    'TodoFooter': TodoFooter,
  },
};
</script>

<style>
body {
  text-align: center;
  background-color: #f6f6f6;
}
input {
  border-style: groove;
  width: 200px;
}
button {
  border-style: groove;
}
.shadow {
  box-shadow: 50px 10px 10px rgba(0, 0, 0, 0.03);
}
</style>
