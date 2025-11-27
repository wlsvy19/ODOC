<template>
  <v-layout class="rounded rounded-md">
    <!-- 헤더영역 -->
    <v-app-bar id="header-container" flat stt>
      <v-icon icon="mdi-menu" style="color: black; margin-left: 1rem" @click="openSiteMap" />
      <router-link to="/main" class="header-logo-container" @click="menuStore.setSideToggle(1)">
        <img :src="`/api/common/getLogoImage`" alt="Logo" class="logo" />
      </router-link>
      <v-spacer />
      <v-autocomplete
        @update:modelValue="onUpdateMenuEvent"
        :items="getAllMenuList"
        placeholder="메뉴명을 입력하세요."
        no-data-text=""
        prepend-inner-icon="mdi-magnify"
        menu-icon=""
        :menu-props="{ contentClass: 'searchMenuClass' }"
        variant="outlined"
        density="compact"
        open-on-clear
        return-object
        clearable
        rounded
        auto-select-first
        hide-details
      />
      <v-spacer />
      <div class="office-area">
        <v-btn icon="mdi-office-building" style="font-size: 22px" readonly /><span>{{ authStore.getIcNm }}</span>
      </div>
      <div class="account-area" @click="showDialogChangePassword">
        <v-btn icon="mdi-account-circle" class="account-icon" style="font-size: 22px" /><span>{{ authStore.getWorkerNm }} 님</span>
      </div>
      <DialogModifyPassword v-model:is-active="isShowDialog" v-model:user-info="userInfo" />
      <div class="login-info-area">
        <p>직전로그인시간: {{ authStore.getLoginTime }}</p>
        <p>직전로그인IP: {{ authStore.getIP }}</p>
      </div>
      <v-icon icon="mdi-exit-to-app" class="exit-icon" @click="onLogout" style="font-size: 30px" />
    </v-app-bar>

    <!-- 상위 메뉴 네비게이션 영역 -->
    <v-app-bar :style="navContainerStyle" height="45" flat>
      <v-btn
        class="font-regular-ac"
        style="font-size: 17px; border-radius: 0%"
        height="45"
        v-for="menu in menuStore.mainMenus"
        :key="menu.name"
        @click="onMainMenu(menu.name)"
        :style="selectedMainMenu(menu.name)"
      >
        {{ menu.name }}
      </v-btn>
    </v-app-bar>

    <!-- 사이드바 토글 버튼 영역 -->
    <div :class="menuStore.sideToggle ? 'sidebar-toggle-on' : 'sidebar-toggle-off'">
      <!-- <v-icon v-if="menuStore.sideToggle" icon="mdi-chevron-left" />
        <v-icon v-else icon="mdi-chevron-right" /> -->
      <v-icon style="margin-left: 2rem" :icon="sidebarIcon" @click="toggleSideMenu" @mouseover="toggleIcon(true)" @mouseleave="toggleIcon(false)" />
    </div>

    <!-- 좌측 사이드바 영역-->
    <v-navigation-drawer
      v-if="menuStore.sideToggle"
      v-model="menuStore.sideToggle"
      app
      :width="sideWidth"
      style="background-color: #445270; color: #fff"
    >
      <v-list style="margin-top: -8px">
        <v-list-item
          height="30"
          v-for="subMenu in subMenus"
          :key="subMenu.name"
          @click="onSubMenu(subMenu.name, subMenu.path)"
          :class="{
            'sub-menu-active': menuStore.getSubMenu === subMenu.name,
          }"
        >
          <v-list-item-title class="font-regular-ac" style="font-size: 15px">
            {{ subMenu.name }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <ButtonComponent
      :disabledSearch="btnSearch !== 'Y'"
      :disabledAdd="btnAdd !== 'Y'"
      :disabledDelete="btnDelete !== 'Y'"
      :disabledSave="btnSave !== 'Y'"
      :disabledExcel="btnExcel !== 'Y'"
      :disabledPrint="btnPrint !== 'Y'"
      @search-click="handleSearch"
      @add-click="handleAdd"
      @delete-click="handleDelete"
      @save-click="handleSave"
      @excel-click="handleExcel"
      @print-click="handlePrint"
      @power-off-click="handlePowerOff"
    />

    <v-main>
      <router-view v-if="menuStore.openTabs.length !== 0" v-slot:default="{ Component, route }">
        <keep-alive :exclude="keepAliveExcludes">
          <component :is="Component" :key="route.path" />
        </keep-alive>
      </router-view>
      <router-view v-else name="main">
        <MainDashboard />
      </router-view>
    </v-main>

    <!-- 선택한 메뉴탭 영역-스크롤 버전-->
    <v-app-bar :flat="true" v-show="menuStore.subMenu != null" :height="36">
      <div class="selected-tab" style="margin-top: 12px">
        <v-icon v-if="isCloseAllTabs" icon="mdi-close-box-multiple" @click="closeAllTabs" title="모두 닫기" />
        <draggable
          v-model="menuStore.openTabs"
          @change="onDragChange"
          item-key="name"
          ghost-class="ghost"
          chosen-class="chosen"
          animation="200"
          swap-threshold="0.5"
        >
          <template #item="{ element }">
            <span
              id="chip-container"
              style="color: #b2b2b2"
              :class="{
                'select-menu-active': menuStore.getSubMenu === element.name,
              }"
            >
              <v-chip label @click="onTab(element.name, element.path)">
                {{ element.name }}
                <v-icon
                  id="close-icon"
                  @click.stop="closeTab(element.name, element.path)"
                  icon="mdi-close-circle"
                  :class="{
                    'select-menu-close-active': menuStore.getSubMenu === element.name,
                  }"
                />
              </v-chip>
            </span>
          </template>
        </draggable>
      </div>
    </v-app-bar>

    <v-app-bar v-if="menuStore.openTabs.length !== 0" flat style="background-color: #f5f5f5" height="8" />
    <v-app-bar v-else flat style="background-color: #f5f5f5; margin-top: -20px" height="-36" />
    <SiteMap v-model="showSiteMap" />
  </v-layout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useMenuStore, useAuthStore, useColorStore, loadSystemSmallCodeAll } from '@/stores/index';
