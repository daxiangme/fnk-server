<template>
  <!--  icon-park-solid:dark-mode-->
  <base-hover-tip text="主题配置">
    <div class="flex-center w-48px text-24px cursor-pointer " @click="drawerShow= true">
      <icon-icon-park-solid:dark-mode/>
    </div>
  </base-hover-tip>
  <n-drawer v-model:show="drawerShow" :width="props.width" :placement="props.placement">
    <n-drawer-content title="主题设置">
      <n-space vertical>
        <n-divider><p>主题模式</p></n-divider>
        <div class="flex-col items-center">
          <n-tabs type="segment" animated v-model:value="theme.mode">
            <n-tab-pane name="light">
              <template #tab>
                <icon-line-md:sunny-filled-loop class="text-#FFCC00 text-16px"/>
                <p class="ml-6px">浅色</p>
              </template>
            </n-tab-pane>
            <n-tab-pane name="auto">
              <template #tab>
                <icon-line-md:light-dark-loop class="text-16px"/>
                <p class="ml-6px">自动</p>
              </template>
            </n-tab-pane>
            <n-tab-pane name="dark">
              <template #tab>
                <icon-line-md:moon-filled-loop class="text-16px"/>
                <p class="ml-6px">深色</p>
              </template>
            </n-tab-pane>
          </n-tabs>
        </div>

        <n-divider><p>布局模式</p></n-divider>
        <div class="flex text-primary">
          <n-grid x-gap="24" y-gap="24" :cols="2">
            <n-gi>
              <base-hover-tip text="基本布局" :hover-style="false">
                <icon-local-layout-base
                  class="p-4px border-2px  hover:border-primary rounded-6px text-100px"
                  :class="[theme.layout.mode === 'base'?'border-primary':'border-transparent']"
                  @click="theme.setLayoutMode('base')"/>
              </base-hover-tip>
            </n-gi>
            <n-gi>
              <base-hover-tip text="分离式卡片布局" :hover-style="false">
                <icon-local-layout-card
                  class="p-4px border-2px  hover:border-primary rounded-6px text-100px"
                  :class="[theme.layout.mode === 'card'?'border-primary':'border-transparent']"
                  @click="theme.setLayoutMode('card')"/>
              </base-hover-tip>
            </n-gi>
            <n-gi span="2" v-if="theme.layout.mode==='card'">
              <div class="flex items-center  justify-between">
                <p class="text-16px  mr-12px">分离式卡片圆角(px)</p>
                <n-input-number class="w-120px text-center" v-model:value="theme.layout.round" :step="2" :min="0"/>
              </div>
            </n-gi>
          </n-grid>
        </div>
        <n-divider><p>菜单配置</p></n-divider>
        <div class="flex items-center justify-between">
          <p class="text-16px">菜单布局</p>
          <n-select class="w-120px text-center" v-model:value="theme.menu.layout" :options="menuLayoutOptions"
                    placeholder="选择布局"></n-select>
        </div>
        <template v-if="theme.menu.layout === 'base'">
          <div class="flex items-center justify-between mt-12px">
            <p class="text-16px ">显示面包屑</p>
            <n-switch v-model:value="theme.menu.showBreadcrumb">
            </n-switch>
          </div>
          <div class="flex items-center justify-between mt-12px">
            <p class="text-16px ">显示面包屑图标</p>
            <n-switch v-model:value="theme.menu.showBreadcrumbIcon">
            </n-switch>
          </div>
        </template>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px">头部高度</p>
          <n-input-number class="w-120px text-center" v-model:value="theme.menu.headerHeight" :min="44"/>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px ">显示标签栏</p>
          <n-switch v-model:value="theme.menu.showTabs">
          </n-switch>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px">标签栏高度</p>
          <n-input-number class="w-120px text-center" v-model:value="theme.menu.tabsHeight" :min="32"/>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px ">显示折叠</p>
          <n-switch v-model:value="theme.menu.showCollapse">
          </n-switch>
        </div>
        <n-divider><p>颜色色配置</p></n-divider>
        <div class="flex items-center justify-between">
          <p class="text-16px ">主题色</p>
          <n-color-picker  class="w-120px" v-model:value="theme.color.primary" :show-alpha="false" :swatches="theme.color.swatches" />
        </div>
        <div class="flex items-center justify-between">
          <p class="text-16px ">信息色</p>
          <n-color-picker  class="w-120px" v-model:value="theme.color.info" :show-alpha="false" :swatches="theme.color.swatches" />
        </div>
        <div class="flex items-center justify-between">
          <p class="text-16px ">成功色</p>
          <n-color-picker  class="w-120px" v-model:value="theme.color.success" :show-alpha="false" :swatches="theme.color.swatches" />
        </div>
        <div class="flex items-center justify-between">
          <p class="text-16px ">警告色</p>
          <n-color-picker  class="w-120px" v-model:value="theme.color.warning" :show-alpha="false" :swatches="theme.color.swatches" />
        </div>
        <div class="flex items-center justify-between">
          <p class="text-16px ">错误色</p>
          <n-color-picker  class="w-120px" v-model:value="theme.color.error" :show-alpha="false"  :swatches="theme.color.swatches" />
        </div>
        <n-divider><p>侧边栏配置</p></n-divider>
        <div class="flex items-center justify-between">
          <p class="text-16px ">侧边栏深色</p>
          <n-switch v-model:value="theme.sider.inverted">
          </n-switch>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px ">侧边栏折叠</p>
          <n-switch v-model:value="theme.sider.collapsed">
          </n-switch>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px">折叠宽度</p>
          <n-input-number class="w-120px text-center" v-model:value="theme.sider.collapsedWidth" :min="64"/>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px ">展开宽度</p>
          <n-input-number class="w-120px text-center" v-model:value="theme.sider.width" :min="200"/>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px ">显示折叠操作条</p>
          <n-switch v-model:value="theme.sider.showCollapse">
          </n-switch>
        </div>
        <n-divider><p>动画配置</p></n-divider>
        <div class="flex items-center justify-between">
          <p class="text-16px ">过渡动画</p>
          <n-switch v-model:value="theme.animate.enable"/>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px ">动画效果</p>
          <n-select class="w-120px text-center" v-model:value="theme.animate.type" :options="animationOptions"
                    placeholder="选择效果"></n-select>
        </div>

        <n-divider><p>底部配置</p></n-divider>
        <div class="flex items-center justify-between">
          <p class="text-16px ">显示底部</p>
          <n-switch v-model:value="theme.footer.show"/>
        </div>
        <div class="flex items-center justify-between mt-12px">
          <p class="text-16px ">底部高度</p>
          <n-input-number class="w-120px text-center" v-model:value="theme.footer.height" :min="44"/>
        </div>
        <n-divider><p>naive-ui配置</p></n-divider>
        <div class="flex items-center justify-between">
          <p class="text-16px ">全局圆角</p>
          <n-input-number class="w-120px text-center" v-model:value="theme.naive.borderRadius" :min="0"/>
        </div>
      </n-space>
      <template #footer>
        <n-button type="warning" @click="theme.resetThemeStore()">重置主题样式</n-button>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>
