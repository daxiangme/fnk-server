import {systemThemeRef, useThemeStore} from "@/store";
import {getColorPalettes, getRgbOfColor} from "@/utils";
import {watch} from "vue";


export const subscribeThemeStore = () => {
  // 缓存处理
  const uTheme = useThemeStore();
  if (uTheme.mode === 'dark') {
    document.documentElement.classList.add('dark');
  } else if (uTheme.mode === 'light') {
    document.documentElement.classList.remove('dark');
  } else {
    if (systemThemeRef.value === "dark") {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }
  addThemeColorToCss(uTheme.color.primary)
  // 监听主题颜色
  uTheme.$subscribe((mutation, state) => {
    // 深色模式处理
    if (state.mode === 'dark') {
      document.documentElement.classList.add('dark');
    } else if (state.mode === 'light') {
      document.documentElement.classList.remove('dark');
    } else {
      if (systemThemeRef.value === "dark") {
        document.documentElement.classList.add('dark');
      } else {
        document.documentElement.classList.remove('dark');
      }
    }
    // 主题色处理
    addThemeColorToCss(state.color.primary);
  })
}

// 监听系统主题
watch(systemThemeRef, (value) => {
  const uTheme = useThemeStore();
  if (value === "dark") {
    if (uTheme.mode === "dark" || uTheme.mode === "auto") {
      document.documentElement.classList.add('dark');
    }
  } else {
    document.documentElement.classList.remove('dark');
  }
})

function addThemeColorToCss(color: string) {
  const styleCss = []
  const {r, g, b} = getRgbOfColor(color);
  styleCss.push(`--primary-color: ${r},${g},${b}`);
  const colorPaletteList = getColorPalettes(color);
  colorPaletteList.forEach((color, index) => {
    const {r: R, g: G, b: B} = getRgbOfColor(color);
    styleCss.push(`--primary-color-${index}: ${R},${G},${B}`);
  })
  document.documentElement.style.cssText += styleCss.join(';');
}
