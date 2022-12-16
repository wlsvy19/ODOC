<template>
  <div>
    <ListItem />
    <!-- <ul class="news-list">
      <li v-for="item in this.$store.state.news" class="post"> -->
    <!-- 포인트 영역(숫자나오는 영역)-->
    <!-- <div class="points">
          {{ item.points }}
        </div> -->
    <!-- 기타 정보 영역: 유저 내용들-->
    <!-- <div>
          <p class="news-title">
            <a v-bind:href="item.url">
              {{ item.title }}
            </a>
          </p>
          <small class="link-text">
            {{item.time_ago}} by
            <router-link class="link-text" v-bind:to="`/user/${item.user}`">{{ item.user }}</router-link>
          </small>
        </div> -->
    <!-- <a v-bind:href="item.url">
          {{ item.title }}
        </a>
        <small>
          {{ item.time_ago }} by
          <router-link v-bind:to="`/user/${item.user}`">{{
            item.user
          }}</router-link>
        </small> -->
    <!-- </li>
    </ul> -->
    <!--
    <p v-for="item in this.$store.state.news">
      <a v-bind:href="item.url">* {{ item.title }}</a>
      <small> 
        {{ item.time_ago }} by 
        <router-link v-bind:to="`/user/${item.user}`">{{ item.user }}</router-link>
      </small>
    </p> -->
  </div>
</template>

<script>
import ListItem from "../components/ListItem.vue";
import ListMixin from "../mixins/ListMixin.js";

export default {
  components: {
    ListItem,
  },
  mixins: [ListMixin],
  // vuex 로 처리->사용x
  // data() {
  //   return {
  //     users: [],
  //   };
  // },
  //created() {
  // 컴포넌트 생성되자마자 들어가는 로직
  //var vm = this; // vue 컴포넌트를 바라보는 상태가 아니라서 변수 선언으로 바인딩 해줘야 함
  // 프로미스 기반 API제공: new Promise() 객체 반환
  /*
    fetchNewsList()
      .then((response) => {
        this.users = response.data;
      })
      .catch((error) => console.log(error));
      */
  // VUEX 사용-> actions 가져오려면 dispatch 사용(vuex 사이클 그림 참고)

  // bus.$emit("start:spinner"); // create되면 바로 스피너 호출
  // setTimeout(()=> {
  // this.$store.dispatch("FETCH_NEWS") // 데이터 요청
  //   .then(() => { // actions.js에 fetchNewsList반환값이 promise라서 then 게속 사용 가능
  //     bus.$emit("end:spinner");
  //   })
  //   .catch((error) => {
  //     console.log(error);
  //   });
  // }, 1000);  // 타임아웃 걸어서 3초뒤에 받아옴
  //},
};
</script>

<style></style>
