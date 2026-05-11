<template>
    <app-container>
        <BaseTable :columns="columns" :loading="pageState.loading" :data="pageState.tableData" :get-data="onGetData"
                   :search-form-items="formFiledList"
                   is-add-action @delete="onDelete"
                   title="角色信息" desc="角色信息" @add="onAdd" @edit="onEdit"/>

        <role-drawer v-model:show="pageState.drawerVisible" :is-update="pageState.isUpdate" :data="pageState.editData"
                     @success="onGetData"
                     :form-items="formFiledList"></role-drawer>
    </app-container>
</template>

<script lang="ts" setup>
import BaseForm from "@/components/basic/form/index.vue"
import BaseTable from "@/components/basic/table/index.vue";
import {computed, onMounted, reactive, Ref, ref} from "vue";
import {BaseFormItemProps} from "@/components/basic/form/index";
import {ITableColumn} from "@/components/basic/table/index";
import {deleteSystemRole, queryAllSystemMenu, querySystemRole} from "@/service";
import RoleDrawer from "@/views/system/role/role-drawer.vue";

const formRef = ref()
const formData = reactive({})
const menuList: Ref = ref([])

const formFiledList = computed(() => [
    {field: 'roleName', label: '名称', filedType: 'string', isSearch: true, required: true},
    {field: 'roleKey', label: '角色key', filedType: 'string', isSearch: true, required: true},
    {
        field: 'roleScope',
        label: '权限数据',
        filedType: 'select',
        isSearch: false,
        required: true,
        slot: 'roleScope',
        filedOptions: {options: menuList.value, multiple: true}
    },
    {field: 'orderSort', label: '显示排序', filedType: 'number', isSearch: false, required: true},
    {field: 'status', label: '状态', filedType: 'switch', isSearch: false, required: true},
] as BaseFormItemProps[])

const columns: Array<ITableColumn> = [
    {field: 'id', title: 'id', type: 'string'},
    {field: 'roleName', title: '名称', type: 'string'},
    {field: 'roleKey', title: '角色key', type: 'string'},
    {field: 'orderSort', title: '显示排序', type: 'number'},
    // {field: 'roleScope', title: '权限数据', type: 'string'},
    {field: 'status', title: '状态', type: 'switch'},
]
const pageState = reactive({
    drawerVisible: false,
    isUpdate: false,
    editData: null,
    loading: false,
    tableData: [],
})

async function onGetData(params?: any) {
    pageState.loading = true
    const {data, error} = await querySystemRole(params)
    if (!error) {
        pageState.tableData = data
    }
    pageState.loading = false

}

async function getMenuList() {
    if (menuList.value.length > 0) {
        return
    }
    const {data, error} = await queryAllSystemMenu()
    if (!error) {
        menuList.value = groupMenuItems(data)
    }
}

async function onAdd() {
    await getMenuList()
    pageState.drawerVisible = true
}

async function onEdit(row: any) {
    await getMenuList()
    pageState.drawerVisible = true
    pageState.isUpdate = true
    pageState.editData = row
}

async function onDelete(row: any) {
    const {data, error} = await deleteSystemRole(row.id)
    if (!error) {
        // @ts-ignore
        window.$message.success(`删除${row?.roleName ?? ''}成功,共删除${data}条数据`)
        await onGetData()
    }
}


function groupMenuItems(menuItems: any[], parentId: number | string = '0'): any[] {
    const groupedItems: any[] = [];

    const childItems = menuItems.filter(item => item.rootId === parentId);
    childItems.forEach(childItem => {
        const index = menuItems.indexOf(childItem);
        if (index !== -1) {
            menuItems.splice(index, 1); // 从原始数据中移除已使用的数据
        }
        let items = groupMenuItems(menuItems, childItem.id);
        if (items.length > 0) {
            childItem.children = items;
        }
        groupedItems.push(childItem);
    });
    return groupedItems;
}

onMounted(async () => {

})
</script>