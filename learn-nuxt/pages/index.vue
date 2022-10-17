<template>
  <div class="app">
    <main>
      <!--@input은 SearchInput컴포넌트에서 보낸 이벤트 받음-->
      <!--:search-keyword="searchKeyword" @input="updateSearchKeyword"-->
      <SearchInput
        v-model="searchKeyword"
        @search="searchProducts"
      ></SearchInput>
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
    <div class="cart-wrapper">
      <button class="btn" @click="moveToCartPage">장바구니 바로가기</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import SearchInput from '../components/SearchInput.vue'
// import fetchProductsByKeyword from '@/api/index'
import { fetchProductsByKeyword } from '@/api/index'

export default {
  components: { SearchInput },
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
      console.log('id: ' + id)
      // pages폴더 _id.vue -> _는 router의 파라미터, id는 넘길 id
      this.$router.push(`detail/${id}`) //  detail은 detail폴더 의미
    },
    async searchProducts() {
      const response = await fetchProductsByKeyword(this.searchKeyword)
      console.log(response)
      this.products = response.data.map((item) => ({
        ...item,
        imageUrl: `${item.imageUrl}?random=${Math.random()}`,
      }))
    },
    moveToCartPage() {
      this.$router.push('/cart')
    },
  },
  data() {
    return {
      searchKeyword: '',
    }
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
  /* sticky: 스크롤 이동해도 고정 */
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
