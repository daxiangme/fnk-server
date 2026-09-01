<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="query"
      :items="searchItems"
      :show-expand="false"
      @search="loadData"
      @reset="resetQuery"
    />

    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:showSearchBar="showSearchBar"
        :loading="loading"
        @refresh="loadData"
      >
        <template #left>
          <ElButton type="primary" v-auth="'system:menu:create'" @click="openCreate()">
            新增菜单
          </ElButton>
          <ElButton
            v-auth="'system:menu:update'"
            :loading="refreshingCache"
            @click="refreshPermissionCache"
          >
            <ArtSvgIcon icon="ri:refresh-line" class="mr-1" />
            刷新缓存数据
          </ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable
        row-key="id"
        default-expand-all
        :loading="loading"
        :data="treeData"
        :columns="columns"
        :pagination="undefined"
      />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑菜单' : '新增菜单'" width="620px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="100px">
        <ElFormItem label="类型" prop="type">
          <ElRadioGroup v-model="form.type">
            <ElRadioButton label="TABLE">目录</ElRadioButton>
            <ElRadioButton label="MENU">菜单</ElRadioButton>
            <ElRadioButton label="BUTTON">按钮</ElRadioButton>
          </ElRadioGroup>
        </ElFormItem>
        <ElFormItem label="上级">
          <ElTreeSelect
            v-model="form.rootId"
            :data="parentOptions"
            node-key="id"
            check-strictly
            clearable
            :props="{ label: 'name', children: 'children', value: 'id' }"
            placeholder="顶级目录"
          />
        </ElFormItem>
        <ElFormItem label="名称" prop="name">
          <ElInput v-model="form.name" placeholder="请输入名称" />
        </ElFormItem>
        <ElFormItem label="路由 Key" prop="routeKey">
          <ElInput v-model="form.routeKey" placeholder="例如 system_user" />
        </ElFormItem>
        <ElFormItem label="路径/链接" prop="path">
          <ElInput
            v-model="form.path"
            :disabled="form.type === 'BUTTON'"
            :placeholder="form.type === 'TABLE' ? '目录可为空' : '例如 /system/user'"
          />
        </ElFormItem>
        <ElFormItem label="权限标识" prop="permission">
          <ElInput v-model="form.permission" placeholder="例如 system:user:view" />
        </ElFormItem>
        <ElFormItem label="图标">
          <ElInput v-model="form.icon" placeholder="Iconify 图标，例如 ri:user-line" />
        </ElFormItem>
        <ElFormItem label="排序">
          <ElInputNumber v-model="form.orderSort" :min="0" />
        </ElFormItem>
        <ElFormItem label="内嵌页面">
          <ElSwitch v-model="form.isIframe" :disabled="form.type !== 'MENU'" />
        </ElFormItem>
        <ElFormItem label="显示">
          <ElSwitch v-model="form.visible" />
        </ElFormItem>
        <ElFormItem label="备注">
          <ElInput v-model="form.remark" type="textarea" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="saving" @click="submit">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import {
    ElMessage,
    ElMessageBox,
    ElTag,
    type FormInstance,
    type FormRules
  } from 'element-plus'
  import {
    fetchDeleteMenu,
    fetchGetMenuList,
    fetchRefreshMenuPermissionCache,
    fetchSaveMenu
  } from '@/api/system-manage'
  import { buildMenuTree, normalizeMenuType } from '@/utils/backend-menu'
  import { useTableColumns } from '@/hooks/core/useTableColumns'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'

  defineOptions({ name: 'SystemMenu' })

  const loading = ref(false)
  const saving = ref(false)
  const refreshingCache = ref(false)
  const showSearchBar = ref(true)
  const dialogVisible = ref(false)
  const formRef = ref<FormInstance>()
  const rawMenus = ref<Api.SystemManage.MenuItem[]>([])

  const query = reactive({
    name: '',
    permission: ''
  })

  const form = reactive({
    id: '',
    rootId: '0',
    name: '',
    routeKey: '',
    orderSort: 0,
    isIframe: false,
    path: '',
    icon: '',
    localIcon: '',
    visible: true,
    permission: '',
    type: 'MENU' as 'TABLE' | 'MENU' | 'BUTTON',
    remark: ''
  })

  const searchItems: SearchFormItem[] = [
    {
      label: '菜单名称',
      key: 'name',
      type: 'input',
      props: { clearable: true, placeholder: '请输入菜单名称' }
    },
    {
      label: '权限标识',
      key: 'permission',
      type: 'input',
      props: { clearable: true, placeholder: '请输入权限标识' }
    }
  ]

  const treeData = computed(() => buildMenuTree(rawMenus.value))
  const parentOptions = computed(() =>
    buildMenuTree(rawMenus.value.filter((item) => normalizeMenuType(item.type) !== 'BUTTON'))
  )

  const { columns, columnChecks } = useTableColumns<Api.SystemManage.MenuItem>(() => [
    { prop: 'name', label: '名称', minWidth: 180 },
    {
      prop: 'type',
      label: '类型',
      width: 90,
      formatter: (row) => h(ElTag, { type: typeTag(row) }, () => typeText(row))
    },
    { prop: 'routeKey', label: '路由 Key', minWidth: 150 },
    { prop: 'path', label: '路径/链接', minWidth: 180 },
    { prop: 'permission', label: '权限标识', minWidth: 180 },
    { prop: 'orderSort', label: '排序', width: 80 },
    {
      prop: 'visible',
      label: '显示',
      width: 80,
      formatter: (row) =>
        h(ElTag, { type: row.visible ? 'success' : 'info' }, () =>
          row.visible ? '显示' : '隐藏'
        )
    },
    {
      type: 'operation',
      prop: 'operation',
      label: '操作',
      width: 220,
      fixed: 'right',
      actions: [
        {
          key: 'create',
          label: '新增',
          permission: 'system:menu:create',
          visible: (row) => normalizeMenuType(row.type) !== 'BUTTON',
          onClick: (row) => openCreate(row)
        },
        {
          key: 'edit',
          label: '编辑',
          permission: 'system:menu:update',
          onClick: (row) => openEdit(row)
        },
        {
          key: 'delete',
          label: '删除',
          type: 'error',
          permission: 'system:menu:delete',
          onClick: (row) => remove(row)
        }
      ]
    }
  ])

  const rules: FormRules = {
    name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
    routeKey: [{ required: true, message: '请输入路由 Key', trigger: 'blur' }],
    path: [
      {
        validator: (_rule, value, callback) => {
          if (form.type === 'MENU' && !value) {
            callback(new Error('菜单必须填写页面路径或链接'))
            return
          }
          callback()
        },
        trigger: ['blur', 'change']
      }
    ],
    permission: [
      {
        validator: (_rule, value, callback) => {
          if (form.type !== 'TABLE' && !value) {
            callback(new Error('菜单和按钮必须填写权限标识'))
            return
          }
          callback()
        },
        trigger: 'blur'
      }
    ]
  }

  onMounted(loadData)

  watch(
    () => form.type,
    (type) => {
      if (type === 'TABLE') {
        form.permission = ''
        form.isIframe = false
      }

      if (type === 'BUTTON') {
        form.path = ''
        form.icon = ''
        form.localIcon = ''
        form.visible = false
        form.isIframe = false
      }

      nextTick(() => formRef.value?.clearValidate(['path', 'permission']))
    }
  )

  async function loadData() {
    loading.value = true
    try {
      rawMenus.value = await fetchGetMenuList({
        name: query.name || undefined,
        permission: query.permission || undefined
      })
    } finally {
      loading.value = false
    }
  }

  function resetQuery() {
    query.name = ''
    query.permission = ''
    loadData()
  }

  function resetForm() {
    Object.assign(form, {
      id: '',
      rootId: '0',
      name: '',
      routeKey: '',
      orderSort: 0,
      isIframe: false,
      path: '',
      icon: '',
      localIcon: '',
      visible: true,
      permission: '',
      type: 'MENU',
      remark: ''
    })
  }

  function openCreate(parent?: Api.SystemManage.MenuItem) {
    resetForm()
    if (parent) {
      if (normalizeMenuType(parent.type) === 'BUTTON') {
        ElMessage.warning('按钮权限不能作为上级菜单')
        return
      }
      form.rootId = parent.id
      form.type = normalizeMenuType(parent.type) === 'MENU' ? 'BUTTON' : 'MENU'
    }
    dialogVisible.value = true
  }

  function openEdit(row: Api.SystemManage.MenuItem) {
    resetForm()
    Object.assign(form, row, {
      rootId: row.rootId || '0',
      type: normalizeMenuType(row.type)
    })
    dialogVisible.value = true
  }

  async function submit() {
    if (!formRef.value) return
    await formRef.value.validate()
    saving.value = true
    try {
      await fetchSaveMenu(normalizeSubmitData())
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadData()
    } finally {
      saving.value = false
    }
  }

  function remove(row: Api.SystemManage.MenuItem) {
    ElMessageBox.confirm(`确定删除菜单「${row.name}」吗？子节点和角色关联会同步处理。`, '删除确认', {
      type: 'warning'
    })
      .then(async () => {
        await fetchDeleteMenu(row.id)
        ElMessage.success('删除成功')
        loadData()
      })
      .catch(() => undefined)
  }

  async function refreshPermissionCache() {
    refreshingCache.value = true
    try {
      await fetchRefreshMenuPermissionCache()
      ElMessage.success('权限缓存刷新成功')
      await loadData()
    } finally {
      refreshingCache.value = false
    }
  }

  function typeText(row: Api.SystemManage.MenuItem) {
    const type = normalizeMenuType(row.type)
    return type === 'TABLE' ? '目录' : type === 'MENU' ? '菜单' : '按钮'
  }

  function typeTag(row: Api.SystemManage.MenuItem) {
    const type = normalizeMenuType(row.type)
    return type === 'TABLE' ? 'info' : type === 'MENU' ? 'primary' : 'danger'
  }

  function normalizeSubmitData() {
    const data = { ...form, rootId: form.rootId || '0' }

    if (data.type === 'TABLE') {
      data.permission = ''
      data.isIframe = false
    }

    if (data.type === 'BUTTON') {
      data.path = ''
      data.icon = ''
      data.localIcon = ''
      data.visible = false
      data.isIframe = false
    }

    return data
  }

</script>
