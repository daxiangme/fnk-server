import {PageRoute} from "@/typings/route";
import {System} from "@/typings/system";
import {hasChildren, isSingleRoute, renderIcon} from "@/utils/index";
import { log } from "console";

/**
 * 将动态菜单项进行分组
 * @param menuItems
 * @param parentId
 */
export function groupDynamicMenu(menuItems: any[], parentId: number | string = '0'): IMenus[] {
    const groupedItems: any[] = [];

    const childItems = menuItems.filter(item => item.rootId === parentId);
    childItems.sort((a, b) => a.orderSort - b.orderSort);
    childItems.forEach(childItem => {
        const index = menuItems.indexOf(childItem);
        if (index !== -1) {
            menuItems.splice(index, 1); // 从原始数据中移除已使用的数据
        }
        let items = groupDynamicMenu(menuItems, childItem.id);
        if (items.length > 0) {
            childItem.children = items;
        }
        groupedItems.push(childItem);
    });
    return groupedItems;
}

/**
 *
 * 动态生成菜单
 * @param pages 处理后的路由
 */
export const dynamicGenerateMenus = (pages: PageRoute[]) => {
    const menuOptions: System.GlobalMenu[] = []
    pages.forEach((page: PageRoute) => {
        if (!page.meta?.hide) {
            if (isSingleRoute(page)) {
                // 添加原本的路由-因为原本的被处理了
                menuOptions.push(singlePageToMenu(page.children[0]))
            } else {
                const menu = {
                    label: page.meta?.title,
                    key: page.name,
                    icon: renderIcon(page.meta?.icon, page.meta?.localIcon),
                    routeName: page.name,
                    routePath: page.path,
                    meta: page.meta
                } as System.GlobalMenu
                const children = page?.children?.map(child => singlePageToMenus(child)).flat()
                if (children?.length) {
                    menu.children = children
                }
                menuOptions.push(menu)
            }
        }
    })
    return menuOptions
}

/**
 * 单个
 * @param page
 */
function singlePageToMenus(page: PageRoute): System.GlobalMenu[] {
    const menus: System.GlobalMenu[] = []
    const menu: System.GlobalMenu = singlePageToMenu(page)
    if (!isSingleRoute(page)){
        menu.children = page.children?.map(child => singlePageToMenus(child)).flat()
    }
    menus.push(menu)
    return menus
}


function singlePageToMenu(page: PageRoute): System.GlobalMenu {
    return {
        label: page.meta?.title,
        key: page.name,
        icon: renderIcon(page.meta?.icon, page.meta?.localIcon),
        routeName: page.name,
        routePath: page.path,
        meta: page.meta,
    }
}