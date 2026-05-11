<template>
  <base-hover-tip text="主题模式" >
    <div class="flex-center w-48px text-24px cursor-pointer " @click="changeThemeMode">
      <icon-line-md:sunny-filled-loop class="text-#FFCC00" v-if="theme.mode==='light'" />
      <icon-line-md:moon-filled-loop v-else-if="theme.mode==='dark'"/>
      <icon-line-md:light-dark-loop  v-else />
    </div>
  </base-hover-tip>
</template>
<script setup lang="ts">

import {useThemeStore} from "@/store";
import {ref} from "vue";

const theme = useThemeStore()
const showSetting = ref(false)

function changeThemeMode() {
  const mode = theme.mode
 if (mode === 'light') {
   theme.setThemeMode('dark')
 } else if (mode === 'dark') {
   theme.setThemeMode('auto')
 } else {
   theme.setThemeMode('light')
 }
}
</script>

<style scoped>

</style>