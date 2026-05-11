import {defineStore} from "pinia";
import {System} from "@/typings/system";
import {SecureStorage} from "@/store/plugins";
import {darkTheme, GlobalThemeOverrides, useOsTheme} from 'naive-ui';
import {themeSetting} from "@/setting/theme";
import {getColorPalette, getRgbOfColor} from "@/utils";

// 系统主题
export const systemThemeRef = useOsTheme()
export const useThemeStore = defineStore({
    id: 'theme-store',
    state: (): System.GlobalTheme => initTheme(),
    getters: {
        systemTheme(state) {
            if (state.mode === 'dark') {
                this.setThemeMode("dark")
                return darkTheme
            } else if (state.mode === 'light') {
                this.setThemeMode("light")
                return undefined
            } else if(state.mode === 'auto') {
                if (systemThemeRef.value === "dark") {
                    // this.setThemeMode("dark")
                    return darkTheme
                }
            }
            return undefined
        },
        /**
         * naive-ui theme override
         *
         * @type import('naive-ui').GlobalThemeOverrides
         */
        getNaiveThemeOverrides(state) {
            const themeOverride: GlobalThemeOverrides = {
                common: {
                    primaryColor: state.color.primary,
                    primaryColorHover: getColorPalette(state.color.primary, 5),
                    primaryColorPressed: getColorPalette(state.color.primary, 7),
                    primaryColorSuppl: state.color.primary,
                    borderRadius: `${state.naive.borderRadius}px`,
                    successColor: state.color.success,
                    successColorHover: getColorPalette(state.color.success, 5),
                    successColorPressed: getColorPalette(state.color.success, 7),
                    successColorSuppl: state.color.success,
                    warningColor: state.color.warning,
                    warningColorHover: getColorPalette(state.color.warning, 5),
                    warningColorPressed: getColorPalette(state.color.warning, 7),
                    warningColorSuppl: state.color.warning,
                    errorColor: state.color.error,
                    errorColorHover: getColorPalette(state.color.error, 5),
                    errorColorPressed: getColorPalette(state.color.error, 7),
                    errorColorSuppl: state.color.error,
                    infoColor: state.color.info,
                    infoColorHover: getColorPalette(state.color.info, 5),
                    infoColorPressed: getColorPalette(state.color.info, 7),
                    infoColorSuppl: state.color.info,
                },
                LoadingBar: {
                    colorLoading: state.color.primary
                }
            }
            return themeOverride
        }
    },
    actions: {
        /** 重置theme状态 */
        resetThemeStore() {
            this.$reset()
        },
        /** 设置主题模式 */
        setThemeMode(mode: System.GlobalTheme['mode']) {
            this.mode = mode;
        },
        /** 设置主题色 */
        setPrimaryColor(color: string) {
            this.color.primary = color;
        },
        /** 设置成功色 */
        setSuccessColor(color: string) {
            this.color.success = color;
        },
        /** 设置警告色 */
        setWarningColor(color: string) {
            this.color.warning = color;
        },
        /** 设置错误色 */
        setErrorColor(color: string) {
            this.color.error = color;
        },
        /** 设置边框圆角 */
        setThemeRounded(round: number) {
            this.round = round;
        },
        /** 设置过渡动画类型 */
        setThemeAnimation(animation: System.GlobalTheme['animate']) {
            this.animation = animation;
        },
        /** 设置是否折叠菜单 */
        setSiderCollapsed(collapsed: boolean) {
            this.sider.collapsed = collapsed;
        },
        /** 设置导航模式 */
        setLayoutMode(model: string) {
            this.layout.mode = model;
        },

    },
    persist: {
        storage: SecureStorage
    }
})

function initTheme() {
    return themeSetting
}