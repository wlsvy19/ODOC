<!-- eslint-disable vue/require-v-for-key -->
<template>
  <div>
    <ul>
      <li v-for="item in items">
        {{ item }}
      </li>
    </ul>
    <button @click="$emit('renew')">renew items</button>
  </div>
</template>

<script>
export default {
  props: {
    items: {
      // 타입 지정
      type: Array,
      required: true,
    },
  },
  methods: {
    // -> 버튼 클릭시 바로 이벤트 emit 상위 컴포넌트로 보내기 가능
    // sendRenew() {
    //   this.$emit('renew');
    // }
  }
}
</script>