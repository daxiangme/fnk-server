import {LayoutComponentType, PageRoute} from "@/typings/route";
import {RouteRecordRaw} from "vue-router";


/**
 * 静态pageRoute转换为路由
 * @param pages 配置的路由
 * @param authRouteKey 权限key 由后端返回 如果不传入则全部返回
 */
export function staticPageRouteGenerateRoutes(pages: PageRoute[], authRouteKey: string[] = []) {
    // 排序处理
    pages.sort((a, b) => a.meta?.order - b.meta?.order)
    return pages.map(page => singlePageToRoute(page, authRouteKey)).flat()
}

export function singlePageToRoute(page: PageRoute, authRouteKey: string[]): RouteRecordRaw[] {
    const resultRoute: RouteRecordRaw[] = [];
    // 判断是否有权限
    if (authRouteKey.length == 0 || authRouteKey.includes(page.name)) {
        const layout = page.type === "self" ? page.component : getLayoutComponent(page.type)
        const itemRoute = {...page} as RouteRecordRaw
        itemRoute.component = layout
        if (hasChildren(page)) {
            itemRoute.children = page.children.map(child => singlePageToRoute(child, authRouteKey)).flat()
        }
        itemRoute.component = layout
        if (isSingleRoute(page)) {
            const parentPath = `${page.path}-parent`
            itemRoute.component = page.component
            return [{
                path: parentPath,
                component: layout,
                isSingle: true,
                redirect: page.path,
                meta: page.meta,
                children: [itemRoute]
            } as RouteRecordRaw]
        }
        resultRoute.push(itemRoute)
    }
    return resultRoute
}

/**
 * 根据后端返回的动态菜单生成路由
 * @param menuList 后端返回的动态菜单并且处理过分组的
 */
export function dynamicGenerateRoutes(menuList: IMenus[]): PageRoute[] {
    return menuList.map(menu => singleDynamicMenuToRoute(menu)).flat()
}

function singleDynamicMenuToRoute(menu: IMenus): PageRoute[] {
    const routes: PageRoute[] = []
    // 判断是否为rootId = 0
    const routeItem = {
        name: menu.routeKey,
        path: menu.isIframe ? '/' + menu.routeKey : menu.path,
        meta: {
            title: menu.name,
            icon: menu.icon,
            localIcon: menu.localIcon,
            permission: menu.permission,
            hide: !menu.visible,
            href: menu.isIframe ? menu.path : undefined,
        },
        type: "self",
        component: menu.isIframe ? getLayoutComponent("iframe") : dynamicGetSelfComponent(menu.path),
    } as PageRoute
    if (menu.rootId === "0") {
        if (!menu.children?.length) {
            const item = {
                name: `${menu.routeKey}-parent`,
                path: `${menu.isIframe ? '/' + menu.routeKey : menu.path}-parent`,
                isSingle: true,
                redirect: menu.isIframe ? '/' + menu.routeKey : menu.path,
                type: menu.isIframe ? "iframe" : "basic",
                meta: {
                    title: menu.name,
                    icon: menu.icon,
                    permission: menu.permission,
                    hide: !menu.visible
                },
                component: getLayoutComponent("basic"),
                children: [routeItem]
            } as PageRoute
            return [item]
        } else {
            const children = menu?.children.map(child => singleDynamicMenuToRoute(child)).flat()
            routeItem.component = getLayoutComponent(menu.isIframe ? "iframe" : "basic")
            if (children.length) {
                routeItem.children = children
                routeItem.redirect = children[0].path
            }
        }
    }
    routes.push(routeItem)
    return routes
}

function dynamicGetSelfComponent(path: string) {
    let views = import.meta.glob('@/views/**/*.vue')
    return views[`/src/views/${path}.vue`]
}

export function isSingleRoute(page: PageRoute) {
    return page?.isSingle ?? false
}

export function hasChildren(page: PageRoute) {
    return page?.children && page?.children.length > 0
}

function getLayoutComponent(component: LayoutComponentType) {
    switch (component) {
        case "iframe":
            return () => import('@/layouts/iframe.vue')
        case "basic":
            return () => import('@/layouts/index.vue')
        case "blank":
            return () => import('@/layouts/blank.vue')

    }
}

// 获取缓存的路由
export const getCacheRoutes = (pages: PageRoute[]) => {
    const cacheRoutes: string[] = []
    pages.forEach((page: PageRoute) => {
        if (page.meta?.keepAlive) {
            cacheRoutes.push(page.name)
        }
        if (page?.children?.length) {
            cacheRoutes.push(...getCacheRoutes(page.children))
        }
    })
    return cacheRoutes
}

