import fs from 'node:fs'
import path from 'node:path'

const root = process.cwd()

function read(file) {
  return fs.readFileSync(path.join(root, file), 'utf8')
}

function assertFile(file) {
  if (!fs.existsSync(path.join(root, file))) {
    throw new Error(`Missing file: ${file}`)
  }
}

function assertContains(file, needles) {
  const text = read(file)
  for (const needle of needles) {
    if (!text.includes(needle)) {
      throw new Error(`${file} is missing: ${needle}`)
    }
  }
}

function assertNotContains(file, needles) {
  const text = read(file)
  for (const needle of needles) {
    if (text.includes(needle)) {
      throw new Error(`${file} must not contain: ${needle}`)
    }
  }
}

assertFile('scripts/sql/2026-06-19-art-menu-alignment.sql')
assertFile('scripts/sql/2026-06-30-infra-codegen.sql')

const menuLabels = [
  '仪表盘',
  '模板中心',
  '组件中心',
  '功能示例',
  '系统管理',
  '文章管理',
  '结果页面',
  '异常页面',
  '运维管理',
  '用户管理',
  '角色管理',
  '菜单管理',
  '系统字典',
  '通知公告'
]

assertContains('service.sql', menuLabels)
assertContains('service.sql', [
  'infra_codegen_table',
  'infra_codegen_field',
  'infra_codegen_relation',
  "'/infra/codegen'",
  "'infra:codegen:view'",
  "'infra:codegen:import'",
  "'infra:codegen:update'",
  "'infra:codegen:preview'",
  "'infra:codegen:download'",
  "'infra:codegen:delete'"
])
assertContains('scripts/sql/2026-06-30-infra-codegen.sql', [
  '基础服务',
  '代码生成',
  'infra_codegen_table',
  'infra_codegen_field',
  'infra_codegen_relation',
  "'/infra/codegen'",
  "'infra:codegen:view'",
  "'infra:codegen:import'",
  "'infra:codegen:update'",
  "'infra:codegen:preview'",
  "'infra:codegen:download'",
  "'infra:codegen:delete'"
])
assertContains('scripts/sql/2026-06-19-art-menu-alignment.sql', menuLabels)
assertContains('scripts/sql/2026-06-19-art-menu-alignment.sql', [
  "WHERE `id` = '920000000000000000'",
  "'system:user:view'",
  "'system:role:permission'",
  "'system:dict:create'",
  "'system:notice:publish'"
])

const listPages = [
  'frontend/src/views/system/user/index.vue',
  'frontend/src/views/system/role/index.vue',
  'frontend/src/views/system/menu/index.vue',
  'frontend/src/views/system/notice/index.vue'
]

assertFile('frontend/src/components/core/tables/fnk-table/index.vue')
assertContains('frontend/src/components/core/tables/fnk-table/index.vue', [
  "name: 'FnkTable'",
  'ArtTable',
  'computed(() => tableRef.value?.elTableRef)',
  'elTableRef'
])
assertContains('frontend/src/components/core/tables/art-table/index.vue', [
  'actionMode',
  'ArtMenuRight',
  'row-contextmenu',
  "col.type === 'operation'",
  'displayColumns',
  'handleActionMenuSelect',
  'resolveActionButtonType',
  'hasPermission',
  'getNativeTableProps',
  'event.preventDefault()',
  'event.stopPropagation()'
])
assertNotContains('frontend/src/components/core/tables/art-table/index.vue', ['...props,'])
assertContains('frontend/src/components/core/others/art-menu-right/index.vue', [
  'isDangerItem',
  'is-danger',
  "item.type === 'error'",
  'ref="menuRef"',
  'menuRef.value?.contains(target)',
  'submenu-arrow'
])
assertNotContains('frontend/src/components/core/others/art-menu-right/index.vue', [
  "document.querySelector('.context-menu')",
  'class="ubmenu-arrow'
])
assertContains('frontend/src/types/component/index.ts', [
  "'operation'",
  "'error'",
  'TableActionItem',
  'TableActionMode'
])

for (const file of listPages) {
  assertContains(file, ['ArtTableHeader', 'FnkTable', "type: 'operation'", 'actions:', "type: 'error'"])
  assertNotContains(file, ['<ArtTable ', '<ArtTable\n', '</ArtTable>'])
  assertNotContains(file, ['<ElTable v-loading', '<ElPagination'])
}

