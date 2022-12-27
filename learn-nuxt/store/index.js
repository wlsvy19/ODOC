export const state = () => ({
  cartItems: [],
})

// 카트에 담으면 mutation이 호출 되면서 state에 저장, commit으로 받음
export const mutations = {
  addCartItem(state, cartItem) {
    const newCartItem = {
      ...cartItem,
      // 카트에 담긴 이미지 다르게 나오게 하기 위함
      imageUrl: `${cartItem.imageUrl}?random=${Math.random()}`,
    }
    state.cartItems.push(newCartItem)
  },
}
