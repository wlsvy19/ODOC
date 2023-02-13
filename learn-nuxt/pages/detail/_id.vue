<template>
  <div>
    <div class="container">
      <div class="main-panel">
        <img
          class="product-image"
          :src="product.imageUrl"
          :alt="product.name"
        />
      </div>
      <div class="side-panel">
        <p class="name">{{ product.name }}</p>
        <p class="price">{{ product.price }}$</p>
        <button type="button" @click="addToCart">Add to Cart</button>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchProductById } from '@/api/index'
/**
 * ! 이 파일은 다이나믹 라우팅이 적용된 파일입니다.
 * ! _id.vue: _는 라우터의 파라미터, id는 파라미터로 넘길 값 -> http://localhost:4000/detail/2
 */
export default {
  // this.$route.params = {params}
  async asyncData({ params }) {
    console.log('_id.vue-> asyncData() params', params)
    const response = await fetchProductById(params.id)
    console.log('response:', response)
    const product = response.data

    // 위에 HTML 템플릿에서 바로 사용 가능
    return { product }
  },
  created() {
    // console.log('created: ', this.$route.params)
  },
  methods: {
    addToCart() {
      // pages폴더 car.vue생성->.nuxt에 router.js 에 cart path 자동 라우팅
      this.$router.push('/cart')

      // commit: store/index.js mutations 호출
      // vue cli생성 시 new Vue({store: store, router: router}) 이 구조가 nuxt에서 자동
      // store 파일밑에 만든 js 파일 자동 인식
      this.$store.commit('addCartItem', this.product)
    },
  },
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  margin: 2rem 0;
}
.product-image {
  width: 500px;
  height: 375px;
}
.side-panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 220px;
  text-align: center;
  padding: 0 1rem;
}
</style>