import draggable from 'vuedraggable';
import DialogModifyPassword from '../dialog/DialogModifyPassword.vue';
import { request } from '@/utils/common.js';

const menuStore = useMenuStore();
const authStore = useAuthStore();
const isShowDialog = ref(false);
const userInfo = ref({});

const showDialogChangePassword = () => {
  userInfo.value = {
    IC_CODE: authStore.getIcCode,
    WORKER_NO: authStore.getWorkerNo,
    WORKER_NM: authStore.getWorkerNm,
  };
  isShowDialog.value = true;
};

onMounted(async () => {
  await menuStore.fetchMenuData();
  authStore.setIcNm(localStorage.getItem('icNm'));
  authStore.setWorkerNo(localStorage.getItem('loginWorkerNo'));
  authStore.setIcCode(localStorage.getItem('loginIcCode'));
  authStore.setWorkerNm(localStorage.getItem('workerNm'));
  authStore.setWorkerClass(localStorage.getItem('workerClass'));
  authStore.setLoginTime(localStorage.getItem('loginTime'));
  authStore.setIP(localStorage.getItem('IP'));
  loadSystemSmallCodeAll().setSystemSmallCodeAll(localStorage.getItem('systemCode'));
});

const sideWidth = computed(() => `${menuStore.getSideWidth}`);

const subMenus = computed(() => {
  const mainMenu = menuStore.mainMenus.find((menu) => menu.name === menuStore.mainMenu);
  return mainMenu ? mainMenu.subMenus : [];
});

const getAllMenuList = computed(() => {
  const allMenuList = menuStore.mainMenus
    .map((obj) =>
      obj.subMenus.map((element) => {
        return { title: element.name, path: element.path, mainMenuName: obj.name };
      }),
    )
    .flat();
  return allMenuList;
});

const onMainMenu = (mainMenuName) => {
  menuStore.setMainMenu(mainMenuName);
  menuStore.sideToggle = menuStore.mainMenu !== null;
};

