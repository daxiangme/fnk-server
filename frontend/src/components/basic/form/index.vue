<template>
  <div ref="formBoxRef">
    <n-form v-bind="props" ref="baseFormRef" :model="data" :rules="rules" :inline="props.inline">
      <n-grid v-bind="getGridProps">
        <n-gi v-bind="item.giProps" v-for="item in props.items" :key="item.field">
          <n-form-item :path="item.field" :label="props.showLabel?item.label:''">
            <!--标签名右侧温馨提示-->
            <template v-if="item.labelMessage" #label>
              <n-space align="center" :justify="props.labelAlign" size="small">
                {{ item.label }}
                <n-tooltip trigger="hover">
                  <template #trigger>
                    <icon-line-md:question-circle class="text-18px text-gray-400 cursor-pointer"/>
                  </template>
                  {{ item.labelMessage }}
                </n-tooltip>
              </n-space>
            </template>
            <!--判断插槽-->
            <template v-if="item.slot">
              <slot
                  :name="item.slot"
                  :model="props.data"
                  :field="item.field"
                  :item="item"
                  :value="props.data[item.field]"
              ></slot>
            </template>
            <template v-else-if="item.filedType === 'number'">
              <n-input-number v-model:value="props.data[item.field]"
                              class="w-full"
                              :placeholder="item.filedOptions?.placeholder??`请输入${item.label}`"
                              v-bind="item.filedOptions"/>
            </template>
            <template v-else-if="item.filedType === 'switch'">
              <n-switch v-model:value="props.data[item.field]"
                        v-bind="item.filedOptions" :checked-value="true" :unchecked-value="false"/>
            </template>
            <template v-else-if="item.filedType === 'select'">
              <n-select v-model:value="props.data[item.field]"
                        :placeholder="item.filedOptions?.placeholder??`请选择${item.label}`"
                        v-bind="item.filedOptions"/>
            </template>
            <template v-else-if="item.filedType === 'upload'"></template>
            <template v-else-if="['string','phone'].includes(item.filedType)">
              <n-input
                  v-model:value="props.data[item.field]"
                  :placeholder="item.filedOptions?.placeholder??`请输入${item.label}`"
                  v-bind="item.filedOptions"
              ></n-input>
            </template>
            <template v-else-if="['date','datetime'].includes(item.filedType)">
              <n-date-picker
                  class="w-full"
                  v-model:formatted-value="props.data[item.field]"
                  :type="item.filedType"
                  :value-format="item?.filedType==='date' ? 'yyyy-MM-dd':'yyyy-MM-dd HH:mm:ss'"
                  :placeholder="item.filedOptions?.placeholder??`请选择${item.label}`"
                  v-bind="item.filedOptions"
              ></n-date-picker>
            </template>

          </n-form-item>
        </n-gi>
        <n-gi v-if="showActionGroup" :span="props.inline?'':'24'" :suffix="props.inline" #="{overflow}">
          <n-space align="center"
                   :justify="props.inline ? 'end' : 'center'"
                   :style="{ 'margin-left': `${!props.inline ? 12 : props.labelWidth}px`,'margin-bottom':`${!overflow ? 24 : 0}px`}">
            <slot name="actionGroup">
              <n-button v-bind="submitBtnOptions" @click="submit">{{ props.submitText }}</n-button>
              <n-button v-bind="resetBtnOptions" @click="reset">{{ props.resetText }}</n-button>
            </slot>
          </n-space>
        </n-gi>
      </n-grid>
    </n-form>
  </div>
</template>
<script setup lang="ts">
import {BaseFormProps, generateRules} from "@/components/basic/form/index";
import {computed, nextTick, onMounted, reactive, ref} from "vue";
import {GridProps, NTooltip} from "naive-ui";

defineOptions({name: 'BaseForm'})
const props = withDefaults(defineProps<BaseFormProps>(), {
  data: () => ({}),
  items: () => ([]),
  size: "medium",
  labelPlacement: "left",
  labelAlign: "left",
  labelWidth: 80,
  showLabel: true,
  inline: false,
  giProps: () => ({}),
  gridProps: () => ({}),
  collapsedRows: 1,
  isSearch: false,
  showActionGroup: true,
  submitText: "提交",
  resetText: "重置",
  submitButtonOptions: () => ({}),
  resetButtonOptions: () => ({}),
})


const rules = computed(() => {
  if (props.isSearch) {
    return {}
  }
  return generateRules(props.items)
})
const gridCollapsed = ref(true);

const getGridProps = computed((): GridProps => {
  return {
    responsive: 'screen',
    xGap: 12,
    // collapsedRows: props.collapsedRows,
    // collapsed: props.inline ? gridCollapsed.value : false,
    cols: props.gridProps && 1,
    ...props.gridProps,
  }
})

const emits = defineEmits<{
  /**
   * 提交表单 返回是否验证通过
   * @param e
   * @param data
   */
  (e: "submit", data: boolean): void;
  (e: "reset"): void;
  (e: "collapse", height: number): void;
  (e: "update:data", data: any): void;
}>()
const baseFormRef = ref();
const formBoxRef = ref();
const formHeight = ref(58)

function unfoldToggle() {
  gridCollapsed.value = !gridCollapsed.value;

  nextTick(() => {
    formHeight.value = formBoxRef.value?.clientHeight
    emits("collapse", formHeight.value)
  })
}

const isShowCollapse = computed(() => {
  return props.inline && props.items.length > (props.collapsedRows * (props.gridProps?.cols as number ?? 1))
})

const actionState = reactive({
  submitLoading: false,
  resetLoading: false,
})
const submitBtnOptions = computed(() => {
  return {
    size: props.size,
    type: 'primary' as any,
    loading: actionState.submitLoading,
    ...props?.submitButtonOptions
  }
})
const resetBtnOptions = computed(() => {
  return {
    size: props.size,
    type: 'default' as any,
    loading: actionState.resetLoading,
    ...props?.resetButtonOptions
  }
})

async function submit() {
  actionState.submitLoading = true;
  let validateRes = false;
  try {
    await baseFormRef.value.validate((res) => {
      validateRes = !res;
    });
  } catch (err) {
    if (err.length > 3) {
      // @ts-ignore
      window.$message?.error('必填项不能为空')
    } else {
      err.forEach((item: any) => {
        // @ts-ignore
        window.$message?.error(item[0].message)
      })
    }
    validateRes = false
  }
  emits("submit", validateRes)
  actionState.submitLoading = false;
  return validateRes;
}

function reset() {
  actionState.resetLoading = true;
  Object.keys(props.data).forEach((key) => {
    props.data[key] = null;
  });
  emits("reset")
  emits("update:data", props.data)
  actionState.resetLoading = false;
}


defineExpose({
  height: formHeight.value + 20
})
onMounted(() => {
  formHeight.value = formBoxRef.value?.clientHeight ?? 58
})

</script>

<style scoped>
</style>