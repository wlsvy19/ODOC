<template>
  <header>
    <h1>{{ title }}</h1>
  </header>
</template>

<script>
export default {
  props: {
    // rpops: ['title'],
    title: String, // 유효성 검사를 위해 타입 지정
  }
}
</script>