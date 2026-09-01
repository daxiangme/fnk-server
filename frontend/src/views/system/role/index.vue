<template>
  <div class="art-full-height">
    <RoleSearch
      v-show="showSearchBar"
      v-model="query"
      @search="loadData"
      @reset="resetQuery"
    />

    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:showSearchBar="showSearchBar"
        :loading="loading"
        @refresh="refreshData"
      >
        <template #left>
          <ElButton type="primary" v-auth="'system:role:create'" @click="openCreate">
            新增角色
          </ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑角色' : '新增角色'" width="520px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="90px">
        <ElFormItem label="角色名称" prop="roleName">
          <ElInput v-model="form.roleName" placeholder="请输入角色名称" />
        </ElFormItem>
        <ElFormItem label="角色标识" prop="roleKey">
          <ElInput
            v-model="form.roleKey"
            :disabled="isEditingSuperAdmin"
            placeholder="例如 SuperAdmin"
          />
        </ElFormItem>
        <ElFormItem label="排序">
          <ElInputNumber v-model="form.orderSort" :min="0" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="form.status" :disabled="isEditingSuperAdmin" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="saving" @click="submit">保存</ElButton>
      </template>
    </ElDialog>

    <ElDialog v-model="permissionVisible" title="角色权限" width="620px">
      <ElAlert
        v-if="permissionReadonly"
        class="mb-3"
        type="info"
        show-icon
        :closable="false"
        title="超级管理员为系统内置角色，默认拥有全部权限，权限不可修改。"
      />
      <ElTree
        ref="treeRef"
        :data="permissionMenuTree"
        node-key="id"
        show-checkbox
        default-expand-all
        :props="treeProps"
      >
        <template #default="{ data: item }">
          <span class="flex items-center gap-2">
            <ElTag size="small" :type="typeTag(item)">{{ typeText(item) }}</ElTag>
            <span>{{ item.name }}</span>
            <span class="text-xs text-gray-400">{{ item.permission }}</span>
          </span>
        </template>
      </ElTree>
      <template #footer>
        <ElButton @click="permissionVisible = false">取消</ElButton>
        <ElButton
          v-if="!permissionReadonly"
          type="primary"
          :loading="saving"
          @click="submitPermission"
        >
          保存权限
        </ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import {
    ElMessage,
    ElTag,
    type ElTree,
    type FormRules
  } from 'element-plus'
  import {
    fetchDeleteRole,
    fetchGetMenuList,
    fetchGetRoleList,
    fetchGetRoleMenus,
    fetchSaveRole
  } from '@/api/system-manage'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import { buildMenuTree, normalizeMenuType } from '@/utils/backend-menu'
  import RoleSearch from './modules/role-search.vue'

  defineOptions({ name: 'SystemRole' })

  const SUPER_ADMIN_ROLE_KEY = 'SuperAdmin'
  const WILDCARD_PERMISSION = '*'

  type RoleForm = {
    id: string
    roleName: string
    roleKey: string
    orderSort: number
    status: boolean
    roleScope: string[]
  }

  type PermissionMenuItem = Api.SystemManage.MenuItem & {
    disabled?: boolean
    children?: PermissionMenuItem[]
  }

  const showSearchBar = ref(true)
  const permissionVisible = ref(false)
  const permissionReadonly = ref(false)
  const currentRole = ref<Api.SystemManage.RoleListItem>()
  const menuTree = ref<Api.SystemManage.MenuItem[]>([])
  const treeRef = ref<InstanceType<typeof ElTree>>()

  const {
    loading,
    saving,
    dialogVisible,
    formRef,
    query,
    form,
    data,
    pagination,
    columns,
    columnChecks,
    loadData,
    resetQuery,
    openCreate,
    openEdit,
    submit,
    remove,
    handleSizeChange,
    handleCurrentChange,
    refreshData
  } = useCrudTable<Api.SystemManage.RoleListItem, Api.SystemManage.RoleSearchParams, RoleForm>({
    defaultQuery: () => ({
      page: 1,
      pageSize: 10,
      roleName: undefined,
      roleKey: undefined,
      status: undefined
    }),
    defaultForm: () => ({
      id: '',
      roleName: '',
      roleKey: '',
      orderSort: 0,
      status: true,
      roleScope: []
    }),
    listApi: fetchGetRoleList,
    saveApi: (value) => fetchSaveRole(value),
    removeApi: (row) => fetchDeleteRole(getRoleId(row)),
    getEditForm: async (row) => ({
      id: getRoleId(row),
      roleName: row.roleName,
      roleKey: row.roleKey || '',
      orderSort: row.orderSort || 0,
      status: row.status !== false,
      roleScope: await fetchGetRoleMenus(getRoleId(row))
    }),
    removeOptions: {
      message: (row) => `确定删除角色「${row.roleName}」吗？`
    },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'roleName', label: '角色名称', minWidth: 140 },
      { prop: 'roleKey', label: '角色标识', minWidth: 160 },
      { prop: 'orderSort', label: '排序', width: 90 },
      {
        prop: 'status',
        label: '状态',
        width: 90,
        formatter: (row) =>
          h(ElTag, { type: row.status !== false ? 'success' : 'info' }, () =>
            row.status !== false ? '启用' : '禁用'
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
            key: 'permission',
            label: '权限',
            permission: 'system:role:permission',
            onClick: (row) => openPermission(row)
          },
          {
            key: 'edit',
            label: '编辑',
            permission: 'system:role:update',
            onClick: (row) => openEdit(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'system:role:delete',
            disabled: (row) => isSuperAdmin(row),
            onClick: (row) => remove(row)
          }
        ]
      }
    ]
  })

  const isEditingSuperAdmin = computed(() => form.roleKey === SUPER_ADMIN_ROLE_KEY)
  const permissionMenuTree = computed<PermissionMenuItem[]>(() =>
    withTreeDisabled(menuTree.value, permissionReadonly.value)
  )

  const treeProps = {
    label: 'name',
    children: 'children',
    disabled: 'disabled'
  }

  const rules: FormRules = {
    roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
    roleKey: [{ required: true, message: '请输入角色标识', trigger: 'blur' }]
  }

  onMounted(loadMenus)

  async function loadMenus() {
    menuTree.value = buildMenuTree(await fetchGetMenuList())
  }

  async function openPermission(row: Api.SystemManage.RoleListItem) {
    currentRole.value = row
    if (!menuTree.value.length) {
      await loadMenus()
    }
    const roleMenus = await fetchGetRoleMenus(getRoleId(row))
    permissionReadonly.value = isSuperAdmin(row) || roleMenus.includes(WILDCARD_PERMISSION)
    permissionVisible.value = true
    await setTreeCheckedKeys(permissionReadonly.value ? getAllMenuIds(menuTree.value) : roleMenus)
  }

  async function submitPermission() {
    if (!currentRole.value || permissionReadonly.value) return
    saving.value = true
    try {
      const checked = treeRef.value?.getCheckedKeys(false).map(String) || []
      const halfChecked = treeRef.value?.getHalfCheckedKeys().map(String) || []
      await fetchSaveRole({
        ...currentRole.value,
        id: getRoleId(currentRole.value),
        roleScope: Array.from(new Set([...checked, ...halfChecked]))
      })
      ElMessage.success('权限保存成功')
      permissionVisible.value = false
      loadData()
    } finally {
      saving.value = false
    }
  }

  function getRoleId(row: Api.SystemManage.RoleListItem) {
    return row.id || String(row.roleId || '')
  }

  function isSuperAdmin(row?: Partial<Api.SystemManage.RoleListItem>) {
    return row?.roleKey === SUPER_ADMIN_ROLE_KEY
  }

  function typeText(row: Api.SystemManage.MenuItem) {
    const type = normalizeMenuType(row.type)
    return type === 'TABLE' ? '目录' : type === 'MENU' ? '菜单' : '按钮'
  }

  function typeTag(row: Api.SystemManage.MenuItem) {
    const type = normalizeMenuType(row.type)
    return type === 'TABLE' ? 'info' : type === 'MENU' ? 'primary' : 'danger'
  }

  function getAllMenuIds(nodes: Api.SystemManage.MenuItem[]) {
    const ids: string[] = []
    const walk = (items: Api.SystemManage.MenuItem[]) => {
      items.forEach((item) => {
        if (item.id) ids.push(String(item.id))
        if (item.children?.length) walk(item.children)
      })
    }
    walk(nodes)
    return ids
  }

  function withTreeDisabled(
    nodes: Api.SystemManage.MenuItem[],
    disabled: boolean
  ): PermissionMenuItem[] {
    return nodes.map((item) => ({
      ...item,
      disabled,
      children: item.children?.length ? withTreeDisabled(item.children, disabled) : undefined
    }))
  }

  async function setTreeCheckedKeys(keys: string[]) {
    await nextTick()
    await new Promise<void>((resolve) => setTimeout(resolve, 0))
    treeRef.value?.setCheckedKeys(keys)
  }

</script>
