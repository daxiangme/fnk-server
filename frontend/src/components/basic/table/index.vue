<template>
  <div class="flex-col h-full gap-12px">
    <n-card size="small" v-if="searchFormFields.length" :bordered="false">
      <n-collapse>
        <n-collapse-item title="搜索条件">
          <BaseForm
              :data="formData"
              :items="searchFormFields"
              inline
              ref="formDom"
              :label-align="props.labelAlign"
              :label-width="props.labelWidth"
              is-search
              submit-text="搜索"
              :grid-props="{ cols: props.searchCols }"
              @submit="onFormSubmit"
              @reset="onGetTableData"
          />
        </n-collapse-item>
      </n-collapse>
    </n-card>
    <n-card :bordered="false" :content-style="{margin:0,padding:'16px' }" class="mb-12px mt-10px">
      <div class="flex items-center justify-between w-full mb-12px" ref="headDom">
        <!--     左     -->
        <div class="flex-col">
          <n-space align="start">
            <template v-if="props.title">
              <n-space align="center" size="small">
                <p class="text-16px h-22px font-bold">{{ props.title }}</p>
                <n-tooltip trigger="hover" v-if="props.desc">
                  <template #trigger>
                    <icon-line-md:question-circle class="text-18px text-gray-400 cursor-pointer"/>
                  </template>
                  {{ props.desc }}
                </n-tooltip>
              </n-space>
            </template>
            <template v-if="!props.title && props.isAddAction">
              <n-button @click='emits("add")' type='primary'>
                <icon-line-md:plus class='mr-4px text-20px'/>
                新增
              </n-button>
            </template>
          </n-space>
        </div>

        <!--     右     -->
        <n-space align="end">
          <n-button @click='emits("add")' dashed type='primary' v-if="props.title && props.isAddAction">
            <icon-line-md:plus class='mr-4px text-20px'/>
            新增数据
          </n-button>
          <n-button @click='onGetTableData' type='primary'>
            <icon-line-md:rotate-270 class='mr-4px text-16px' :class="{ 'animate-spin': props.loading }"/>
            刷新表格
          </n-button>
        </n-space>
      </div>
      <n-data-table :columns="tableColumns" :row-key="(rowData:any)=>rowData[props.rowKey]" :data="tableData"
                    :loading="props.loading" striped :pagination="false"
                    :max-height="tableHeight" :scroll-x="tableHeight">
        <template #empty>
          <div class="flex-col-center">
            <icon-local-empty
                class="text-300px text-primary"
            ></icon-local-empty>
            <p
                class="text-20px text-primary"
                v-if="!props.emptyText"
            >
              无{{ props.title }}数据~
            </p>
            <p class="text-20px text-primary" v-else>
              {{ props.emptyText }}
            </p>
          </div>
        </template>
      </n-data-table>
    </n-card>
  </div>
</template>
<script setup lang="tsx">
import {
  computed,
  nextTick,
  onMounted,
  PropType,
  reactive,
  Ref,
  ref,
  unref,
  watch,
} from "vue";
import {ITableColumn} from "@/components/basic/table/index";
import {BaseFormItemProps} from "@/components/basic/form/index";
import {generateTableColumnRender} from "@/components/basic/table/render";
import BaseForm from "@/components/basic/form/index.vue";
import {
  DataTableColumn,
  NButton,
  NSpace,
  PaginationProps,
  NPopconfirm,
} from "naive-ui";

defineOptions({name: "BaseTable"});

const props = defineProps({
  title: {
    type: String,
    default: null,
  },
  desc: {
    type: String,
    default: null,
  },
  rowKey: {
    type: String,
    default: "id",
  },
  data: {
    type: Object as PropType<any | any[]>,
  },
  loading: {
    type: Boolean,
    default: false,
  },
  columns: {
    type: Array as PropType<Array<ITableColumn>>,
    default: [],
  },
  searchFormItems: {
    type: Array as PropType<Array<BaseFormItemProps>>,
    default: [],
  },
  rowAction: Function as PropType<(row: any, rowIndex: number) => any>,
  getData: Function as PropType<({}) => any>,
  // 添加按钮
  isAddAction: {
    type: Boolean,
    default: false,
  },
  // 列表数据取值key
  dataKey: {
    type: String,
    default: "records",
  },
  // 默认操作按钮
  isAction: {
    type: Boolean,
    default: true,
  },
  // 编辑按钮
  isEditAction: {
    type: Boolean,
    default: true,
  },
  // 删除按钮
  isDeleteAction: {
    type: Boolean,
    default: true,
  },
  // 类型 page：分页列表  list：列表
  type: {
    type: String as PropType<"page" | "list">,
    default: "page",
  },
  // 搜索个数
  searchCols: {
    type: Number,
    default: 4,
  },
  isSelect: {
    type: Boolean,
    default: false,
  },
  emptyText: {
    type: String,
    default: null,
  },
  // 未使用
  loadingText: {
    type: String,
    default: null,
  },
  labelAlign: {
    type: String as PropType<"left" | "right">,
    default: "right",
  },
  labelWidth: {
    type: Number,
    default: 120,
  },
});

