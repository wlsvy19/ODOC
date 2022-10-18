<template>
  <div>
    <h1>Ask...</h1>
    <!--  v-for디렉티브는 in 뒤에 오는 ask배열을 반복하여 div태그 돌림, 결과를 in 앞에 있는 변수로 받음 -->
    <!-- <div v-for="item in fetchedAsk">* {{ item.title }}</div> -->
    <p v-for="item in fetchedAsk">
      <a v-bind:href="item.url">* {{ item.title }}</a>
      <small> {{item.time_ago}} by {{item.user}}</small>
    </p>
  </div>
</template>

<script>
// import { mapState } from "vuex";
import { mapGetters } from "vuex";
export default {
  computed: {
    // 표현식의 여러가지 방법->computed 사용

    // 1 .computed에서 처리
    // ask() {
    //   return this.$store.state.ask_items;
    // }
    
    // 2. map 헬퍼 함수 사용
    // ...mapState({
    //   ask: state => state.ask_items
    // })

    // 3. map 헬퍼 함수 사용-store에 getters사용
      // ...mapGetters({
      //   // fetchedAsk: 'fetchedAsk'
      //   askItems: 'fetchedAsk'

      // })

      // 4. map 헬퍼 함수 사용-store getters 바로 사용
      ...mapGetters([
        'fetchedAsk'
      ])
  },
  created() {
    // var vm = this;
    // 속성에 함수 붙으면 : fuction 생략 가능
    // fetchAskList()
    //   .then(function (response) {
    //     console.log(response.data)
    //     vm.ask = response.data;
    //   })
    //   .catch(function (error) {
    //     console.log(error);
    //   });
    this.$store.dispatch("FETCH_ASK");
  },
};
</script>

<style>
</style>