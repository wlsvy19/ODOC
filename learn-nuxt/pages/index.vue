<template>
  <div class="app">
    <main>
      <div>
        <input type="text" />
      </div>
    </main>
    <ul>
      <li
        v-for="product in products"
        :key="product.id"
        class="item flex"
        @click="moveToDetailPage(product.id)"
      >
        <img
          class="product-image"
          :src="product.imageUrl"
          :alt="product.name"
        />
        <p>{{ product.name }}</p>
        <span>{{ product.price }}$</span>
        <br />
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  // asyncData는 pages폴더 안에 정의한 컴포넌트들에만 적용
  async asyncData() {
    const response = await axios.get('http://localhost:3000/products')
    // asyncData는 this.products로 접근 불가->const 변수 선언 후 사용
    const products = response.data.map((item) => ({
      ...item,
      imageUrl: `${item.imageUrl}?random=${Math.random()}`,
    }))
    return { products }
  },
  methods: {
    moveToDetailPage(id) {
      console.log(id)
      // pages폴더 _id.vue -> _는 router의 파라미터, id는 넘길 id
      this.$router.push(`detail/${id}`) //  detail은 detail폴더 의미
    },
  },
}
</script>

<style scoped>
.flex {
  display: flex;
  justify-content: center;
}
.item {
  display: inline-block;
  width: 400px;
  height: 300px;
  text-align: center;
  margin: 0 0.5rem;
  cursor: pointer;
}
.product-image {
  width: 400px;
  height: 250px;
}
.app {
  position: relative;
}
.cart-wrapper {
  position: sticky;
  float: right;
  bottom: 50px;
  right: 50px;
}
.cart-wrapper .btn {
  display: inline-block;
  height: 40px;
  font-size: 1rem;
  font-weight: 500;
}
</style>
