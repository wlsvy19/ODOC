<!-- eslint-disable vue/v-on-style -->
<!-- eslint-disable vue/v-bind-style -->
<template>
  <div class="input-wrapper flex">
    <div>
      <!--value와 input을 엮으면 v-model과 동일, 
  !!! $emit('input'): 아래 [input 태그]의 값들이 바뀔 때 마다 input 이벤트를 상위컴포넌트로 올림-->
      <!-- v-on:input : 사용자가 적은 내용을 특정 이벤트 없이 즉각 반영-->
      <input
        class="search-input"
        type="text"
        v-bind:value="parentsData"
        v-on:input="$emit('input', $event.target.value)"
        v-on:keyup.enter="$emit('search')"
      />
      <button class="btn" type="button" @click="$emit('search')">찾기</button>
    </div>

    <div style="margin-left: 40px">
      <span>Props Test(상->하): </span>
      <span style="color: crimson; font-weight: 200">[{{ parentsData }}]</span>
    </div>
  </div>
</template>

<script>
export default {
  // props: 상위 컴포넌트 -> 하위(여기) 컴포넌트 데이터 전달
  props: {
    parentsData: {
      type: String,
      // 아무것도 입력 안하면 기본 문자열로 들어오게 설정
      default: () => '',
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
