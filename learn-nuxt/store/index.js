// state: 여러 컴포넌트 간에 공유되는 데이터
export const state = () => ({
  cartItems: [],
})

// mutation: commit으로 호출 됨 -> state에 저장
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
}
