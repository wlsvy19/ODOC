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
        <button type="button" @click="addToCart">장바구니 담기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchProductById, createCartItem } from '@/api/index'
/**
 * ! 이 파일은 다이나믹 라우팅이 적용된 파일입니다.
 * ! _id.vue: _는 라우터의 파라미터, id는 파라미터로 넘길 값 -> http://localhost:4000/detail/2
 */
export default {
  // this.$route.params => {params}
  async asyncData({ params }) {
    console.log('_id.vue-> asyncData() params', params)
    const response = await fetchProductById(params.id)
    const product = response.data

    // 위에 HTML 템플릿에서 바로 사용 가능
    return { product }
  },

  // head(): 페이지 별 메타 태그 설정 - 노출될때 보여지는 내용
  // head를 function으로 하면 값 가져올 수 있음
  head() {
    return {
      title: `Shopping Item Detail - ${this.product.name}`,
      meta: [
        {
          // nuxt.config.js 의 hid값과 똑같아야 페이지별로 description 태그 한개임
          hid: 'description',
          name: 'description',
          content: `이 상품은 ${this.product.name} 입니다.`,
        },
        // OG태그 사용
        // 카톡 OG태그 캐시 비우기: https://developers.kakao.com/tool/debugger/sharing
        {
          hid: 'og:title',
          property: 'og:title',
          content: '상품 상세 페이지',
        },
        {
          hid: 'og:description',
          property: 'og:description',
          content: '상품의 상세 정보를 확인해보세요',
        },
        {
          hid: 'og:image',
          property: 'og:image',
          content: 'http://placeimg.com/640/480/fashion',
        },
      ],
      link: [
        {
          rel: 'stylesheet',
          href: '#',
        },
      ],
    }
  },

  created() {
    // console.log('created: ', this.$route.params)
  },
  methods: {
    async addToCart() {
      const response = await createCartItem(this.product)
      console.log('장바구니담기 response: ', response)

      // commit: store/index.js mutations 호출
      // vue cli생성 시 new Vue({store: store, router: router}) 이 구조가 nuxt에서 자동
      // store 파일밑에 만든 js 파일 자동 인식
      this.$store.commit('addCartItem', this.product)

      // pages폴더 car.vue생성->.nuxt에 router.js 에 cart path 자동 라우팅
      this.$router.push('/cart')
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
