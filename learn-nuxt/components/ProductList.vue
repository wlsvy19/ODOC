<template>
  <div>
    {{ products }}
  </div>
</template>

<script>
import axios from 'axios'

export default {
  // asyncData는 pages 폴더 안에 파일들만 유효 -> 여기는 components 폴더라서 인식 못함
  async asyncData() {
    const response = await axios.get('http://localhost:3000/products')
    const products = response.data
    console.log('products: ', products)
    return { products }
  },
}
</script>

<style></style>
