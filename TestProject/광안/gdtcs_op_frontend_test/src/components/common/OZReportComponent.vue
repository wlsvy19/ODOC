<template>
  <div v-resize="onResize">
    <div ref="resizableDiv">
      <form ref="reportForm" :action="ozViewerUrl" method="POST" target="OZTest">
        <input :value="getData.length" type="hidden" name="IDX" />
        <template v-for="(data, idx) in getData" :key="idx">
          <input :value="data" type="hidden" :name="`data${idx}`" />
        </template>
      </form>
      <iframe :style="{ width: '100%', height: `${iframeHeight}`, border: '0px', display: 'block' }" name="OZTest"></iframe>
    </div>
  </div>
</template>

<script setup>
import { ref, onActivated, onUpdated, computed } from 'vue';
import { ozUrl } from '@/stores/index';

const getData = computed(() => {
  const submitData = [];
  const length = props.jsonData.length;
  const MAX_LEN = 150000;
  const idx = Math.ceil(length / MAX_LEN);

  for (var i = 0; i < idx; i++) submitData.push(props.jsonData.slice(MAX_LEN * i, MAX_LEN * (i + 1)));

  return submitData;
});

const props = defineProps({
  jsonData: {
    type: String,
    required: true,
  },
  iframeHeight: {
    type: String,
    default: '',
  },
});

onUpdated(() => {
  reportForm.value.submit();
});
onActivated(() => {
  onResize();
  reportForm.value.submit();
});

const ozViewerUrl = `${ozUrl}viewer.jsp`;

const resizableDiv = ref();
const reportForm = ref();
const iframeHeight = ref();

const onResize = () => {
  iframeHeight.value = props.iframeHeight ? props.iframeHeight : `calc(100vh - ${228}px)`;
};
</script>

<style scoped></style>
