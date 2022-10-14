<template>
  <div>
    <h1>메인 페이지</h1>
  </div>
</template>

<script>
import axios from 'axios'
export default {
    async created() {
      const response = await axios.get('http://localhost:3000/products')
      console.log(response)
    }
}

</script>

<style></style>