const colorStore = useColorStore();

const navContainerStyle = computed(() => {
  return {
    backgroundColor: colorStore.basicColor,
    color: '#fff',
  };
});

const selectedMainMenu = (mainMenuName) => {
  return {
    backgroundColor: menuStore.getMainMenu === mainMenuName ? colorStore.selectedColor : colorStore.basicColor,
  };
};

const router = useRouter();
const powerOffData = ref('');
const powerOffPath = ref('');
const onSubMenu = (subMenuName, path) => {
  if (menuStore.openTabs.length > 9) {
    alert(`메뉴는 최대 10개까지 열 수 있습니다.\n메뉴를 닫은 후 시도해주세요.`);
    return;
  }

  keepAliveExcludes.value = '';
  const mainMenu = menuStore.mainMenus.find((menu) => menu.subMenus.some((subMenu) => subMenu.name === subMenuName));
  if (mainMenu) {
    menuStore.addTab({
      name: subMenuName,
      path: path,
      mainMenu: mainMenu.name,
    });
    menuStore.setSubMenu(subMenuName);
    menuStore.setSelectedSubMenu(subMenuName);
    menuStore.setSelectedSubMenuPath(path);
    powerOffData.value = subMenuName;
    powerOffPath.value = path;
    router.push(path);
  }
};

const onTab = (tabMenuName, path) => {
  const tab = menuStore.openTabs.find((tab) => tab.name === tabMenuName);
  if (tab) {
    menuStore.setMainMenu(tab.mainMenu);
    menuStore.setSubMenu(tabMenuName);
    menuStore.setSelectedSubMenuPath(path);
  }
  powerOffData.value = tabMenuName;
  powerOffPath.value = path;
  router.push(path);
};

const keepAliveExcludes = ref('');
const beforeData = ref([]);
const closeTab = (tabName, path) => {
  const route = router.getRoutes().find((route) => route.path === path);
  keepAliveExcludes.value = route ? route.name : null;
  beforeData.value = menuStore.closeOneTab(tabName);
  if (beforeData.value !== null) {
    onTab(beforeData.value[0], beforeData.value[1]);
  }
};

const isHovering = ref(false);
const toggleIcon = (hovering) => {
  if (menuStore.mainMenu !== null) {
    isHovering.value = hovering;
  }
};

const toggleSideMenu = () => {
  menuStore.setSideToggle(2);
};

const sidebarIcon = computed(() => {
  if (!menuStore.sideToggle) {
    return 'mdi-chevron-right';
  } else if (isHovering.value) {
    return 'mdi-chevron-left';
  } else {
    return 'mdi-drag-vertical';
  }
});

const isCloseAllTabs = computed(() => {
  return menuStore.openTabs.length > 5;
});

const closeAllTabs = () => {
  //const currentSubMenu = menuStore.getSubMenu;
  //menuStore.openTabs = menuStore.openTabs.filter((tab) => tab.name === currentSubMenu);
  menuStore.closeAllTabs();
};

const onLogout = async () => {
  if (confirm('로그아웃 하시겠습니까?')) {
    try {
      await request('post', '/api/logout', {
        IC_CODE: authStore.getIcCode,
        WORKER_NO: authStore.getWorkerNo,
        LOGIN_TIME: authStore.getLoginTime,
      });
      authStore.logout();
      localStorage.removeItem('loginIcCode');
      localStorage.removeItem('IP');
      localStorage.removeItem('loginTime');
      localStorage.removeItem('loginWorkerNo');
      localStorage.removeItem('workerNm');
      localStorage.removeItem('workerClass');
      localStorage.removeItem('icNm');
      localStorage.removeItem('PassPrim-scroll-value');
      localStorage.removeItem('systemCode');
      router.push('/');
    } catch (error) {
      alert('로그아웃 과정에서 오류가 발생했습니다. 관리자한테 문의하세요.', error);
    }
  }
};

