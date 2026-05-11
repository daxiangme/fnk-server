<template>
  <n-menu ref="menuRef"
          :value="activeKey"
          :expanded-keys="expandedKeys"
          accordion
          :mode="props.mode"
          :collapsed="props.collapsed"
          :options="menuOptions"
          :collapsed-icon-size="24"
          :collapsed-width="props.collapsedWidth"
          :indent="24"
          :inverted="props.inverted"
          @update:value="clickMenuItem"
          @update:expanded-keys="menuExpanded"
          show-trigger/>
</template>
<script setup lang="ts">
import {computed, h, PropType, reactive, ref, watch} from "vue";
import {useRouteStore} from "@/store";
import {useRoute, useRouter} from "vue-router";
import {System} from "@/typings/system";

const menuOptions = useRouteStore().menus as any
const menuRef = ref()
const route = useRoute();
const router = useRouter();
const activeKey = computed(() => (route.meta?.activeMenu ? route.meta.activeMenu : route.name) as string);
const expandedKeys = ref<string[]>([]);
const props = defineProps({
  collapsed: {
    // 侧边栏菜单是否收起
    type: Boolean,
  },
  mode:{
    type: String as PropType<"vertical" | "horizontal">,
    default: 'vertical',
  },
  inverted:{
    type: Boolean,
    default: false
  },
  collapsedWidth:{
    type: Number,
    default: 64
  }
})
// 菜单点击
const clickMenuItem = (key: string, item: System.GlobalMenu) => {
  if(router.currentRoute.value.name !== key){
    router.push({path: item.routePath})
  }
}
// 菜单展开
const menuExpanded = (keys: string[]) => {
  expandedKeys.value = keys;
}
const getActiveKeyPathsOfMenus = (activeKey: string, menus: System.GlobalMenu[]) => {
  const activeKeyPaths: string[] = [];
  if (!activeKey) {
    return activeKeyPaths;
  }
  // menus中的channel的key是否等于activeKey
  menus.forEach((menu) => {
    if (menu.key === activeKey) {
      activeKeyPaths.push(activeKey);
    } else if (menu.children) {
      const childActiveKeyPaths = getActiveKeyPathsOfMenus(activeKey, menu.children);
      if (childActiveKeyPaths.length) {
        activeKeyPaths.push(menu.key);
        activeKeyPaths.push(...childActiveKeyPaths);
      }
    }
  });
  return activeKeyPaths;
};
console.log(menuOptions)
watch(
    () => route.name,
    () => {
      expandedKeys.value = getActiveKeyPathsOfMenus(activeKey.value, menuOptions);
    },
    { immediate: true }
);
// updateMenu()
</script>

<style scoped>

</style>