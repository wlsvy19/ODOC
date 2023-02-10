<template>
  <div>
    <AppHeader />
    <!-- <Nuxt>: page폴더 안에 루트 / 라우터 -> index.vue -->
    <Nuxt />
    <AppFooter />
  </div>
</template>

<script>
import AppHeader from '@/layouts/AppHeader.vue'
import AppFooter from '@/layouts/AppFooter.vue'
export default {
  components: { AppHeader, AppFooter },
}
</script>

<style></style>
