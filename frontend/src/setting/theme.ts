import {System} from "@/typings/system";

export const themeSetting: System.GlobalTheme = {
    mode: "light",
    layout: {
        mode: "base",
        round: 4
    },
    color: {
        primary: "#6366F1",
        info: "#6366F1",
        success: "#5cb85c",
        warning: "#f0ad4e",
        error: "#d9534f",
        swatches: ['#3b82f6',
            '#6366f1',
            '#8b5cf6',
            '#a855f7',
            '#0ea5e9',
            '#06b6d4',
            '#f43f5e',
            '#ef4444',
            '#ec4899',
            '#d946ef',
            '#f97316',
            '#f59e0b',
            '#eab308',
            '#84cc16',
            '#22c55e',
            '#10b981']
    },
    menu: {
        layout: "base",
        showBreadcrumb: true,
        showBreadcrumbIcon: true,
        headerHeight: 56,
        showTabs: true,
        tabsHeight: 44,
        showCollapse: true
    },
    sider: {
        collapsed: false,
        width: 220,
        collapsedWidth: 64,
        inverted: false,
        showCollapse: true
    },
    animate: {
        enable: true,
        type: "fade-slide"
    },
    footer: {
        show: true,
        height: 44
    },
    naive: {
        borderRadius: 4
    }

};