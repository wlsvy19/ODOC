import { wrapFunctional } from './utils'

export { default as Logo } from '../..\\components\\Logo.vue'
export { default as ProductList } from '../..\\components\\ProductList.vue'
export { default as SearchInput } from '../..\\components\\SearchInput.vue'

export const LazyLogo = import('../..\\components\\Logo.vue' /* webpackChunkName: "components/logo" */).then(c => wrapFunctional(c.default || c))
export const LazyProductList = import('../..\\components\\ProductList.vue' /* webpackChunkName: "components/product-list" */).then(c => wrapFunctional(c.default || c))
export const LazySearchInput = import('../..\\components\\SearchInput.vue' /* webpackChunkName: "components/search-input" */).then(c => wrapFunctional(c.default || c))
