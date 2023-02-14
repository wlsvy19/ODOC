import { fetchCartItems } from '~/api'

// state: 여러 컴포넌트 간에 공유되는 데이터
export const state = () => ({
  cartItems: [],
})

// mutation: commit으로 호출 됨 -> state의 상태 변경
// 카트에 담으면 mutation이 호출 되면서 state에 저장
export const mutations = {
  addCartItem(state, cartItem) {
    // console.log('cartItem: ', cartItem)
    const newCartItem = {
      // cartItem은 Spread Operator로 그대로 쓰고
      ...cartItem,

      // imageUrl은 새로 정의
      // 카트에 담긴 이미지 다르게 나오게 하기 위함
      imageUrl: `${cartItem.imageUrl}?random=${Math.random()}`,
    }
    // console.log('newCartItem:', newCartItem)
    // state에 있는 carItems 배열에 추가
    state.cartItems.push(newCartItem)
  },
  setCartItems(state, cartItems) {
    state.cartItems = cartItems
  },
}

// ES6+ 문법: 동적키값 정의 방식 -> 변수로 관리, [] 로 사용
export const FETCH_CART_ITEMS = 'FETCH_CART_ITEMS'

export const actions = {
  // {commit} -> context.commit
  async [FETCH_CART_ITEMS]({ commit }) {
    // api/index.js 함수 import
    const { data } = await fetchCartItems()
    console.log('actions의 FETCH_CART_ITEMS 결과: ', data)

    // commit -> mutations 호출
    commit(
      'setCartItems',
      data.map((item) => ({
        ...item,
        imageUrl: `${item.imageUrl}?random=${Math.random()}`,
      }))
    )
  },

  // https://joshua1988.github.io/vue-camp/nuxt/store.html#nuxtserverinit
  // nuxtServerInit: SSR, 자동으로 스토어에 미리 데이터 불러옴
  async nuxtServerInit(storeContext, nuxtContext) {
    /* 
    const { data } = await fetchCartItems()
    storeContext.commit(
      'setCartItems',
      data.map((item) => ({
        ...item,
        imageUrl: `${item.imageUrl}?random=${Math.random()}`,
      }))
    )
    */
    // 위 코드와 동일
    // await storeContext.dispatch(FETCH_CART_ITEMS)
  },
}
