<template>
<n-card title="项目信息" :bordered="false" size="small" class="rounded-8px shadow-sm">
  <n-descriptions label-placement="left" bordered size="small" :column="2">
    <n-descriptions-item label="版本">
      <n-tag type="primary">{{ version }}</n-tag>
    </n-descriptions-item>
    <n-descriptions-item label="最后编译时间">
      <n-tag type="primary">2023年12月28日15:09:22</n-tag>
    </n-descriptions-item>
    <n-descriptions-item label="Github地址">
      <a class="text-primary" href="https://github.com/daxiangme/fnk-server" target="_blank">Github地址</a>
    </n-descriptions-item>
    <n-descriptions-item label="预览地址">
      <a class="text-primary" href="https://fnk-server.isite.fun" target="_blank">失效了</a>
    </n-descriptions-item>
  </n-descriptions>
</n-card>
</template>
<script setup lang="ts">
import {pkgJson} from "./index";

const { version } = pkgJson;
</script>

<style scoped>

</style>
