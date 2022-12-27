<template>
  <div>
    <transition-group name="list" tag="ul">
      <!-- list에서 v-for로 반복문 돌려 줌 -->
      <!-- <li v-for="(todoItem, index) in this.$store.state.todoItems" v-bind:key="index" class="shadow"> -->
      <!-- <li v-for="(todoItem, index) in this.$store.getters.storedTodoItems" v-bind:key="index" class="shadow"> -->
      <!-- <li v-for="(todoItem, index) in this.todoItems" v-bind:key="index" class="shadow"> -->
      <li v-for="(todoItem, index) in this.storedTodoItems" v-bind:key="index" class="shadow">
       <i class="checkBtn fa-solid fa-check" v-bind:class="{checkBtnComplated: todoItem.completed}" 
          v-on:click="toggleComplete({todoItem, index})">✔</i>
        <span v-bind:class="{textCompleted: todoItem.completed}">{{ todoItem.item }}</span>
        <!-- <span class="removeBtn" v-on:click="removeTodo(todoItem, index)"> -->
        <span class="removeBtn" v-on:click="removeTodo({todoItem, index})">
          <i class="fa-solid fa-trash">삭제</i>
        </span>
      </li>
    </transition-group>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'

export default {
  computed: { // 계산 속성
    // todoItems() {
    //   return this.$store.getters.storedTodoItems;
    // }
    
    // Spread Operator
    ...mapGetters(['storedTodoItems']) // Helper사용-배열 리터럴
  },
  // 리팩토링...
  //props: ['propsdata'], // App.vue 데이터 props로 받음 -> store로 변경
  // data: function () {
  //   return {
  //     todoItems: [],
  //   };
  // },
  methods: {
    ...mapMutations({
      removeTodo: 'removeOneItem',
      toggleComplete: 'toggleOneItem'
    }),

    //removeTodo(todoItem, index) {
      //this.$emit('removeItem', todoItem, index);
      //this.$store.commit('removeOneItem', {todoItem, index});

      // 리펙토링...
    //   localStorage.removeItem(todoItem); // 키삭제->Value삭제
    //   // 화면상에 삭제된거 바로 나오게 해야함
    //   this.todoItems.splice(index, 1); // 기존 배열 변경 후 새로운 배열로 반환, slice:기존배열변경
    //},

    //toggleComplete(todoItem, index) {
      // todoItem.completed =! todoItem.completed;
      // // 로컬 스토리지의 데이터 갱신
      // localStorage.removeItem(todoItem.item);
      // localStorage.setItem(todoItem.item, JSON.stringify(todoItem));

      //this.$emit('toggleItem', todoItem, index)
      //this.$store.commit('toggleOneItem', {todoItem, index});
    //}
  },
// 리팩토링...
  // Vue인스턴스가 생성 되자마자 호출 되는 라이프 사이클 훅
  // created: function () { // App.vue로 이동
  //   if (localStorage.length > 0) {
  //     for (var i = 0; i < localStorage.length; i++) {
  //       // 로컬스토리지에 웹펙 저장되어 있는건 출력 안함
  //       if (localStorage.key(i) !== "loglevel:webpack-dev-server") {

  //         // 키만 넣음
  //         // this.todoItems.push(localStorage.key(i));

  //         // value에서 item을 맞춰줘야 completed접근 가능
  //         // String->JSON(객체) 변환
  //         this.todoItems.push(JSON.parse(localStorage.getItem(localStorage.key(i))));
  //       }
  //     }
  //   }
  // },
};
</script>

<style scoped>
ul {
	list-style-type: none;
	padding-left: 0px;
	margin-top: 0;
	text-align: left;
}
li {
	display: flex;
	min-height: 50px;
	height: 50px;
	line-height: 50px;
	margin: 0.5rem 0;
	padding: 0 0.9rem;
	background: white;
	border-radius: 5px;
}
.removeBtn {
    margin-left: auto;
    color: #de4343;
  }
.checkBtn {
	line-height: 45px;
	color: #62acde;
	margin-right: 5px;
}
.checkBtnComplated {
	color: #b3adad;
}
.textCompleted {
	text-decoration: line-through;
	color: #b3adad;
}

/* 리스트 아이템 트랜지션 효과*/
.list-enter-active, .list-leave-active {
  transition: all 0.3s; /* 움직이는 속도*/
}
.list-enter, .list-leave-to /* .list-leave-active below version 2.1.8 */ {
  opacity: 0;
  transform: translateY(30px);
}
</style>
