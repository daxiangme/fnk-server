<template>
  <div class="art-full-height">
    <UserSearch
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
          <ElButton type="primary" v-auth="'system:user:create'" @click="openCreate">
            新增用户
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

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="560px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="90px">
        <ElFormItem label="用户名" prop="username">
          <ElInput v-model="form.username" placeholder="请输入用户名" />
        </ElFormItem>
        <ElFormItem label="手机号" prop="phone">
          <ElInput v-model="form.phone" placeholder="请输入手机号" />
        </ElFormItem>
        <ElFormItem label="密码" prop="password">
          <ElInput
            v-model="form.password"
            type="password"
            show-password
            :placeholder="form.id ? '不填则不修改' : '请输入密码'"
          />
        </ElFormItem>
        <ElFormItem label="性别">
          <ElSelect v-model="form.sex" clearable placeholder="请选择">
            <ElOption label="男" value="MAN" />
            <ElOption label="女" value="WOMAN" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="角色" prop="roleIdList">
          <ElSelect v-model="form.roleIdList" multiple clearable placeholder="请选择角色">
            <ElOption
              v-for="role in roleOptions"
              :key="role.id || role.roleId"
              :label="role.roleName"
              :value="role.id || String(role.roleId || '')"
            />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="form.status" />
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
  import { ElTag } from 'element-plus'
  import {
    fetchDeleteUser,
    fetchGetAllRoles,
    fetchGetUserList,
    fetchGetUserRoles,
    fetchSaveUser
  } from '@/api/system-manage'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import UserSearch from './modules/user-search.vue'
  import type { FormRules } from 'element-plus'

  defineOptions({ name: 'SystemUser' })

  interface UserForm {
    id: string
    username: string
    phone: string
    password: string
    sex: string
    status: boolean
    roleIdList: string[]
  }

  const showSearchBar = ref(true)
  const roleOptions = ref<Api.SystemManage.RoleListItem[]>([])

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
  } = useCrudTable<Api.SystemManage.UserListItem, Api.SystemManage.UserSearchParams, UserForm>({
    immediate: false,
    defaultQuery: () => ({
      page: 1,
      pageSize: 10,
      username: undefined,
      phone: undefined,
      status: undefined
    }),
    defaultForm: () => ({
      id: '',
      username: '',
      phone: '',
      password: '',
      sex: '',
      status: true,
      roleIdList: []
    }),
    listApi: fetchGetUserList,
    saveApi: (value) => fetchSaveUser(value),
    removeApi: (row) => fetchDeleteUser(row.id),
    getEditForm: async (row) => ({
      ...row,
      password: '',
      status: row.status === true || row.status === 'true',
      roleIdList: await fetchGetUserRoles(row.id)
    }),
    removeOptions: {
      message: (row) => `确定删除用户「${row.username}」吗？`,
      successMessage: '删除成功'
    },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'username', label: '用户名', minWidth: 140 },
      { prop: 'phone', label: '手机号', minWidth: 140 },
      { prop: 'sex', label: '性别', width: 90, formatter: (row) => sexText(row.sex) },
      { prop: 'loginIp', label: '登录 IP', minWidth: 140 },
      {
        prop: 'status',
        label: '状态',
        width: 90,
        formatter: (row) =>
          h(
            ElTag,
            { type: isEnabled(row.status) ? 'success' : 'info' },
            () => (isEnabled(row.status) ? '启用' : '禁用')
          )
      },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 150,
        fixed: 'right',
        actions: [
          {
            key: 'edit',
            label: '编辑',
            permission: 'system:user:update',
            onClick: (row) => openEdit(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'system:user:delete',
            onClick: (row) => remove(row)
          }
        ]
      }
    ]
  })

  const rules: FormRules = {
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
    roleIdList: [{ required: true, message: '请选择角色', trigger: 'change' }]
  }

  onMounted(() => {
    loadRoles()
    loadData()
  })

  async function loadRoles() {
    roleOptions.value = await fetchGetAllRoles()
  }

  function isEnabled(status: boolean | string) {
    return status === true || status === 'true'
  }

  function sexText(value?: string) {
    if (value === 'MAN') return '男'
    if (value === 'WOMAN') return '女'
    return value || '-'
  }
</script>
