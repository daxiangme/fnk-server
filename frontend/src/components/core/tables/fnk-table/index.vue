<template>
  <ArtTable ref="tableRef" v-bind="forwardedAttrs">
    <template v-for="(_, name) in $slots" #[name]="slotProps">
      <slot :name="name" v-bind="slotProps || {}" />
    </template>
  </ArtTable>
</template>

<script setup lang="ts">
  import { computed, ref, useAttrs } from 'vue'
  import ArtTable from '../art-table/index.vue'

  defineOptions({
    name: 'FnkTable',
    inheritAttrs: false
  })

  const tableRef = ref<InstanceType<typeof ArtTable>>()
  const forwardedAttrs = useAttrs() as any
  const elTableRef = computed(() => tableRef.value?.elTableRef)

  const scrollToTop = () => {
    tableRef.value?.scrollToTop()
  }

  defineExpose({
    tableRef,
    elTableRef,
    scrollToTop
  })
</script>
