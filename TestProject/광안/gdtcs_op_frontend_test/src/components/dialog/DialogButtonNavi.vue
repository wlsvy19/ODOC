<template>
  <v-row>
    <v-spacer></v-spacer>
    <v-btn class="dialog-button-nvi" variant="tonal" color="black darken-1" :disabled="isFirst" text @click="$emit('btnClickEventBefore')"
      >이전</v-btn
    >
    <v-btn class="dialog-button-nvi" variant="tonal" color="black darken-1" :disabled="isLast" text @click="$emit('btnClickEventAfter')">다음</v-btn>
  </v-row>
</template>

<script setup>
const props = defineProps({
  isFirst: { type: Boolean, required: false },
  isLast: { type: Boolean, required: false },
});
</script>

<style scoped>
.dialog-button-nvi {
  margin: 0.2em;
}
</style>
