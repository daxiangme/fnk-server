import fs from 'node:fs'
import path from 'node:path'

const root = path.resolve(new URL('..', import.meta.url).pathname)

function read(relativePath) {
  return fs.readFileSync(path.join(root, relativePath), 'utf8')
}

function assert(condition, message) {
  if (!condition) {
    throw new Error(message)
  }
}

const requiredFiles = [
  'app-system/app-system-biz/src/main/java/com/fnk/app/system/biz/controller/SystemDictController.java',
  'app-system/app-system-biz/src/main/java/com/fnk/app/system/biz/controller/SystemNoticeController.java',
  'app-system/app-system-biz/src/main/java/com/fnk/app/system/biz/service/SystemUserNoticeService.java',
  'app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/controller/InfraConfigController.java',
  'app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/controller/InfraFileController.java',
  'app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/service/InfraConfigService.java',
  'app-infra/app-infra-biz/src/main/java/com/fnk/app/infra/biz/service/InfraFileService.java',
  'app-system/app-system-biz/src/main/java/com/fnk/app/system/biz/service/SystemDictTypeService.java',
  'app-system/app-system-biz/src/main/java/com/fnk/app/system/biz/service/SystemDictItemService.java',
  'app-system/app-system-biz/src/main/java/com/fnk/app/system/biz/service/SystemNoticeService.java',
  'frontend/src/api/foundation.ts',
  'frontend/src/api/infra.ts',
  'frontend/src/views/system/dict/index.vue',
  'frontend/src/views/system/notice/index.vue',
  'frontend/src/views/system/notice-center/index.vue',
  'frontend/src/views/infra/config/index.vue',
  'frontend/src/views/infra/file/index.vue'
]

for (const file of requiredFiles) {
  assert(fs.existsSync(path.join(root, file)), `${file} must exist.`)
}

const dictController = read(requiredFiles[0])
const noticeController = read(requiredFiles[1])
const configController = read(requiredFiles[3])
const fileController = read(requiredFiles[4])
const serviceSql = read('service.sql')
const foundationApi = read('frontend/src/api/foundation.ts')
const infraApi = read('frontend/src/api/infra.ts')
const dictPage = read('frontend/src/views/system/dict/index.vue')
const noticePage = read('frontend/src/views/system/notice/index.vue')
const noticeCenterPage = read('frontend/src/views/system/notice-center/index.vue')
const configPage = read('frontend/src/views/infra/config/index.vue')
const filePage = read('frontend/src/views/infra/file/index.vue')

for (const permission of [
  'system:dict:view',
  'system:dict:create',
  'system:dict:update',
  'system:dict:delete'
]) {
  assert(dictController.includes(`@SaCheckPermission("${permission}")`), `dict controller missing ${permission}.`)
  assert(serviceSql.includes(`'${permission}'`), `service.sql missing ${permission}.`)
  if (permission !== 'system:dict:view') {
    assert(dictPage.includes(`'${permission}'`), `dict page missing ${permission}.`)
  }
}

for (const permission of [
  'system:notice:view',
  'system:notice:create',
  'system:notice:update',
  'system:notice:delete',
  'system:notice:publish',
  'system:notice:revoke'
]) {
  assert(
    noticeController.includes(`@SaCheckPermission("${permission}")`),
    `notice controller missing ${permission}.`
  )
  assert(serviceSql.includes(`'${permission}'`), `service.sql missing ${permission}.`)
  if (permission !== 'system:notice:view') {
    assert(noticePage.includes(`'${permission}'`), `notice page missing ${permission}.`)
  }
}

for (const token of [
  'system:notice:mine',
  'system_user_notice',
  'pageMy',
  'unreadCount',
  'readAll'
]) {
  assert(serviceSql.includes(token) || noticeController.includes(token) || noticeCenterPage.includes(token), `notice center missing ${token}.`)
}

for (const permission of [
  'infra:config:view',
  'infra:config:create',
  'infra:config:update',
  'infra:config:delete'
]) {
  assert(configController.includes('InfraPermissionConstants.CONFIG_'), `config controller missing permission constants.`)
  assert(serviceSql.includes(`'${permission}'`), `service.sql missing ${permission}.`)
  if (permission !== 'infra:config:view') {
    assert(configPage.includes(`'${permission}'`), `config page missing ${permission}.`)
  }
}

for (const permission of [
  'infra:file:view',
  'infra:file:upload',
  'infra:file:delete'
]) {
  assert(fileController.includes('InfraPermissionConstants.FILE_'), `file controller missing permission constants.`)
  assert(serviceSql.includes(`'${permission}'`), `service.sql missing ${permission}.`)
  assert(filePage.includes(`'${permission}'`), `file page missing ${permission}.`)
}

for (const token of [
  'CREATE TABLE `system_dict_type`',
  'CREATE TABLE `system_dict_item`',
  'CREATE TABLE `system_notice`',
  'CREATE TABLE `system_user_notice`',
  'CREATE TABLE `infra_config`',
  'CREATE TABLE `infra_file`',
  "'系统字典'",
  "'通知公告'",
  "'参数配置'",
  "'文件管理'",
  "'消息中心'"
]) {
  assert(serviceSql.includes(token), `service.sql missing ${token}.`)
}

for (const token of [
  'fetchGetDictTypeList',
  'fetchGetDictItemList',
  'fetchGetDictOptions',
  'fetchGetNoticeList',
  'fetchPublishNotice',
  'fetchRevokeNotice',
  'fetchGetMyNoticeList',
  'fetchReadMyNotice',
  'fetchReadAllMyNotices'
]) {
  assert(foundationApi.includes(token), `foundation API missing ${token}.`)
}

for (const token of [
  'fetchGetInfraConfigList',
  'fetchSaveInfraConfig',
  'fetchGetInfraFileList',
  'fetchUploadInfraFile',
  'fetchDownloadInfraFile'
]) {
  assert(infraApi.includes(token), `infra API missing ${token}.`)
}

assert(serviceSql.includes('infra.file.max-size-mb'), 'service.sql missing file max size config.')

console.log('phase-two M5 foundation service checks passed')
