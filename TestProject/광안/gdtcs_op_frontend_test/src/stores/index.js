import { defineStore } from 'pinia';
import { request } from '@/utils/common';
import Cookies from 'js-cookie';
import router from '@/router';

export const ozUrl = import.meta.env.VITE_APP_OZ_URL;
export const ozAppImageUrl = import.meta.env.VITE_APP_OZ_APP_IMAGE_URL;

// export const ozUrl = 'http://10.66.1.31:9082/oz80/';
// export const ozAppImageUrl = 'http://10.66.1.31:9081/api/base/getReportImageFile?IC_CODE=094&RPT_APP_CODE=';

export const useMenuStore = defineStore('menu', {
  state: () => ({
    mainMenu: null,
    subMenu: null,
    selectedSubMenu: null, // 선택한 서브메뉴를 탭으로 보여주는 변수
    selectedSubMenuPath: null,
    openTabs: [], // 선택된 서브메뉴를 저장하는 배열
    sideWidth: 215, // 사이드바 넓이
    sideToggle: false,
    mainMenus: [],

    currentSearchHandler: null,
    currentAddHandler: null,
    currentDeleteHandler: null,
    currentSaveHandler: null,
    currentExcelHandler: null,
    currentPrintHandler: null,
    currentPowerOffHandler: null,
  }),
  actions: {
    setMainMenu(menuName) {
      this.mainMenu = menuName;
    },
    setSubMenu(menuName) {
      this.subMenu = menuName;
    },
    setSelectedSubMenu(menuName) {
      this.selectedSubMenu = menuName;
    },
    setSelectedSubMenuPath(menuPath) {
      this.selectedSubMenuPath = menuPath;
    },
    addTab(subMenu) {
      const existingTab = this.openTabs.find((tab) => tab.name === subMenu.name);
      if (!existingTab) {
        this.openTabs.push(subMenu);
      }
    },
    // removeTab(subMenuName) {
    //   this.openTabs = this.openTabs.filter((tab) => tab.name !== subMenuName);
    // },
    setSideToggle(path) {
      //this.sideToggle = !this.sideToggle;
      //this.sideToggle = false;
      // this.mainMenu = null;
      // this.subMenu = null;
      //this.openTabs = [];
      if (path === 1) {
        this.mainMenu = null;
        this.subMenu = null;
        this.sideToggle = false;
        this.openTabs = [];
      } else if (path === 2) {
        if (this.mainMenu !== null) {
          this.sideToggle = !this.sideToggle;
        } else {
          this.sideToggle = false;
        }
      }
    },
    async fetchMenuData() {
      const data = await request('post', 'api/base/getMenuInfo', {
        IC_CODE: localStorage.getItem('loginIcCode'),
        WORKER_NO: localStorage.getItem('loginWorkerNo'),
      });
      const mainMenus = data
        .filter((mainmenu) => mainmenu.MENU_DPH === 1)
        .map((mainmenu) => ({
          name: mainmenu.PRG_NM,
          path: `/${mainmenu.PRG_CODE}`,
        }));
      // 상위 메뉴 별로 하위 메뉴 항목을 찾아 매핑
      mainMenus.forEach((mainMenu) => {
        const subMenus = data
          .filter((subMenu) => subMenu.MENU_DPH === 2 && subMenu.HIGH_PRG_CODE === mainMenu.path.substring(1))
          .map((subMenu) => ({
            name: subMenu.PRG_NM,
            path: `/${subMenu.PRG_CODE}`,
            btnSearch: subMenu.BTN_SEARCH,
            btnAdd: subMenu.BTN_ADD,
            btnDelete: subMenu.BTN_DELET,
            btnSave: subMenu.BTN_STRG,
            btnExcel: subMenu.BTN_EXCEL,
            btnPrint: subMenu.BTN_PRT,
          }));
        mainMenu.subMenus = subMenus;
      });
      this.mainMenus = mainMenus;
    },
    closeAllTabs() {
      this.openTabs = [];
      this.setSubMenu(null);
      router.push('/');
    },
    closeOneTab(subMenuName) {
      this.openTabsNum = this.openTabs.findIndex((tab) => tab.name === subMenuName);
      this.openTabs = this.openTabs.filter((tab) => tab.name !== subMenuName);

      if (this.openTabs.length > 0) {
        const nextTabIdx = this.openTabsNum >= this.openTabs.length ? this.openTabsNum - 1 : this.openTabsNum;
        const nextTab = this.openTabs[nextTabIdx];
        if (nextTab) {
          this.setMainMenu(nextTab.mainMenu);
          this.setSubMenu(nextTab.name);
          return [nextTab.name, nextTab.path];
        } else {
          this.setSubMenu(null);
          router.push('/');
          return null;
        }
      } else {
        this.setSubMenu(null);
        router.push('/');
        return null;
      }
    },
    setCurrentSearchHandler(handler) {
      this.currentSearchHandler = handler;
    },
    setCurrentAddHandler(handler) {
      this.currentAddHandler = handler;
    },
    setCurrentDeleteHandler(handler) {
      this.currentDeleteHandler = handler;
    },
    setCurrentSaveHandler(handler) {
      this.currentSaveHandler = handler;
    },
    setCurrentExcelHandler(handler) {
      this.currentExcelHandler = handler;
    },
    setCurrentPrintHandler(handler) {
      this.currentPrintHandler = handler;
    },
    setCurrentPowerOffHandler(handler) {
      this.currentPowerOffHandler = handler;
    },
    resetMenu() {
      this.mainMenu = null;
      this.subMenu = null;
      this.selectedSubMenu = null;
      this.openTabs = [];
      this.sideWidth = 250;
      this.sideToggle = false;
    },
  },
  getters: {
    getMainMenu() {
      return this.mainMenu;
    },
    getSubMenu() {
      return this.subMenu;
    },
    getSelectedSubMenu() {
      return this.selectedSubMenu;
    },
    getSelectedSubMenuPath() {
      return this.selectedSubMenuPath;
    },
    getSideWidth() {
      return this.sideWidth;
    },
  },
});

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    IC_CODE: '',
    IC_DIV: '',
    IC_NM: '',
    WORKER_NO: '',
    WORKER_NM: '',
    WORKER_CLS: '',
    loginTime: '',
    IP: '',
  }),
  getters: {
    isAuthenticated(state) {
      return !!state.accessToken;
    },
    getIcNm(state) {
      return state.IC_NM;
    },
    getWorkerNo(state) {
      return state.WORKER_NO;
    },
    getWorkerNm(state) {
      return state.WORKER_NM;
    },
    getWorkerClass(state) {
      return state.WORKER_CLS;
    },
    getIcCode(state) {
      return state.IC_CODE;
    },
    getLoginTime(state) {
      return state.loginTime;
    },
    getIP(state) {
      return state.IP;
    },
  },
  actions: {
    login(accessToken) {
      this.accessToken = accessToken;
    },
    setIcNm(IC_NM) {
      this.IC_NM = IC_NM;
    },
    setWorkerNo(WORKER_NO) {
      this.WORKER_NO = WORKER_NO;
    },
    setWorkerNm(WORKER_NM) {
      this.WORKER_NM = WORKER_NM;
    },
    setWorkerClass(WORKER_CLS) {
      this.WORKER_CLS = WORKER_CLS;
    },
    setIcCode(IC_CODE) {
      this.IC_CODE = IC_CODE;
    },
    setLoginTime(loginTime) {
      this.loginTime = loginTime;
    },
    setIP(IP) {
      this.IP = IP;
    },
    checkLoginStatus() {
      const token = Cookies.get('gdtcs-auth-token');
      if (token) {
        this.accessToken = token;
      } else {
        this.accessToken = null;
      }
    },
    logout() {
      Cookies.remove('gdtcs-auth-token');
      const menuStore = useMenuStore();
      menuStore.resetMenu();
      this.accessToken = null;
      this.IC_CODE = '';
      this.IC_DIV = '';
      this.IC_NM = '';
      this.WORKER_NO = '';
      this.WORKER_NM = '';
      this.WORKER_CLS = '';
      this.loginTime = '';
      this.IP = '';
    },
  },
});

