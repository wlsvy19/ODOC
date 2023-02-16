<template>
  <div>
    <fetch-data url="https://jsonplaceholder.typicode.com/users/1">
      <!-- 로직 -->
      <div slot-scope="{ response, loading }">
        <div v-if="!loading">
          {{ response.name }},
          {{ response.email }}
        </div>
        <div v-if="loading">로딩중...</div>
      </div>
    </fetch-data>
  </div>
</template>

<script>
//import axios from 'axios';
import FetchData from "./components/FetchData.vue";

export default {
  components: {
    FetchData,
  },
  // 원래 대로라면 여기서 데이터 불러오는 로직 처리 -> 데이터 패치 로직(불러오는 로직)
  // data() {
  //   return {
  //     response: null,
  //     loading: true,
  //   }
  // },
  // created() {
  //   axios.get(this.url)
  //     .then(response => {
  //       this.response = response.data;
  //       this.loading = false;
  //     })
  //     .catch(error => {
  //       alert('[ERROR] fetching the data', error);
  //       console.log(error);
  //     });
  // },
};
</script>