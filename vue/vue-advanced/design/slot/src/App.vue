<!-- eslint-disable vue/valid-v-for -->
<template>
  <div>

    <!--
    <ul>
      <ItemVue1 v-for="item in items">{{ item }}</ItemVue1>
    </ul>
    -->

    <!-- <ItemVue1>
      ItemVue1
    </ItemVue1>
    <ItemVue1>
      ItemVue2
    </ItemVue1>
    <ItemVue1 style="color: blue; font-size:20px;">
      슬롯은 스타일도 지정 가능
    </ItemVue1>
    <item-vue1>
      이미지 넣어주세요
      <img src = "./assets/logo.png" alt="alt" width="50px" height="50px"/>
    </item-vue1>
    <item-vue1>
      <button> 버튼넣고 클릭하게 해주세요</button>
    </item-vue1> -->

    <ItemVue>
      <template v-slot:first>
        <h1>App.vue....</h1>
      </template>  
    </ItemVue>
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