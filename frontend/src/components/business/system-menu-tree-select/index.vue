<template>
  <ElTreeSelect
    v-model="model"
    :data="menuOptions"
    :loading="loading"
    :props="treeProps"
    node-key="id"
    check-strictly
    clearable
    filterable
    class="w-full"
    :placeholder="placeholder"
    v-bind="$attrs"
  />
</template>

<script setup lang="ts">
  import { ElMessage } from 'element-plus'
  import { fetchGetMenuList } from '@/api/system-manage'
  import { buildMenuTree, normalizeMenuType } from '@/utils/backend-menu'

  defineOptions({ name: 'SystemMenuTreeSelect', inheritAttrs: false })

  const model = defineModel<string | undefined | null>()

  const props = withDefaults(
    defineProps<{
      placeholder?: string
      includeRoot?: boolean
      rootLabel?: string
      selectableTypes?: Array<'TABLE' | 'MENU' | 'BUTTON'>
      excludeIds?: string[]
    }>(),
    {
      placeholder: '请选择菜单',
      includeRoot: true,
      rootLabel: '顶级目录',
      selectableTypes: () => ['TABLE', 'MENU'],
      excludeIds: () => []
    }
  )

  type MenuTreeNode = Api.SystemManage.MenuItem & {
    disabled?: boolean
    children?: MenuTreeNode[]
  }

  const loading = ref(false)
  const rawMenus = ref<Api.SystemManage.MenuItem[]>([])
  const treeProps = {
    label: 'name',
    value: 'id',
    children: 'children',
    disabled: 'disabled'
  }

  const menuOptions = computed<MenuTreeNode[]>(() => {
    const excludeIds = new Set(props.excludeIds)
    const selectableTypes = new Set(props.selectableTypes)
    const availableMenus = rawMenus.value.filter((item) => {
      if (excludeIds.has(item.id)) return false
      return selectableTypes.has(normalizeMenuType(item.type))
    })
    const tree = buildMenuTree(availableMenus) as MenuTreeNode[]

    if (!props.includeRoot) {
      return tree
    }

    return [
      {
        id: '0',
        rootId: '0',
        name: props.rootLabel,
        routeKey: 'root',
        orderSort: -1,
        isIframe: false,
        path: '',
        visible: true,
        type: 'TABLE',
        children: tree
      }
    ]
  })

  onMounted(loadMenus)

  async function loadMenus() {
    loading.value = true
    try {
      rawMenus.value = await fetchGetMenuList()
    } catch (error) {
      ElMessage.error('菜单树加载失败')
      rawMenus.value = []
    } finally {
      loading.value = false
    }
  }
</script>