assertContains('frontend/src/views/system/user/modules/user-search.vue', ['ArtSearchBar'])
assertContains('frontend/src/views/system/role/modules/role-search.vue', ['ArtSearchBar'])

assertContains('frontend/src/views/system/dict/index.vue', [
  'dict-layout',
  'dict-type-panel',
  'dict-item-panel',
  'DictTypeSearch',
  'ArtTableHeader',
  'FnkTable',
  'action-mode="menu"',
  "type: 'operation'",
  "type: 'error'",
  'actions:',
  'selectedDictType',
  'grid-template-columns: minmax(0, 2fr) minmax(0, 3fr)',
  'const showTypeSearch = ref(false)'
])
assertContains('frontend/src/views/system/dict/modules/dict-type-search.vue', ['ArtSearchBar'])
assertNotContains('frontend/src/views/system/dict/index.vue', [
  'v-model:current-page="itemQuery.page"',
  'v-model:page-size="itemQuery.pageSize"',
  ':fit="false"',
  'grid-template-columns: minmax(360px, 420px) minmax(0, 1fr)',
  'const showTypeSearch = ref(true)'
])

assertContains('service-code-generate/src/main/resources/templates/vue-page.ftl', [
  'ArtSearchBar',
  'ArtTableHeader',
  'FnkTable',
  "type: 'operation'",
  "type: 'error'",
  'actions:'
])

assertFile('doc/infra-enterprise-codegen-design.md')
assertFile('doc/superpowers/plans/2026-06-30-enterprise-codegen-implementation.md')
assertContains('doc/infra-enterprise-codegen-design.md', [
  'app-infra',
  '添加数据表',
  '分析表关系',
  'infra_codegen_relation',
  '/infra/codegen',
  'FnkForm'
])
assertContains('doc/superpowers/plans/2026-06-30-enterprise-codegen-implementation.md', [
  'app-infra-api',
  'app-infra-biz',
  'infra_codegen_table',
  'FnkTable',
  'FnkForm'
])

assertFile('app-infra/pom.xml')
assertContains('pom.xml', ['<module>app-infra</module>', 'app-infra-api', 'app-infra-biz'])
assertContains('app-server/app-server-admin/pom.xml', ['app-infra-biz'])
assertContains('app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/controller/CodeGenController.java', [
  '@RequestMapping("/infra/codegen")',
  'InfraPermissionConstants.CODEGEN_VIEW',
  'InfraPermissionConstants.CODEGEN_IMPORT',
  'InfraPermissionConstants.CODEGEN_UPDATE',
  'InfraPermissionConstants.CODEGEN_PREVIEW'
])
assertContains('app-infra/app-infra-api/src/main/java/com/fnk/app/infra/api/constants/InfraPermissionConstants.java', [
  'infra:codegen:view',
  'infra:codegen:import',
  'infra:codegen:update',
  'infra:codegen:preview',
  'infra:codegen:download',
  'infra:codegen:delete'
])
assertContains('app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/service/DatabaseIntrospectService.java', [
  'information_schema.TABLES',
  'information_schema.COLUMNS',
  'information_schema.KEY_COLUMN_USAGE'
])
assertContains('app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/service/TypeMappingService.java', [
  'javaType',
  'tsType',
  'formType',
  'queryType'
])
assertContains('app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/service/CodeGenRelationService.java', [
  'foreign_key',
  'CodeGenRelationTypeEnum.TREE',
  'CodeGenRelationTypeEnum.LOOKUP',
  'CodeGenRelationTypeEnum.MANY_TO_MANY',
  'analyzeAndSave'
])

assertContains('frontend/src/components/core/forms/fnk-form/index.vue', [
  "name: 'FnkForm'",
  'export interface FnkFormItem',
  'defineModel',
  'dict-select',
  'defineExpose'
])
assertContains('frontend/src/api/infra.ts', [
  '/infra/codegen/database/tables',
  '/infra/codegen/tables/import',
  '/fields',
  '/relations/analyze',
  '/preview',
  '/download'
])
assertContains('frontend/src/views/infra/codegen/index.vue', [
  'ArtSearchBar',
  'FnkTable',
  'ElDialog',
  'ElForm',
  'action-mode="menu"',
  '导入数据表',
  '同步表结构',
  '编辑',
  '配置生成',
  "infra:codegen:import",
  "infra:codegen:update"
])

console.log('Art menu/list/dict alignment checks passed.')
