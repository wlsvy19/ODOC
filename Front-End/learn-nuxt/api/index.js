import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:3000',
})

/**
 *
 * @param {*} id 물건 ID
 * @returns 이미지 클릭 시 ID에 해당하는 물건 상세 정보
 */
function fetchProductById(id) {
  return instance.get(`/products/${id}`)
}

/**
 *
 * @param {*} keyword 찾기 input에 들어가는 키워드
 * @returns 서치 결과
 */
function fetchProductsByKeyword(keyword) {
  return instance.get(`/products`, {
    params: {
      name_like: keyword,
    },
  })
}

/**
 * * 장바구니 담기
 * * 페이크 API인 http://localhost:3000/carts POST로 호출-> backend - db.json에 저장
 * @param {} cartItem 장바구니에 담을 아이템
 * @returns
 */
function createCartItem(cartItem) {
  return instance.post('/carts', cartItem)
}

/**
 * * 장바구니 담은 아이템 가져오기
 * * stroe의 actions에서 호출 -> fetchCartItems
 */
function fetchCartItems() {
  return instance.get('/carts')
}

export {
  fetchProductById,
  fetchProductsByKeyword,
  createCartItem,
  fetchCartItems,
}
