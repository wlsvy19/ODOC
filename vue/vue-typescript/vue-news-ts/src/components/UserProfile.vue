<template>
  <div class="user-container">
    <div>
      <i class="fas fa-user"></i>
    </div>
    <div class="user-description">
      <template v-if="info.id">
        <!---- slot: 하위컴포넌트에서 정의만, 상위에서 name불러 사용-->
        <!-- v-if 대신 사용 가능 -->
        <slot name="username">
          <!-- 상위 컴포넌트에서 정의할 영역-->
        </slot>
      </template>

      <div class="time">
        <slot name="time">
          <!-- 상위 컴포넌트에서 정의할 영역-->
        </slot>
        <slot name="karma">
           <!-- 상위 컴포넌트에서 정의할 영역-->
        </slot>
        </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    info: Object,
  },
};
</script>

<style scoped>
.user-container {
  display: flex;
  align-items: center;
  padding: 0.6rem;
}
.fa-user {
  font-size: 2.5rem;
}
.user-description {
  padding-left: 8px;
}
.time {
  font-size: 0.8rem;
}
</style>