export const useColorStore = defineStore('color', {
  state: () => ({
    /** green 계열 */
    // basicColor: '#017B56',
    // selectedColor: '#026344',
    // buttonColor: '#0A9B6F',
    /** red 계열 */
    // basicColor: '#cc0000',
    // selectedColor: '#b30000',
    // buttonColor: '#e60000',
    /** yello 계열 */
    // basicColor: '#808000',
    // selectedColor: '#666600',
    // buttonColor: '#b3b300',
    /** blue 계열 */
    basicColor: '#0086E5',
    selectedColor: '#0077CC',
    buttonColor: '#0095FF',
  }),
});

export const useScrollStore = defineStore('scroll', {
  state: () => ({
    scrollValues: {},
  }),
  actions: {
    setScrollValue(key, value) {
      this.scrollValues[key] = value;
    },
    getScrollValue(key) {
      return this.scrollValues[key] || 0;
    },
  },
});

export const useFareStore = defineStore('fare', {
  state: () => ({
    fareInfo: [], // 통행요금 정보
  }),
  getters: {
    getFareInfo(state) {
      return state.fareInfo;
    },
  },
  actions: {
    setFareInfo(fareInfo) {
      this.fareInfo = fareInfo;
    },
  },
});

export const loadSystemSmallCodeAll = defineStore('systemSmallCode', {
  state: () => ({
    systemCode: [], // SystemCode정보
  }),
  actions: {
    /**
     * setSystemSmallCodeAll 공통 함수
     * @returns
     */
    async setSystemSmallCodeAll() {
      const response = await request('post', 'api/base/getSystemSmallCode', {
        CODESC_USE_YN: 'Y',
      });
      if (response && Array.isArray(response)) {
        this.systemCode = await response.reduce((acc, item) => {
          if (!acc[item.CODELC]) {
            acc[item.CODELC] = [];
          }
          acc[item.CODELC].push({
            title: item.CODESC_NM,
            value: item.CODESC,
          });
          return acc;
        }, {});
      }
      //return this.systemCode;
    },
  },
  getters: {
    getSystemSmallCodeAll(state) {
      return state.systemCode;
    },
  },
});
