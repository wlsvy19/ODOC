<!-- eslint-disable vue/v-on-style -->
<template>
  <div>
    <button v-on:click="emitTest">Emit Test Button(하->상)</button>
  </div>
</template>

<script>
export default {
  methods: {
    emitTest() {
      this.$emit('myEmitTest', '자식: Data 보내요~')
    },
  },
}
</script>

<style></style>
