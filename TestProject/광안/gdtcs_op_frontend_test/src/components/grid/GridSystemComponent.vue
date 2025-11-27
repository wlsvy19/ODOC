<template>
  <table :style="{ height: `${tableHeight}` }">
    <tbody>
      <tr v-for="row in Math.ceil(headers.length / colsPerRow)" :key="row" :style="{ height: `${rowHeight}` }">
        <template v-for="col in colsPerRow * 2" :key="col">
          <th v-if="col % 2 == 1 && (row - 1) * colsPerRow + col / 2 < headers.length" :style="{ width: `${tableThWidth}` }">
            {{ headers[parseInt((row - 1) * colsPerRow + col / 2)].title }}
          </th>
          <template v-else-if="row == Math.ceil(headers.length / colsPerRow) && col > (colsPerRow - (row * colsPerRow - headers.length)) * 2">
          </template>
          <td
            :colspan="(row * colsPerRow - headers.length) * 2 + 1"
            v-else-if="row == Math.ceil(headers.length / colsPerRow) && col == (colsPerRow - (row * colsPerRow - headers.length)) * 2"
          >
            {{ contents[headers[(row - 1) * colsPerRow + col / 2 - 1].key] }}
          </td>
          <td v-else>
            {{ contents[headers[(row - 1) * colsPerRow + col / 2 - 1].key] }}
          </td>
        </template>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
const props = defineProps({
  headers: {
    type: Array,
    required: true,
  },
  tableThWidth: {
    type: String,
    required: true,
  },
  contents: {
    type: Object,
    required: true,
  },
  colsPerRow: {
    type: Number,
    required: true,
  },
  tableHeight: {
    type: String,
    required: false,
    default: '100%',
  },
  rowHeight: {
    type: String,
    required: false,
    default: '18px',
  },
});
</script>

<style scoped>
table {
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
  border-spacing: 0;
  font-size: 12px;
}
th {
  border: 1px solid #b0b0b0;
  background-color: #f5f5f5;
  padding: 2px;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
td {
  border: 1px solid #b0b0b0;
  text-align: center;
  white-space: nowrap;
  overflow-x: auto;
}
td::-webkit-scrollbar {
  display: none;
}
.custom-width th {
  width: 12%;
}
</style>
