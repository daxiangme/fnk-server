<template>
  <app-container>
      <BaseTable :columns="columns" :loading="pageState.loading" :data="pageState.tableData" :get-data="onGetData" :search-form-items="formFiledList"
                 is-add-action title="系统用户" desc="系统用户" @add="onAdd" @edit="onEdit" @delete="onDelete"/>
      <user-drawer v-model:show="pageState.drawerVisible" :is-update="pageState.isUpdate" :data="pageState.editData"
                   @success="onGetData"
                   :form-items="formFiledList"></user-drawer>
  </app-container>
</template>
<script setup lang="ts">

import BaseForm from "@/components/basic/form/index.vue"
import BaseTable from "@/components/basic/table/index.vue";
import {computed, reactive, ref} from "vue";
import {BaseFormItemProps} from "@/components/basic/form/index";
import {ITableColumn} from "@/components/basic/table/index";
import {deleteSystemUser, queryAllSystemRole, querySystemUser} from "@/service";
import UserDrawer from "@/views/system/user/user-drawer.vue";

const pageInfo = reactive({
  path: "/system/admin/user",
  name: "系统用户",
});
const formRef = ref()
const formData = reactive({})

const genderOptions = [
  {label: '男', value: 'MAN'},
  {label: '女', value: 'WOMAN'},
  {label: '未知', value: 'OTHER'},
]
const roleListOptions = ref([])
const formFiledList = computed(()=>[
  {field: 'username', label: '用户名称', filedType: 'string', isSearch: true, required: true},
  {field: 'phone', label: '手机号', filedType: 'string', isSearch: true, required: true},
  {field: 'password', label: '密码',labelMessage:`${pageState.isUpdate ? '密码不为空则自动更新密码':'' }`, filedType: 'string', isSearch: false, required: !pageState.isUpdate},
  // {field: 'avatar', label: '用户头像', filedType: 'string', isSearch: false, required: true},
  {field: 'sex', label: '用户性别', filedType: 'select', isSearch: false, required: true,filedOptions:{options:genderOptions}},
  {field: 'roleIdList', label: '用户角色', filedType: 'select', isSearch: false, required: true,filedOptions:{options:roleListOptions.value,multiple:true}},
  // {field: 'loginIp', label: '登录ip', filedType: 'string', isSearch: false, required: true},
  // {field: 'deptId', label: '部门ID', filedType: 'string', isSearch: false, required: true},
  {field: 'status', label: '用户状态', filedType: 'switch', isSearch: false, required: true},
] as BaseFormItemProps[])






const columns: Array<ITableColumn> = [
  {field: 'id', title: 'id', type: 'string'},
  {field: 'phone', title: '手机号', type: 'string'},
  {field: 'username', title: '用户名称', type: 'string'},
  // {field: 'avatar', title: '用户头像', type: 'string'},
  {field: 'sex', title: '用户性别', type: 'string'},
  {field: 'loginIp', title: '登录ip', type: 'string'},
  // {field: 'deptId', title: '部门ID', type: 'string'},
  {field: 'status', title: '用户状态', type: 'switch', typeOptions: {switchTag: {trueText: '正常', falseText: '不可用'}}},
]
const pageState = reactive({
  drawerVisible: false,
  isUpdate: false,
  editData: null,
  loading: false,
  tableData: [],
})


async function onGetRoleList() {
  if (roleListOptions.value.length > 0) {
    return
  }
  const {data, error} = await queryAllSystemRole()
  if (!error) {
    roleListOptions.value = data.map((item: any) => {
      return {label: item.roleName, value: item.id}
    })
  }
}
/**
 * 获取数据
 * @param params
 */
async function onGetData(params?: any) {
  pageState.loading = true
  const {data, error} = await querySystemUser(params)
  if (!error) {
    pageState.tableData = data
  }
  pageState.loading = false
}

/**
 * 添加
 *
 */
async function onAdd() {
  await onGetRoleList()
  pageState.drawerVisible = true
  pageState.isUpdate = false
}

/**
 * 编辑
 * @param row
 */
async function onEdit(row: any) {

  await onGetRoleList()
  pageState.drawerVisible = true
  pageState.isUpdate = true
  pageState.editData = row
}

/**
 * 删除
 * @param row
 */
async function onDelete(row: any) {
  const {error} = await deleteSystemUser(row.id)
  if (!error) {
    // @ts-ignore
    window.$message.success(`删除${row?.username??''}成功`)
    await onGetData()
  }
}
</script>

<style scoped>

</style>