<template>
  <div class="page-header bg-#ffffff dark:bg-dark " :style="[getHeaderStyle]">
    <div class="page-header-left" v-if="false">
      <page-menu :mode="'horizontal'"/>
    </div>
    <n-space align="center" v-else>
      <template v-if="theme.menu.layout === 'base'">
        <header-menu-collapse :collapsed="props.collapsed" @click="(e:boolean)=>{ emits('update:collapsed',e) }"
                              v-if="props.showCollapsed"/>
        <!--   todo 可扩展   -->
        <div class="h-full flex-center" v-else>
          <!--        <n-button round secondary type="primary">常用</n-button>-->
        </div>
        <!--   面包屑   -->
        <header-breadcrumb v-if="props.showBreadcrumb" :show-icon="props.showBreadcrumbIcon"/>
      </template>
       <header-app-title v-else />
    </n-space>
    <div class="flex h-full">
      <header-github/>
      <header-theme/>
      <header-theme-setting/>
      <header-avatar/>
    </div>

  </div>
</template>
<script setup lang="ts">
import PageMenu from "@/layouts/components/menu/index.vue";
import HeaderMenuCollapse from "@/layouts/components/header/header-menu-collapse.vue";
import HeaderBreadcrumb from "@/layouts/components/header/header-breadcrumb.vue";
import HeaderAvatar from "@/layouts/components/header/header-avatar.vue";
import HeaderTheme from "@/layouts/components/header/header-theme.vue";
import {computed, PropType} from "vue";
import {useThemeStore} from "@/store";
import HeaderGithub from "@/layouts/components/header/header-github.vue";
import HeaderThemeSetting from "@/layouts/components/header/header-theme-setting.vue";
import HeaderAppTitle from "@/layouts/components/header/header-app-title.vue";

const props = defineProps({
  collapsed: {
    // 侧边栏菜单是否收起
    type: Boolean,
  },
  showCollapsed: {
    type: Boolean,
    default: true
  },
  height: {
    type: Number,
    default: 44
  },
  showBreadcrumb: {
    type: Boolean,
    default: true
  },
  showBreadcrumbIcon: {
    type: Boolean,
    default: true
  },
  // base 基本布局 app 应用布局
  layout: {
    type: String as PropType<'base' | 'app'>,
    default: 'base'
  }
})
const emits = defineEmits(['update:collapsed', 'clickMenuItem']);
const theme = useThemeStore()
const getHeaderStyle = computed(() => {
  let style = {} as any
  if (theme.layout.mode === 'card') {
    if (theme.menu.showTabs) {
      style.borderBottomLeftRadius = theme.layout.round + 'px'
      style.borderBottomRightRadius = theme.layout.round + 'px'
    }
  }
  style.height = props.height + 'px'
  return style
})
</script>

<style scoped lang="scss">
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 10;
  //position: absolute;
  //top: 0;
  //left: 10px;
  //width: calc(100% - 20px);
  box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
  padding: 0 12px;

}

.page-header-fix {
  position: fixed;
  top: 0;
  right: 0;
  left: 200px;
  z-index: 11;
}
</style>