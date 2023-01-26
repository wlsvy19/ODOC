import { VueConstructor } from "vue";

// 반복되는 ref 속성들을 사용하기 위한 파일
type MyVue<T> = VueConstructor<Vue & T>;
export type MyVueRefs<T> = VueConstructor<Vue & { $refs: T }>;
