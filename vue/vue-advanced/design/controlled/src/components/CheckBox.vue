<template>
  <!-- input에는 v-model -->
  <input type="checkbox" :value="value" @click="toggleCheckBox" />
</template>

<script>
export default {
  props: ["value"],
  methods: {
    toggleCheckBox() {
      this.$emit("input", !this.value);
    },
  },
};
</script>