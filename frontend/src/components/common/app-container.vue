<template>
  <div  class="w-full flex-col  flex-1" >
    <slot></slot>
  </div>

</template>
<script setup lang="ts">
import {computed} from "vue";
import {round} from "colord/helpers";
import {useThemeStore} from "@/store";

/**
 * @description 通用容器组件
 *
 */
const props = defineProps({
  /** 是否透明 */
  transparent: {
    type: Boolean,
    default: false
  },
  /** 是否自动高度 true 自动计算 否100% */
  height: {
    type: String,
    default: 'calc(100vh - 114px)'
  },
  round: {
    type: Boolean,
    default: true
  }
})
const theme =  useThemeStore()
const style = computed(() => {
  const style = {
    height: props.height
  } as any

  if (props.round){
    style.borderRadius= theme.round+'px'
  }
  return style
})

</script>

<style scoped>

</style>