<!-- eslint-disable vue/v-bind-style -->
<!-- eslint-disable vue/v-on-style -->
<!-- eslint-disable vue/attribute-hyphenation -->
<template>
  <div class="app">
    <!-- 시맨틱 마크업 사용(div ㄴㄴ) -->
    <main>
      <SearchInput
        v-model="searchKeyword"
        v-bind:parentsData="searchKeyword"
        v-on:input="searchProducts"
        v-on:search="searchProducts"
      ></SearchInput>
      <EmitTest v-on:myEmitTest="parentEmit"></EmitTest>
    </main>

    <ul>
      <li
        v-for="product in products"
        :key="product.id"
        class="item flex"
        @click="routeToProductDetailPage(product.id)"
      >
        <img
          class="product-image"
          v-bind:src="product.imageUrl"
          v-bind:alt="product.name"
        />
        <p>{{ product.name }}</p>
        <span>{{ product.price }}$</span>
        <br />
      </li>
    </ul>
    <div class="cart-wrapper">
      <button class="btn" v-on:click="moveToCartPage">장바구니 바로가기</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import SearchInput from '../components/SearchInput.vue'
// import fetchProductsByKeyword from '@/api/index'
import { fetchProductsByKeyword } from '@/api/index'
import EmitTest from '~/components/EmitTest.vue'

/**
 * ! 이 파일은 루트 URL 로 라우터에 자동 등록
 */

export default {
  components: { SearchInput, EmitTest },

  // Nuxt의 비동기 데이터 호출 방법
  // https://joshua1988.github.io/vue-camp/nuxt/data-fetching.html#asyncdata
  // asyncData: pages폴더 안에 정의한 파일에서만 사용 가능, async 키워드 있어야 함
  // 컴포넌트 그려지기 전이라 data의 this 사용 못함
  // vue의 네비게이션 가드에서 비동기 데이터가 다 불러져 와야 next() 호출 하는 것과 비슷
  async asyncData() {
    const response = await axios.get('http://localhost:3000/products')
    // console.log('응답 데이터: ', response)
    // asyncData는 this.products로 접근 불가->const 변수 선언 후 사용
    const resProducts = response.data.map((item) => ({
      // Spread Operator: response.data의 속성들 (id, name, price 등...)을 item에 키:값 형태로 넣음
      ...item,
      // imageUrl을 랜덤 으로 설정, http://placeimg.com/640/480/tech?random=0.6045700411882764
      imageUrl: `${item.imageUrl}?random=${Math.random()}`,
    }))
    // console.log('resProducts:', resProducts)
    // ES6 객체 축약 문법, 리턴 하자마자 vue 인스턴스로 사용 가능-> v-for와 searchProducts()에서 사용
    return { products: resProducts }
  }, // end asyncData

  data() {
    return {
      searchKeyword: '',
    }
  }, // end data

  methods: {
    /**
     * * 클릭한 사진의 상세페이지로 이동 하는 메서드
     * @param {*} id: 클릭한 사진 id
     */
    routeToProductDetailPage(id) {
      console.log('클릭한 사진 id: ' + id)

      // this.$router 정의 안했는데? -> Nuxt에서 라우터 기본 제공
      // pages폴더 _id.vue -> _는 router의 파라미터, id는 넘길 id
      // detail: detail 폴더를 의미(파라미터)
      // ``는 템플릿 리터럴->백틱 문자 사용
      this.$router.push(`detail/${id}`)
    }, // end routeToProductDetailPage()

    /**
     * * 특정 키워드를 찾는 메서드
     */
    async searchProducts() {
      const response = await fetchProductsByKeyword(this.searchKeyword)
      // asyncData의 return 값
      this.products = response.data.map((item) => ({
        ...item,
        imageUrl: `${item.imageUrl}?random=${Math.random()}`,
      }))
    }, // end searchProducts()

    moveToCartPage() {
      this.$router.push('/cart')
    }, // end moveToCartPage()

    parentEmit(data) {
      alert(`부모:Data 받아요!! -> ${data}`)
      console.log('부모: Data 받아요~! -> ', data)
    }, // end parentEmit()
  }, // end methods
}
</script>

<!-- 복붙 https://github.com/joshua1988/learn-nuxt/blob/master/pages/index.vue -->
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
