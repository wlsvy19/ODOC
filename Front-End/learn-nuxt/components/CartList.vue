<template>
  <div class="list-wrapper">
    <ul>
      <li
        v-for="cartItem in $store.state.cartItems"
        :key="cartItem.id"
        class="list-item"
      >
        <img class="thumbnail" :src="cartItem.imageUrl" :alt="cartItem.name" />
        <div class="description">
          <p>{{ cartItem.name }}</p>
          <span>{{ cartItem.price }}$</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { FETCH_CART_ITEMS } from '@/store/index'

export default {
  // asyncData(): pages폴더 에서만 사용가능 하므로 사용x
  // -> cart.vue에서 CartList 컴포넌트로 사용하면서 생긴 문제 -> fetch() 사용
  // asyncData 에서는 this 사용 못함 -> 화면 그려지기 전 호출되는 로직 이기 때문
  /*
  async asyncData({ store }) {
    await store.dispatch(FETCH_CART_ITEMS)
  },
  */

  // fetch(): 일반 뷰 컴포넌트에서 사용 가능, 화면 그려지고 호출
  // this
  // 1. 루트 / 에서 cart 페이지 이동 -> this는 VueComponent 가리킴(루트에서 컴포넌트로 화면을 이미 그림 -> SPA)
  // 2. cart에서 새로고침 -> Nuxt SSR(서버에서 화면 그림)
  async fetch() {
    console.log('this: ', this)
    await this.$store.dispatch(FETCH_CART_ITEMS)
  },
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
