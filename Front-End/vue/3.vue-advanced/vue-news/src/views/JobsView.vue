<template>
  <div>
    <ListItem />
  </div>
</template>

<script>
import ListItem from "../components/ListItem.vue";
import ListMixin from "../mixins/ListMixin.js";

export default {
  components: {
    ListItem,
  },
  mixins: [ListMixin], // -> ListMixin 내용이 그대로 들어감
};
</script>

<style>
</style>