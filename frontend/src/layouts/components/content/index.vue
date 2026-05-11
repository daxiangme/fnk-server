<template>
    <div class="flex-1 flex-col h-full  page-content mx-12px" :class="[`rounded-${theme.naive.borderRadius}px`]">
        <router-view v-slot="{ Component, route }" :key="key">
          <transition :name="theme.animate.enable?theme.animate.type:''" mode="out-in" :appear="true"  >
            <keep-alive :include="routeStore.cacheRoutes">
              <component class="transition-300" :is="Component" :key="route.fullPath" v-if="routeStore.reloadFlag" />
            </keep-alive>
          </transition>
        </router-view>
      <n-back-top :right="30" class="z100" />
    </div>
</template>
<script setup lang="ts">
import {useRouteStore, useThemeStore} from "@/store";
import {computed} from "vue";
import {useRoute} from "vue-router";

const theme = useThemeStore();
const routeStore = useRouteStore();
const route = useRoute();
const key = computed(() => {
  return route.fullPath+Math.random()
});
</script>

<style lang="scss" scoped>
.page-content{
}
</style>