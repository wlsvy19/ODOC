// 테스트 유틸 라이브러리 로딩
import { shallowMount } from '@vue/test-utils';
// 컴포넌트 로딩
import SignupForm from './SignupForm.vue';

describe('SignupForm.vue 테스트', () => {
  test('회원가입 테스트', () => {
    // 내용 정의
    const wrapper = shallowMount(SignupForm);
    console.log(wrapper);
  });
});
