<template>
  <div style="display: flex; vertical-align: center">
    <v-label style="margin: 0px 10px 0px 10px">
      <v-icon :icon="computedIconType" style="margin-right: 5px" />
      <span>{{ message }}</span>
    </v-label>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  iconType: {
    type: String,
    default: 'information',
  },
  message: {
    type: String,
    required: false,
    default: '',
  },
});

const computedIconType = computed(() => {
  if (props.iconType === 'attention') {
    return 'mdi-alert-rhombus-outline';
  } else if (props.iconType === 'information') {
    return 'mdi-information-variant-circle-outline';
  } else if (props.iconType === 'pin') {
    return 'mdi-pin';
  } else if (props.iconType === 'location') {
    return 'mdi-map-marker';
  } else if (props.iconType === 'bell') {
    return 'mdi-bell';
  }
  return props.iconType;
});
</script>
