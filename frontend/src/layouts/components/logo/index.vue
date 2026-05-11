<template>
  <div class="flex-center nowrap-hidden text-primary transition-all" :style="{height:`${props.height}px`}">
    <icon-local-logo class="text-32px " :class="{ 'mr-2':!props.collapsed}"/>
    <h2 v-show="!props.collapsed" class="text-xl font-semibold ">{{VITE_APP_TITLE}}</h2>
  </div>
</template>
<script setup lang="ts">

const props = defineProps({
  // 侧边栏是否折叠
  collapsed: {
    type: Boolean,
  },
  height:{
    type: Number,
    default: 44
  }
});

const {VITE_APP_TITLE} = import.meta.env;
</script>

<style scoped lang="scss">

</style>