<template>
  <div>
    <ListItem />

    <!-- <ul class="news-list"> -->
    <!--  v-for디렉티브는 in 뒤에 오는 ask배열을 반복하여 div태그 돌림, 결과를 in 앞에 있는 변수로 받음 -->
    <!-- <div v-for="item in fetchedAsk">* {{ item.title }}</div> -->
      <!-- <li v-for="item in fetchedAsk" class="post"> -->
        <!-- 포인트 영역(숫자나오는 영역)-->
        <!-- <div class="points">
          {{ item.points }}
        </div> -->
        <!-- 기타 정보 영역: 유저 내용들-->
        <!-- <div>
          <p class="news-title">
            <router-link v-bind:to="`item/${item.id}`">{{item.title}}</router-link>
              {{ item.title }}
            </a>
          </p>
          <small class="link-text">
            {{item.time_ago}} by
            <router-link class="link-text" v-bind:to="`/user/${item.user}`">{{ item.user }}</router-link>
          </small>
        </div>
      </li>
    </ul>-->
  </div> 
</template>

<script>
// import { mapState } from "vuex";
//import { mapGetters } from "vuex";
import ListItem from "../components/ListItem.vue";
export default {
  components: {
    ListItem
  },
  //computed: {
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
      //...mapGetters(['fetchedAsk'])
  //},
  //created() {
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
    //this.$store.dispatch('FETCH_ASK');
  //},
};
</script>

<style>

</style>