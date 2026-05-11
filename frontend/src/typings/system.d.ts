// Desc: 系统相关的类型定义
import {IRouteMeta, LayoutComponentType} from "@/typings/route";
import {RouteComponent} from "vue-router";


declare namespace System {

    // 菜单项
    type GlobalMenu = import('naive-ui').MenuOption & {
        key: string;
        label: string;
        routeName: string;
        routePath: string;
        meta: IRouteMeta;
        component?: RouteComponent;
        icon?: () => import('vue').VNodeChild;
        children?: GlobalMenu[];
    }
    type GlobalBreadcrumb = {
        label: string;
        name: string;
        icon?: () => import('vue').VNodeChild;
        meta?: IRouteMeta;
        children?: GlobalBreadcrumb[]
        // 下拉中disabled
        disabled?: boolean;
    }
    // 下拉菜单
    type GlobalDropdown = {
        label: string;
        key: string;
        icon?: () => import('vue').VNodeChild;
        disabled?: boolean;
        render?: () => import('vue').VNodeChild;
        [x: string]: any;
    }

    type GlobalTheme = {
        // 主题模式
        mode: 'light' | 'dark' | 'auto';
        // 布局模式 基本布局 | 分离式卡片布局
        layout:{
            mode: 'base' | 'card';
            //  分离式卡片圆角
            round: number;
        }
        // 颜色色配置
        color: {
            primary: string;
            info: string;
            success: string;
            warning: string;
            error: string;
            swatches: string[];
        }
        // 菜单配置
        menu: {
            // 菜单布局样式 base：基本布局 app：应用布局
            layout:'base'|'app'
            // base 显示面包屑
            showBreadcrumb: boolean;
            // base 显示面包屑图标
            showBreadcrumbIcon: boolean;
            // 头部高度 44
            headerHeight: number;
            // 显示标签栏
            showTabs: boolean;
            // 标签栏高度 44
            tabsHeight: number;
            // 显示折叠
            showCollapse: boolean;
        }
        // 侧边栏配置
        sider: {
            // 是否开启折叠
            collapsed: boolean;
            // 侧边栏宽度 220
            width: number;
            // 侧边栏折叠宽度 64
            collapsedWidth: number;
            // 是否深色
            inverted: boolean;
            // 是否显示折叠
            showCollapse: boolean;
        }
        // 动画配置
        animate: {
            // 是否开启动画
            enable: boolean;
            // 动画类型
            /**
             * 过渡动画类型
             * fade-slide 滑动
             * fade 淡入淡出
             * fade-bottom 底部消退
             * fade-scale 缩放消退
             * zoom-fade 渐变
             * zoom-out 闪现
             * none 无动画
             */
            type: 'fade-slide' | 'fade' | 'fade-bottom' | 'fade-scale' | 'zoom-fade' | 'zoom-out' | 'none';
        }
        // 底部配置
        footer: {
            // 底部高度 44
            height: number;
            // 是否显示底部
            show: boolean;
        }
        // naive-ui配置
        naive: {
            borderRadius: number;
            // 其他配置
        }
    }
}