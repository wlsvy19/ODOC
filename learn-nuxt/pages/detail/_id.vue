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
import { fetchProductById } from '~/api'
export default {
  //   created() {
  //     const id = console.log(this.$route.params.id)
  //     fetchProductById();
  //   },

  // create말고 nuxt에서 제공하는 asyncData 사용
  // asyncData 속성은 페이지 진입하기 전에 호출되는 로직이라서(컴포넌트 그려지기전) this사용x
  // -> router에서 제공하는 parmas 인자 받아 사용
  // asyncData가 서버에서 데이터 받아옴
  async asyncData({ params }) {
    // const id = console.log(this.$route.params.id)
    const response = await fetchProductById(params.id)
    const product = response.data
    return { product } //
  },
  methods: {
    addToCart() {
      // pages폴더 car.vue생성->.nuxt에 router.js 에 cart path 자동 라우팅
      this.$router.push('/cart')
      // mutation 호출
      // vue cli생성 시 new Vue({store: '', router: ''}) 이 구조가 nuxt에서 자동
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
