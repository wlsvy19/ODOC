<template>
  <div class="input-wrapper flex">
    <!--value와 input을 엮으면 v-model과 동일, 
  input tage의 값들이 바뀔 때 마다(target) input 이벤트를 상위컴포넌트로 올림-->
    <input
      class="search-input"
      type="text"
      :value="value"
      @input="$emit('input', $event.target.value)"
      @keyup.enter="$emit('search')"
    />
    <button class="btn" type="button" @click="$emit('search')">search</button>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: () => '', // 아무것도 입력 안하면 기본 문자열로 들어오게 설정
    },
  },
}
</script>

<style scoped>
.input-wrapper {
  height: 40px;
  margin: 1rem 0;
}
.search-input {
  width: 200px;
  font-size: 1.2rem;
  font-weight: 500;
}
.btn {
  font-size: 1.2rem;
  font-weight: 500;
}
</style>
