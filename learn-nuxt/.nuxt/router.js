import Vue from 'vue'
import Router from 'vue-router'
import { normalizeURL, decode } from 'ufo'
import { interopDefault } from './utils'
import scrollBehavior from './router.scrollBehavior.js'

const _79f3da58 = () => interopDefault(import('..\\pages\\board.vue' /* webpackChunkName: "pages/board" */))
const _d3d132bc = () => interopDefault(import('..\\pages\\cart.vue' /* webpackChunkName: "pages/cart" */))
const _876dcc52 = () => interopDefault(import('..\\pages\\login.vue' /* webpackChunkName: "pages/login" */))
const _d59a068a = () => interopDefault(import('..\\pages\\main.vue' /* webpackChunkName: "pages/main" */))
const _661706dd = () => interopDefault(import('..\\pages\\product.vue' /* webpackChunkName: "pages/product" */))
const _85b1c8b6 = () => interopDefault(import('..\\pages\\register.vue' /* webpackChunkName: "pages/register" */))
const _5103aa77 = () => interopDefault(import('..\\pages\\detail\\_id.vue' /* webpackChunkName: "pages/detail/_id" */))
const _1abd1280 = () => interopDefault(import('..\\pages\\index.vue' /* webpackChunkName: "pages/index" */))

const emptyFn = () => {}

Vue.use(Router)

export const routerOptions = {
  mode: 'history',
  base: '/',
  linkActiveClass: 'nuxt-link-active',
  linkExactActiveClass: 'nuxt-link-exact-active',
  scrollBehavior,

  routes: [{
    path: "/board",
    component: _79f3da58,
    name: "board"
  }, {
    path: "/cart",
    component: _d3d132bc,
    name: "cart"
  }, {
    path: "/login",
    component: _876dcc52,
    name: "login"
  }, {
    path: "/main",
    component: _d59a068a,
    name: "main"
  }, {
    path: "/product",
    component: _661706dd,
    name: "product"
  }, {
    path: "/register",
    component: _85b1c8b6,
    name: "register"
  }, {
    path: "/detail/:id?",
    component: _5103aa77,
    name: "detail-id"
  }, {
    path: "/",
    component: _1abd1280,
    name: "index"
  }],

  fallback: false
}

export function createRouter (ssrContext, config) {
  const base = (config._app && config._app.basePath) || routerOptions.base
  const router = new Router({ ...routerOptions, base  })

  // TODO: remove in Nuxt 3
  const originalPush = router.push
  router.push = function push (location, onComplete = emptyFn, onAbort) {
    return originalPush.call(this, location, onComplete, onAbort)
  }

  const resolve = router.resolve.bind(router)
  router.resolve = (to, current, append) => {
    if (typeof to === 'string') {
      to = normalizeURL(to)
    }
    return resolve(to, current, append)
  }

  return router
}