const emits = defineEmits<{
  /**
   * 获取数据 返回data
   * @param e 事件名
   * @param data 数据
   */
  (e: "getData", data: any): void;
  /**
   * 编辑
   * @param e 事件名
   * @param data 数据
   */
  (e: "edit", data: any): void;
  /**
   * 删除
   * @param e 事件名
   * @param data 数据
   */
  (e: "delete", data: any): void;
  /**
   * 新增
   * @param e 事件名
   */
  (e: "add"): void;
}>();

const formData = reactive({});

// 分页数据
const pagination = reactive({
  page: parseInt(props.data?.current ?? 1),
  pageSize: parseInt(props.data?.size ?? 10),
  pageCount: parseInt(props.data?.total ?? 1),
  pageSizes: [10, 20, 30, 40, 50],
  showSizePicker: true,
});

const tableColumns = computed(() => {
  const columns = props.columns.map((item: ITableColumn) => {
    return {
      key: item.field,
      title: item.title,
      align: 'center',
      titleAlign: 'center',
      render: item.render ?? generateTableColumnRender(item),
      width: item.labelWidth ?? 100,
      fixed: item.fixed ?? false,
      ...item
    } as any
  })
  if (props.isAction) {
    columns.push({
      title: defaultAction.title,
      key: defaultAction.field,
      align: "center",
      titleAlign: "center",
      render: defaultAction.render,
      width: defaultAction.labelWidth ?? 100,
      ...defaultAction,
    } as any);
  }
  if (props.isSelect) {
    return [
      {
        type: "selection",
      },
      ...columns,
    ];
  }
  return columns;
});
const defaultAction = reactive<ITableColumn>({
  field: "actions",
  fixed: "right",
  title: "操作",
  labelWidth: 140,
  render: (row: any, rowIndex: number) => {
    return (
        <NSpace size="small" justify="center">
          {props.rowAction?.(row, rowIndex)}
          {props.isEditAction && (
              <NButton
                  onClick={() => emits("edit", row)}
                  secondary
                  type="info"
              >
                编辑
              </NButton>
          )}
          {props.isDeleteAction && (
              <NPopconfirm
                  onPositiveClick={() => emits("delete", row)}
                  v-slots={{
                    trigger: () => {
                      return (
                          <NButton secondary type="error">
                            删除
                          </NButton>
                      );
                    },
                  }}
              >
                确认删除吗？
              </NPopconfirm>
          )}
        </NSpace>
    );
  },
});

const tableData = computed(() => {
  if (!props.data) {
    return [];
  }
  if (props.type === "list") {
    return props.data;
  } else {
    return props.data[props.dataKey];
  }
});

const searchFormFields = computed(() => {
  if (!props.searchFormItems) {
    return [];
  }
  const items = [];
  Object.assign(items, props.searchFormItems);
  return items
      .filter((item) => item?.isSearch)
      .map((item) => {
        if (item?.filedOptions?.disabled) {
          item.filedOptions.disabled = false;
        }
        return item;
      });
});

const headDom = ref();
const formDom = ref();

function onPageChange(page: number) {
  pagination.page = page;
  onGetTableData();
}

function onPageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize;
  onGetTableData();
}

function onFormSubmit(state: boolean) {
  if (state) {
    onGetTableData();
  }
}

function onGetTableData() {
  if (props.type === "page") {
    emits("getData", {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...unref(formData),
    });
    props.getData?.({
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...unref(formData),
    });
  } else {
    emits("getData", unref(formData));
    props.getData?.(unref(formData));
  }
}

onMounted(() => {
  // 获取当前屏幕高度
  onGetTableData();
});
</script>

<style scoped></style>
