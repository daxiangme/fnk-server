<template>
  <n-space align="center" size="small">

    <icon-material-symbols:arrow-back-ios-rounded
      v-if="false"
      class="text-20px text-center hover:cursor-pointer hover:opacity-75"
      />
    <p class="text-16px font-bold">{{ route.meta?.title }}</p>

  </n-space>
</template>
<script setup lang="ts">
import {useRoute} from "vue-router";

const route = useRoute()
console.log(route)
</script>

<style scoped>

</style>