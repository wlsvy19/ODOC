<template>
  <div class="inputBox shadow">
    <!--2way 바인딩 화면데이터와 스크립트 데이터의 동기화 -->
    <input type="text" v-model="newTodoItem" v-on:keyup.enter="addTodo" />
    <!-- <button v-on:click="addTodo">add</button> -->
    <span class="addContainer" v-on:click="addTodo">
      <i class="fa-regular fa-plus addBtn">+</i>
    </span>
    <AlertModal v-if="showModal" @close="showModal = false">
      <h3 slot="header">경고!
        <button class="closeModalBtn fas fa-times" @click="showModal = false">x</button>
      </h3>
      <div slot="body">아무것도 입력하지 않았습니다.</div>
    </AlertModal>
  </div>
</template>

<script>
import AlertModal from "./common/AlertModal.vue"
export default {
  data () {
    return {
      newTodoItem: "",
      showModal: false
    };
  },
  methods: {
    addTodo () {
      // input에 값이 있을 때만 추가
      if (this.newTodoItem !== "") {
        // 저장하는 로직 수행
        // key,value->개발자도구-애플리케이션-저장용량-로컬스토리지에 저장
        // var obj = { completed: false, item: this.newTodoItem };
        // //JS객체->String변환
        // localStorage.setItem(this.newTodoItem, JSON.stringify(obj));

        //App.vue로 이벤트 올라가서 v-on으로 받음, 데이터 인자로 넘겨줄 수 있음
        //this.$emit("addTodoItem", this.newTodoItem); // -> store에 mutation으로 변경
        this.$store.commit('addOneItem', this.newTodoItem);
        this.clearInput();
      } else {
        // alert("내용을 입력해주세요!");
        this.showModal = !this.showModal
      }
    },

    // clearInput: function() {
    //   this.newTodoItem = "";
    // },
    // 위와 동일, 이 형태로 사용 해도 됨
    clearInput() {
      // 인풋박스 초기화
      this.newTodoItem = "";
    },
  },
  components: {
    AlertModal,
  }
};
</script>

<style scoped>
input:focus {
  outline: none;
}
.inputBox {
  background: white;
  height: 50px;
  line-height: 50px;
  border-radius: 5px;
}
.inputBox input {
  border-style: none;
  font-size: 0.9rem;
}
.addContainer {
  float: right;
  background: linear-gradient(to right, #6478fb, #8763fb);
  display: block;
  width: 3rem;
  border-radius: 0 5px 5px 0; /* 테두리 둥글게*/
}
.addBtn {
  color: white;
  vertical-align: middle;
}
.closeModalBtn {
  color: #42b983;
}
</style>
