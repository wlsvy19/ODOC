<!-- 단순 컴포넌트 등록만 함 중복데이터-> CreateListView.js 에서 처리-->

<template>
  <!--New, Ask, Job 화면을 한번에 뿌려줌-->
  <list-item></list-item>
</template>

<script>
import ListItem from "../components/ListItem.vue";
export default {
  components: {
    ListItem,
  },
};
</script>

<style>
</style>