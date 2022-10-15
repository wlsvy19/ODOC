<template>
  <div>
    <h1>상세 페이지</h1>
    <div>
      <!--alt: 이미지표시 안될 때 대체할거-->
      <img :src="product.imageUrl" :alt="product.name" />
      <p>name: {{ product.name }}</p>
      <p>price: {{ product.price }}</p>
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
}
</script>

<style></style>
