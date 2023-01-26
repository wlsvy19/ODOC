<template>
  <div>
    {{ products }}
  </div>
</template>

<script>
import axios from "axios";

export default {
  // asyncData는 pages안에 파일들만 유효
  async asyncData() {
    const response = await axios.get('http://localhost:3000/products')
    const products = response.data
    console.log(products)
    return { products }
  },
}
</script>

<style></style>
