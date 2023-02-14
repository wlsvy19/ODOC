<template>
  <div class="container">
    <h1 class="list-title">카트페이지</h1>
    <CartList></CartList>
    <div>
      <div class="extra-panel">
        <button>구매하기</button>
      </div>
    </div>
  </div>
</template>

<script>
import CartList from '~/components/CartList.vue'
// import { FETCH_CART_ITEMS } from '@/store/index'

export default {
  components: { CartList },
  /**
   * ! 장바구니에 담은 아이템이 보이는 페이지
   */
  // nuxtServerInit 으로 SSR 구현 ->  아래 코드 생략
  /*
  async asyncData({ store }) {
    // store/index.js 에서 actions 호출
    await store.dispatch(FETCH_CART_ITEMS)
  }, // -> nuxtServerInit 호출로 스토어에 데이터 미리 불러옴
  */
}
</script>

<style scoped>
.container {
  margin: 2rem 10rem;
}
.list-title {
  font-weight: 700;
  font-size: 1.4rem;
}
.list-wrapper {
  margin: 0.4rem 0;
}
.list-item {
  display: flex;
}
.thumbnail {
  width: 100px;
  height: 100px;
}
.description {
  padding: 2rem 1rem;
}
.extra-panel {
  text-align: right;
  padding: 0.2rem 0;
}
</style>