const selectedSubMenu = computed(() => {
  const mainMenu = menuStore.mainMenus.find((menu) => menu.name === menuStore.getMainMenu);
  const returnData = mainMenu?.subMenus.find((subMenu) => subMenu.name === menuStore.getSubMenu);
  return returnData;
});

const onUpdateMenuEvent = (item) => {
  if (item) {
    onMainMenu(item.mainMenuName);
    onSubMenu(item.title, item.path);
  }
};

const btnSearch = computed(() => selectedSubMenu.value?.btnSearch);
const btnAdd = computed(() => selectedSubMenu.value?.btnAdd);
const btnDelete = computed(() => selectedSubMenu.value?.btnDelete);
const btnSave = computed(() => selectedSubMenu.value?.btnSave);
const btnExcel = computed(() => selectedSubMenu.value?.btnExcel);
const btnPrint = computed(() => selectedSubMenu.value?.btnPrint);

const handleSearch = () => {
  if (menuStore.currentSearchHandler) {
    menuStore.currentSearchHandler();
  } else {
    alert('기본 Search 처리');
  }
};

const handleAdd = () => {
  if (menuStore.currentAddHandler) {
    menuStore.currentAddHandler();
  } else {
    alert('기본 Add 처리');
  }
};

const handleDelete = () => {
  if (menuStore.currentDeleteHandler) {
    menuStore.currentDeleteHandler();
  } else {
    alert('기본 Delete 처리');
  }
};

const handleSave = () => {
  if (menuStore.currentSaveHandler) {
    menuStore.currentSaveHandler();
  } else {
    alert('기본 Save 처리');
  }
};

const handleExcel = () => {
  if (menuStore.currentExcelHandler) {
    menuStore.currentExcelHandler();
  } else {
    alert('기본 Excel 처리');
  }
};

const handlePrint = () => {
  if (menuStore.currentPrintHandler) {
    menuStore.currentPrintHandler();
  } else {
    alert('기본 Print 처리');
  }
};
const handlePowerOff = () => {
  if (menuStore.openTabs.length != 0) {
    closeTab(powerOffData.value, powerOffPath.value);
  }
};

const onDragChange = (event) => {
  menuStore.openTabs = event.moved.newIndex = menuStore.openTabs;
};

const showSiteMap = ref(false);

const openSiteMap = () => {
  showSiteMap.value = true;
};
</script>

<style scoped>
#header-container {
  background-color: #fff;
}

.v-btn {
  margin: 0.1rem;
}

#chip-container {
  display: inline-flex;
}

#close-icon {
  position: absolute;
  top: 0;
  right: 0;
  font-size: 0.8rem;
  cursor: pointer;
}

.sidebar-toggle-on,
.sidebar-toggle-off {
  justify-content: center;
  position: fixed;
  top: 33rem;
  z-index: 1;
}

.sidebar-toggle-on {
  margin-left: 11rem;
}

.sidebar-toggle-off {
  margin-left: -2.5rem;
}

.exit-icon {
  cursor: pointer;
  margin-right: 0.7rem;
}

.office-area {
  margin-right: 1rem;
}

.account-area {
  margin-right: 1rem;
}

.login-info-area {
  font-size: 12px;
  flex-direction: column;
  padding: 5px 10px;
  border-radius: 5px;
  margin-right: 1rem;
  color: black;
  background-color: #f5f5f5;
}

.sub-menu-active {
  background-color: #23304f;
  color: #fff;
}

#chip-container .v-chip {
  border-radius: 2px;
  margin: 0.15rem;
  font-size: 0.8rem;
}

.select-menu-active .v-chip {
  color: #666666;
}

.header-logo-container {
  margin-left: 10px;
  display: flex;
  align-items: center;
}

.logo {
  height: 62px;
  margin-left: 1rem;
}

.v-toolbar__content > .v-btn:first-child {
  margin-inline-start: 0px;
}

.v-chip.v-chip--size-default {
  --v-chip-height: 26px;
}

.selected-tab {
  display: flex;
  overflow-x: auto;
}

.ghost {
  opacity: 0.5;
}

.chosen {
  background-color: #e0e0e0;
}
</style>