<script setup lang="ts">
import {computed, ref} from "vue";
import {useThemeStore} from "@/store";
import {isWhiteColor} from "@/utils";

const props = defineProps({
  width: {
    type: String,
    default: '360px'
  },
  placement: {
    type: String,
    default: 'right'
  },
  show: {
    type: Boolean,
    default: false
  }
})
const emits = defineEmits(['update:show'])
const drawerShow = ref(false)
const theme = useThemeStore()
/**
 * 过渡动画类型
 * 过渡动画类型
 * fade-slide 滑动
 * fade 淡入淡出
 * fade-bottom 底部消退
 * fade-scale 缩放消退
 * zoom-fade 渐变
 * zoom-out 闪现
 * none 无动画
 */

const animationOptions = [
  {label: '滑动', value: 'fade-slide'},
  {label: '淡入淡出', value: 'fade'},
  {label: '底部消退', value: 'fade-bottom'},
  {label: '缩放消退', value: 'fade-scale'},
  {label: '渐变', value: 'zoom-fade'},
  {label: '闪现', value: 'zoom-out'},
  {label: '无动画', value: 'none'}
]

const menuLayoutOptions = [
  {label: '基本布局', value: 'base'},
  {label: '应用布局', value: 'app'}
]

</script>

<style scoped>

</style>