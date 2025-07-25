<template>
  <v-dialog v-model="dialog" fullscreen transition="dialog-top-transition">
    <v-card height="100%">
      <v-toolbar color="#2E323F" height="100">
        <v-toolbar-title style="margin-left: 45%">사이트맵</v-toolbar-title>
        <v-btn icon dark @click="closeDialog">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <v-container fluid style="background-color: #393e4e; height: 100%; overflow-y: auto">
        <v-row>
          <v-col v-for="(mainMenu, index) in menuStore.mainMenus" :key="index" cols="12" sm="6" md="4" lg="2">
            <v-card flat>
              <v-card-title style="background-color: #393e4e; color: white; border-bottom: 1px solid #ffffff26">{{ mainMenu.name }}</v-card-title>
              <v-list style="background-color: #393e4e">
                <v-list-item
                  v-for="subMenu in mainMenu.subMenus"
                  :key="subMenu.name"
                  @click="navigateTo(subMenu.name, subMenu.path)"
                  class="submenu-item"
                >
                  <v-list-item-title style="color: #f5f5f5; font-size: 14px">{{ subMenu.name }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed } from 'vue';
import { useMenuStore } from '@/stores/index';
import { useRouter } from 'vue-router';

const props = defineProps({
  modelValue: Boolean,
});

const emit = defineEmits(['update:modelValue']);

const menuStore = useMenuStore();

const router = useRouter();

const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
});

const closeDialog = () => {
  dialog.value = false;
};

const navigateTo = (subMenuName, path) => {
  if (menuStore.openTabs.length > 8) {
    closeDialog();
    alert(`메뉴는 최대 9개까지 열 수 있습니다.\n메뉴를 닫은 후 시도해주세요.`);
    return;
  }

  const mainMenu = menuStore.mainMenus.find((menu) => menu.subMenus.some((subMenu) => subMenu.name === subMenuName));
  if (mainMenu) {
    // 메인메뉴가 로드 되어야 서브메뉴 불러 올 수 있음
    menuStore.setMainMenu(mainMenu.name);
    menuStore.addTab({
      name: subMenuName,
      path: path,
      mainMenu: mainMenu.name,
    });
    menuStore.setSubMenu(subMenuName);
    menuStore.setSelectedSubMenu(subMenuName);
    menuStore.setSelectedSubMenuPath(path);
    router.push(path);
  }
  closeDialog();
};
</script>

<style scoped>
.submenu-item:hover {
  background-color: #4a5161 !important;
}

.submenu-item {
  transition: background-color 0.3s ease;
}
</style>
