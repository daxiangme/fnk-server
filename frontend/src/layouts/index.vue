<template>
  <n-layout has-sider class="bg-layout layout">
    <n-layout-sider
        class="layout-side "
        :collapsed-width="theme.sider.collapsedWidth"
        :collapsed="theme.sider.collapsed"
        collapse-mode="width"
        :width="theme.sider.width"
        :inverted="theme.sider.inverted"
        :native-scrollbar="false"
        :show-trigger="theme.sider.showCollapse&&'bar'"
        @collapse="theme.setSiderCollapsed(true)"
        @expand="theme.setSiderCollapsed(false)"
    >
      <page-logo class="logo" :collapsed="theme.sider.collapsed" :height="theme.menu.headerHeight"/>
      <page-menu class="menu" :style="{top:`${theme.menu.headerHeight}px`}" :collapsed="theme.sider.collapsed"
                 :collapsed-width="theme.sider.collapsedWidth"
                 :inverted="theme.sider.inverted"/>
    </n-layout-sider>
    <n-layout style="height: 100vh" class="bg-layout">
      <n-layout-header
          class="bg-layout " :class="[isCard&&'px-12px']"
          :style="{height: theme.menu.tabsHeight + theme.menu.headerHeight+'px'}"
      >
        <div class="flex-col">
          <page-header
              :collapsed="theme.sider.collapsed"
              :show-collapsed="theme.menu.showCollapse"
              :height="theme.menu.headerHeight"
              :show-breadcrumb="theme.menu.showBreadcrumb"
              :show-breadcrumb-icon="theme.menu.showBreadcrumbIcon"
              @update:collapsed="theme.setSiderCollapsed"
          />
          <page-tabs :height="theme.menu.tabsHeight" :is-card="isCard" v-if="theme.menu.showTabs"/>
        </div>
      </n-layout-header>
      <n-layout position="absolute" :native-scrollbar="false" :style="[pageContentStyle]"
                class="bg-layout">
        <page-content/>
      </n-layout>
      <PageFooter v-if="theme.footer.show" :is-card="isCard" :height="theme.footer.height" :round="theme.layout.round"/>
    </n-layout>
  </n-layout>
</template>
<script setup lang="ts">
import {useThemeStore} from "@/store";

defineOptions({name: "BaseLayout"});
import PageLogo from "@/layouts/components/logo/index.vue";
import PageHeader from "@/layouts/components/header/index.vue";
import PageMenu from "@/layouts/components/menu/index.vue";
import PageContent from "@/layouts/components/content/index.vue";
import PageTabs from "@/layouts/components/tabs/index.vue";
import PageFooter from "@/layouts/components/footer/index.vue";
import {computed} from "vue";

const theme = useThemeStore();

const isCard = computed(() => theme.layout.mode === "card");

const pageContentStyle = computed(() => {
  const padding = 12;
  let top = theme.menu.showTabs ? theme.menu.headerHeight + theme.menu.tabsHeight : theme.menu.headerHeight;
  const bottom = theme.footer.show ? theme.footer.height : 0;
  const style = {} as any;
  if (!isCard.value) {
    top += padding;
  }
  style.top = `${top}px`;
  style.bottom = theme.footer.show?`${bottom + padding}px`:'0';
  return style;
});
</script>

<style>
.n-scrollbar > .n-scrollbar-container > .n-scrollbar-content {
  height: 100%;
}
</style>

<style scoped lang="scss">

#PAGE_CONTENT_SCROLLBAR {
  scrollbar-width: thin;
  scrollbar-color: rgba(0, 0, 0, 0.4) transparent;

  &::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.4);
    border-radius: 7px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: rgba(0, 0, 0, 0.4);
    border-radius: 7px;
  }

  &::-webkit-scrollbar {
    width: 7px;
    height: 7px;
  }

  &::-webkit-scrollbar-track-piece {
    background-color: rgba(0, 0, 0, 0);
    border-radius: 0;
  }
}


.layout-side {
  z-index: 99;
  box-shadow: 2px 0 8px 0 rgb(29, 35, 41, 0.05);
  position: relative;

  & .logo {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
  }
  & .menu {
    position: absolute;
    left: 0;
    right: 0;
  }
}


// 圆角 下边框
.header-rounded-bottom {
  border-bottom-left-radius: var(--n-border-radius);
  border-bottom-right-radius: var(--n-border-radius);
}
</style>
