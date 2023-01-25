import Vue from "vue";
import { MyStore } from "@/store/types";

// declare: 모듈 확장

// NOTE: `node_module/vuex/types/vue.d.ts` 파일을 삭제 해줘야 아래 타입이 정상 추론 됨
declare module "vue/types/vue" {
  interface Vue {
    $store: MyStore;
  }
}

declare module "vue/types/options" {
  interface ComponentOptions<V extends Vue> {
    store?: MyStore;
  }
}
