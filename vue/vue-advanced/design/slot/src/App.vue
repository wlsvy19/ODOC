<!-- eslint-disable vue/valid-v-for -->
<template>
  <div>
    <ul>
      <!-- <Item v-for="item in items" :propsdata="item"></Item> -->
      <ItemVue v-for="item in items">{{ item }}</ItemVue>
    </ul>

    <ItemVue>
      ItemVue1
    </ItemVue>
    <ItemVue>
      ItemVue2
    </ItemVue>
    <ItemVue style="color: blue; font-size:20px;">
      슬롯은 스타일도 지정 가능
    </ItemVue>
    <item-vue>
      <img src = "./assets/logo.png" alt="alt" width="50px" height="50px"/>
    </item-vue>
    <item-vue>
      <button> 버튼 추가 가능</button>
    </item-vue>
  </div>
</template>

<script>
import ItemVue from "./Item.vue";

export default {
  components: {
    ItemVue,
  },
  data() {
    return {
      items: ["아이템1", "아이템2", "아이템3", "아이템4", "아이템5"],
    };
  },
};
</script>