/**
 * API 接口类型定义模块
 *
 * 提供所有后端接口的类型定义
 *
 * ## 主要功能
 *
 * - 通用类型（分页参数、响应结构等）
 * - 认证类型（登录、用户信息等）
 * - 系统管理类型（用户、角色等）
 * - 全局命名空间声明
 *
 * ## 使用场景
 *
 * - API 请求参数类型约束
 * - API 响应数据类型定义
 * - 接口文档类型同步
 *
 * ## 注意事项
 *
 * - 在 .vue 文件使用需要在 eslint.config.mjs 中配置 globals: { Api: 'readonly' }
 * - 使用全局命名空间，无需导入即可使用
 *
 * ## 使用方式
 *
 * ```typescript
 * const params: Api.Auth.LoginParams = { userName: 'admin', password: '123456' }
 * const response: Api.Auth.UserInfo = await fetchUserInfo()
 * ```
 *
 * @module types/api/api
 * @author Art Design Pro Team
 */

declare namespace Api {
  /** 通用类型 */
  namespace Common {
    /** 分页参数 */
    interface PaginationParams {
      /** 当前页码 */
      current: number
      /** 每页条数 */
      size: number
      /** 总条数 */
      total: number
    }

    /** 通用搜索参数 */
    type CommonSearchParams = Pick<PaginationParams, 'current' | 'size'>

    /** 分页响应基础结构 */
    interface PaginatedResponse<T = any> {
      records: T[]
      current: number
      size: number
      total: number
    }

    /** 启用状态 */
    type EnableStatus = '1' | '2'
  }

  /** 认证类型 */
  namespace Auth {
    /** 登录参数 */
    interface LoginParams {
      phone?: string
      userName?: string
      password: string
      ip?: string
    }

    /** 登录响应 */
    interface LoginResponse {
      tokenName: string
      tokenValue: string
      token?: string
      refreshToken?: string
      isLogin: boolean
      loginId: string | number
      loginType: string
      tokenTimeout: number
      sessionTimeout: number
      tokenSessionTimeout: number
      tokenActivityTimeout: number
      loginDevice: string
    }

    /** 用户信息 */
    interface UserInfo {
      id: string
      phone: string
      username: string
      userId?: string | number
      userName?: string
      email?: string
      avatar?: string
      sex?: string
      loginIp?: string
      deptId?: string
      status?: boolean
      roleIdList?: string[]
      roles: string[]
      permissions: string[]
      buttons?: string[]
      menus: SystemManage.MenuItem[]
    }
  }

  /** 系统管理类型 */
  namespace Foundation {
    type DictTypeList = Api.Common.PaginatedResponse<DictTypeItem>
    type DictItemList = Api.Common.PaginatedResponse<DictItemItem>
    type NoticeList = Api.Common.PaginatedResponse<NoticeItem>
    type UserNoticeList = Api.Common.PaginatedResponse<UserNoticeItem>

    interface DictTypeItem {
      id: string
      dictCode: string
      dictName: string
      status: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface DictItemItem {
      id: string
      dictCode: string
      label: string
      value: string
      orderSort: number
      status: boolean
      tagType?: string
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface NoticeItem {
      id: string
      title: string
      noticeType: string
      content?: string
      publishStatus: boolean
      publishTime?: string
      createTime?: string
      updateTime?: string
    }

    interface UserNoticeItem {
      id: string
      userId: string
      noticeId: string
      title: string
      noticeType: string
      content?: string
      readStatus: boolean
      readTime?: string
      createTime?: string
    }

    type DictTypeSearchParams = Partial<
      Partial<DictTypeItem> & {
        page: number
        pageSize: number
      }
    >

    type DictItemSearchParams = Partial<
      Partial<DictItemItem> & {
        page: number
        pageSize: number
      }
    >

    type NoticeSearchParams = Partial<
      Partial<NoticeItem> & {
        page: number
        pageSize: number
      }
    >

    type UserNoticeSearchParams = Partial<
      Partial<UserNoticeItem> & {
        page: number
        pageSize: number
      }
    >
  }

  /** 消息中心类型 */
  namespace Messages {
    type SmsChannelList = Api.Common.PaginatedResponse<SmsChannelItem>
    type SmsTemplateList = Api.Common.PaginatedResponse<SmsTemplateItem>
    type SmsLogList = Api.Common.PaginatedResponse<SmsLogItem>
    type MailAccountList = Api.Common.PaginatedResponse<MailAccountItem>
    type MailTemplateList = Api.Common.PaginatedResponse<MailTemplateItem>
    type MailLogList = Api.Common.PaginatedResponse<MailLogItem>
    type NotifyTemplateList = Api.Common.PaginatedResponse<NotifyTemplateItem>
    type NotifyMessageList = Api.Common.PaginatedResponse<NotifyMessageItem>

