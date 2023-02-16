import { shallowMount } from '@vue/test-utils';
// import Vue from 'vue';
import LoginForm from './LoginForm.vue';

describe('LoginForm.vue 테스트1', () => {
  // test: 테스트 단위
  test('컴포넌트가 mounted 되면 username이 존재하고 빈 값으로 설정 되어 있어야 함', () => {
    // const instance = new Vue(LoginForm).$mount();

    // shallowMount: new Vue~~ 줄임
    const wrapper = shallowMount(LoginForm);

    // wrapper.vm: Vue의 인스턴스들
    // console.log(wrapper.vm);

    // 처음 username은 빈 값임
    expect(wrapper.vm.username).toBe('');
  });
});

describe('LoginForm.vue 테스트2', () => {
  test('ID가 이메일 형식이 아니면 warning 문구 나타나는지 테스트', () => {
    // shallowMount: 2번째 인자로 vue에서 제공하는 data를 세팅할 수 있음
    const wrapper = shallowMount(LoginForm, {
      data() {
        return {
          username: 'test',
          //username: 'aaa@a.com',
        };
      },
    });

    // find: LoginForm이 렌더링 되면 HTML 태그를 찾는 API
    // const idInput = wrapper.find('#username');

    // HTML 엘리먼트의 값을 가져옴
    // console.log('input value: ', idInput.element.value);

    // computed 에 명시한 함수 반환값 가져옴
    // console.log('isUsernameValid: ', wrapper.vm.isUsernameValid);

    const warningText = wrapper.find('.warning');
    // console.log('warningText: ', warningText.html());

    // warnig문구 존재 하면 true
    expect(warningText.exists()).toBeTruthy();
  });

  test('ID와 PW가 입력되지 않으면 로그인 버튼 비활성화', () => {
    const wrapper = shallowMount(LoginForm, {
      data() {
        return {
          username: '',
          password: '',
        };
      },
    });
    const button = wrapper.find('.btn');

    // 버튼 비활성화 됐는지 테스트
    expect(button.element.disabled).toBeTruthy();
  });
});
