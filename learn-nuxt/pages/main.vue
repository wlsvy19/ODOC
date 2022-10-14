<template>
  <div>
    <h1>메인 페이지 입니다!!!!</h1>
    <br />
    <div>
      <ul>
        <li v-for="product in products" :key="product.id">
          <img :src="product.imageUrl" :alt="product.name" />
          <p>● {{ product.name }}</p>
          <p>{{ product.price }}</p>
          <br />
        </li>
      </ul>
    </div>
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
    console.log(products)
    return { products }
  },
}
</script>

<style></style>