    interface SmsChannelItem {
      id: string
      channelName: string
      channelCode: string
      accessKey?: string
      accessSecret?: string
      signature?: string
      endpoint?: string
      status: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface SmsTemplateItem {
      id: string
      channelId: string
      templateName: string
      templateCode: string
      providerTemplateCode?: string
      templateType?: string
      content: string
      params: string[]
      status: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface SmsLogItem {
      id: string
      channelId: string
      templateId: string
      templateCode: string
      mobile: string
      content: string
      templateParams?: Record<string, string>
      sendStatus: string
      sendTime?: string
      errorMsg?: string
      createTime?: string
    }

    interface MailAccountItem {
      id: string
      mail: string
      username: string
      password?: string
      host: string
      port: number
      sslEnable: boolean
      starttlsEnable: boolean
      status: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface MailTemplateItem {
      id: string
      accountId: string
      name: string
      code: string
      fromName?: string
      title: string
      content: string
      params: string[]
      status: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface MailLogItem {
      id: string
      accountId: string
      templateId: string
      code: string
      fromMail: string
      toMail: string
      title: string
      content: string
      templateParams?: Record<string, string>
      sendStatus: string
      sendTime?: string
      errorMsg?: string
      createTime?: string
    }

    interface NotifyTemplateItem {
      id: string
      name: string
      code: string
      nickname: string
      templateType?: string
      content: string
      params: string[]
      status: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface NotifyMessageItem {
      id: string
      userId: string
      templateId: string
      templateCode: string
      templateNickname: string
      templateContent: string
      templateType?: string
      templateParams?: Record<string, string>
      readStatus: boolean
      readTime?: string
      createTime?: string
      updateTime?: string
    }

    type SmsChannelSearchParams = Partial<Partial<SmsChannelItem> & { page: number; pageSize: number }>
    type SmsTemplateSearchParams = Partial<Partial<SmsTemplateItem> & { page: number; pageSize: number }>
    type SmsLogSearchParams = Partial<Partial<SmsLogItem> & { page: number; pageSize: number }>
    type MailAccountSearchParams = Partial<Partial<MailAccountItem> & { page: number; pageSize: number }>
    type MailTemplateSearchParams = Partial<Partial<MailTemplateItem> & { page: number; pageSize: number }>
    type MailLogSearchParams = Partial<Partial<MailLogItem> & { page: number; pageSize: number }>
    type NotifyTemplateSearchParams = Partial<Partial<NotifyTemplateItem> & { page: number; pageSize: number }>
    type NotifyMessageSearchParams = Partial<Partial<NotifyMessageItem> & { page: number; pageSize: number }>

    interface SmsSendParams {
      templateCode: string
      mobile: string
      params?: Record<string, string>
    }

    interface MailSendParams {
      templateCode: string
      toMail: string
      params?: Record<string, string>
    }

    interface NotifySendParams {
      templateCode: string
      userIds: string[]
      params?: Record<string, string>
    }
  }

  /** 基础服务类型 */
  namespace Infra {
    type CodeGenTableList = Api.Common.PaginatedResponse<CodeGenTableItem>
    type ConfigList = Api.Common.PaginatedResponse<ConfigItem>
    type FileConfigList = Api.Common.PaginatedResponse<FileConfigItem>
    type FileList = Api.Common.PaginatedResponse<FileItem>

    interface DatabaseTable {
      tableName: string
      tableComment?: string
      engine?: string
      columnCount?: number
      imported: boolean
    }

    interface DatabaseTableSearchParams {
      tableName?: string
      excludeImported?: boolean
    }

    interface CodeGenTableItem {
      id: string
      tableName: string
      tableComment?: string
      businessName: string
      moduleName: string
      className: string
      packageName?: string
      apiBasePath?: string
      frontendPath?: string
      routePath?: string
      permissionPrefix?: string
      menuParentId?: string
      generateType: string
      author?: string
      syncTime?: string
      createTime?: string
      updateTime?: string
    }

    interface CodeGenFieldItem {
      id: string
      tableId?: string
      columnName?: string
      propertyName?: string
      columnComment?: string
      dbType?: string
      javaType?: string
      tsType?: string
      primaryKey?: boolean
      required?: boolean
      listVisible?: boolean
      searchVisible?: boolean
      formVisible?: boolean
      detailVisible?: boolean
      formType?: string
      queryType?: string
      dictCode?: string
      defaultValue?: string
      orderSort?: number
      width?: number
      readonlyOnCreate?: boolean
      readonlyOnEdit?: boolean
    }

    interface CodeGenRelationItem {
      id?: string
      tableId?: string
      relationName: string
      relationType: string
      sourceTable: string
      sourceColumn?: string
      targetTable?: string
      targetColumn?: string
      joinTable?: string
      joinSourceColumn?: string
      joinTargetColumn?: string
      displayColumn?: string
      generateQuery?: boolean
      generateForm?: boolean
      generateDetail?: boolean
      deleteStrategy?: string
      confidence?: number
      sourceType?: string
      remark?: string
    }

    interface CodeGenFileItem {
      fileType: string
      filePath: string
      content: string
    }

    interface CodeGenPreview {
      table: CodeGenTableItem
      fields: CodeGenFieldItem[]
      relations: CodeGenRelationItem[]
      files: CodeGenFileItem[]
    }

    interface ConfigItem {
      id: string
      configName: string
      configKey: string
      configValue?: string
      groupCode: string
      valueType: string
      visible: boolean
      status: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    interface FileItem {
      id: string
      originalName: string
      fileName: string
      contentType: string
      fileSize: number
      storageType: string
      configId?: string
      configName?: string
      storagePath?: string
      url: string
      createTime?: string
    }

    interface FileConfigItem {
      id: string
      name: string
      storageType: 'local' | 's3' | string
      master: boolean
      basePath?: string
      domain?: string
      maxSizeMb?: number
      endpoint?: string
      bucket?: string
      accessKey?: string
      accessSecret?: string
      enablePathStyleAccess?: boolean
      remark?: string
      createTime?: string
      updateTime?: string
    }

    type CodeGenTableSearchParams = Partial<
      Partial<CodeGenTableItem> & {
        page: number
        pageSize: number
      }
    >

    interface CodeGenTableImportParams {
      tableNames: string[]
    }

    type CodeGenTableUpdateParams = Partial<
      Pick<
        CodeGenTableItem,
        | 'businessName'
        | 'moduleName'
        | 'className'
        | 'packageName'
        | 'apiBasePath'
        | 'frontendPath'
        | 'routePath'
        | 'permissionPrefix'
        | 'menuParentId'
        | 'generateType'
        | 'author'
      >
    >

    type ConfigSearchParams = Partial<
      Partial<ConfigItem> & {
        page: number
        pageSize: number
      }
    >

    type FileSearchParams = Partial<
      Partial<FileItem> & {
        page: number
        pageSize: number
      }
    >

    type FileConfigSearchParams = Partial<
      Partial<FileConfigItem> & {
        page: number
        pageSize: number
      }
    >

  }

  /** 系统管理类型 */
  namespace SystemManage {
    type MenuType = 'TABLE' | 'MENU' | 'BUTTON' | { value?: string; desc?: string }

    interface MenuItem {
      id: string
      rootId: string
      name: string
      routeKey: string
      orderSort: number
      isIframe: boolean
      path: string
      icon?: string
      localIcon?: string
      visible: boolean
      permission?: string
      type: MenuType
      remark?: string
      children?: MenuItem[]
    }

    /** 用户列表 */
    type UserList = Api.Common.PaginatedResponse<UserListItem>

    /** 用户列表项 */
    interface UserListItem {
      id: string
      phone: string
      username: string
      userName?: string
      userGender?: string
      nickName?: string
      userPhone?: string
      userEmail?: string
      userRoles?: string[]
      avatar?: string
      sex?: string
      loginIp?: string
      deptId?: string
      status: boolean | string
      department?: string
      score?: number
      createBy?: string
      createTime?: string
      updateBy?: string
      updateTime?: string
      roleIdList?: string[]
      roles?: string[]
    }

    /** 用户搜索参数 */
    type UserSearchParams = Partial<
      Partial<UserListItem> & {
        page: number
        pageSize: number
        current?: number
        size?: number
      }
    >

    /** 角色列表 */
    type RoleList = Api.Common.PaginatedResponse<RoleListItem>

    /** 角色列表项 */
    interface RoleListItem {
      id?: string
      roleId?: string | number
      roleName: string
      roleKey?: string
      roleCode?: string
      description?: string
      orderSort?: number
      roleScope?: string[]
      status?: boolean
      enabled?: boolean
      createTime?: string
    }

    /** 角色搜索参数 */
    type RoleSearchParams = Partial<
      Partial<RoleListItem> & {
        page: number
        pageSize: number
        current?: number
        size?: number
        startTime?: string | null
        endTime?: string | null
      }
    >
  }
}
