# 服务端模块规范

本文档用于约束服务端后续重构与新增模块开发。项目定位是开源、快速、中后台脚手架，因此模块设计应优先保证开箱即用、边界清晰、扩展简单、生成友好。

## 基础约定

- 基础包名统一为 `com.fnk`。
- 新增服务端代码不得继续扩展旧包名 `fun.isite.service`，存量代码可按迁移计划逐步调整。
- Controller 放在各业务 `*-biz` 模块中，`app-server` 只负责启动和模块装配。
- 业务模块按 `api + biz` 拆分：`api` 定义契约，`biz` 实现契约和业务。
- Service 使用具体类，不再使用 `IService + ServiceImpl` 这种空壳接口实现结构。
- DAL 层使用 MyBatis-Plus 提供基础 CRUD 能力，只有复杂查询、批量处理或性能优化场景才新增 Mapper 方法或 XML。
- 模块间调用优先依赖目标模块的 `*-api`，禁止直接依赖目标模块的 `*-biz`。

## 推荐目录结构

```text
eg-service
├── app-server
│   └── app-server-admin
│       ├── src/main/java/com/fnk/app/admin/AdminApplication.java
│       └── src/main/resources
│           ├── application.yml
│           ├── application-dev.yml
│           └── application-prod.yml
│
├── service-starter
│   ├── service-starter-web
│   ├── service-starter-security
│   ├── service-starter-data
│   ├── service-starter-doc
│   └── service-starter-core
│
├── service-common
│   ├── service-common-bean
│   ├── service-common-db
│   └── service-common-tools
│
├── app-system
│   ├── app-system-api
│   └── app-system-biz
│
├── service-code-generate
└── service.sql
```

## 模块职责

### app-server

`app-server` 是应用启动层，只负责组合 starter 和业务模块。

- `app-server-admin` 是后台管理端启动模块。
- 启动类、环境配置、应用级装配放在这里。
- 不写具体业务 Controller、Service、Mapper。
- 引入某个业务 `*-biz` 模块后，该模块的接口、业务和资源应自动生效。

推荐依赖：

```text
app-server-admin
  -> service-starter-core
  -> app-system-biz
  -> other-biz
```

### service-starter

`service-starter` 是基础设施自动装配层，不依赖具体业务模块。

- `service-starter-web`：WebMVC、统一异常、统一响应、参数转换、CORS、requestId、日志过滤器。
- `service-starter-security`：Sa-Token、登录态、权限注解、匿名接口、认证拦截基础能力。
- `service-starter-data`：MyBatis-Plus、分页插件、自动填充、逻辑删除、Redis 基础配置。
- `service-starter-doc`：springdoc、Knife4j、OpenAPI 分组与文档开关。
- `service-starter-core`：聚合常用 starter，供 `app-server` 一键引入。

starter 只提供框架能力和默认配置，不放用户、角色、菜单等系统业务。

### service-common

`service-common` 是公共基础层，要求尽量稳定、轻量、低耦合。

- `service-common-bean`：常量、统一响应对象、响应码接口、基础异常、纯 POJO。
- `service-common-db`：`BaseEntity`、分页对象、通用数据库模型、MyBatis-Plus 相关基础类型。
- `service-common-tools`：工具类，例如 JSON、Redis、Spring 上下文、字符串、加密等。

公共模块不应反向依赖业务模块。

### 业务模块

业务模块统一按 `api + biz` 组织。例如系统模块：

```text
app-system
├── app-system-api
└── app-system-biz
```

`app-system-api` 是系统模块对外契约，其他模块只依赖它。  
`app-system-biz` 是系统模块实现，包含 Controller、Facade 实现、业务 Service、DAL、缓存等。

## app-system-api 规范

推荐包结构：

```text
com.fnk.app.system.api
├── facade
│   ├── AdminUserFacade.java
│   ├── RoleFacade.java
│   └── MenuFacade.java
├── model
│   ├── query
│   │   ├── AdminUserPageQuery.java
│   │   └── RolePageQuery.java
│   ├── request
│   │   ├── LoginAO.java
│   │   ├── AdminUserCreateAO.java
│   │   └── AdminUserUpdateAO.java
│   └── response
│       ├── LoginVO.java
│       ├── AdminUserVO.java
│       └── MenuTreeVO.java
├── enums
│   ├── MenuTypeEnum.java
│   └── SystemErrorCode.java
├── exception
│   └── SystemException.java
└── constants
    └── SystemCacheKey.java
```

约束：

- `facade` 定义模块对外能力，供 Controller 或其他业务模块调用。
- `model/query` 放查询参数，类名以 `Query` 结尾。
- `model/request` 放写操作入参，类名以 `AO` 结尾。
- `model/response` 放响应对象，类名以 `VO` 结尾。
- `enums` 放模块枚举和错误码枚举。
- `exception` 放模块业务异常。
- `constants` 放模块常量、缓存 key、权限 key 等稳定契约。
- 不放 Entity、Mapper、MyBatis-Plus 类型、Service、Controller。

## app-system-biz 规范

推荐包结构：

```text
com.fnk.app.system.biz
├── controller
│   ├── AdminUserController.java
│   ├── RoleController.java
│   └── MenuController.java
├── facade
│   ├── AdminUserFacadeImpl.java
│   ├── RoleFacadeImpl.java
│   └── MenuFacadeImpl.java
├── service
│   ├── AdminUserService.java
│   ├── RoleService.java
│   └── MenuService.java
├── dal
│   ├── entity
│   │   ├── AdminUserDO.java
│   │   ├── RoleDO.java
│   │   └── MenuDO.java
│   ├── mapper
│   │   ├── AdminUserMapper.java
│   │   ├── RoleMapper.java
│   │   └── MenuMapper.java
│   └── xml
│       ├── AdminUserMapper.xml
│       ├── RoleMapper.xml
│       └── MenuMapper.xml
├── convert
│   └── SystemConvert.java
└── cache
    └── RoleCache.java
```

约束：

- Controller 放在 `*-biz` 模块，应用启动模块引入 `*-biz` 后接口自动可用。
- Controller 保持轻薄，只做 HTTP 入参、权限注解、响应包装和调用 facade/service。
- Facade 实现 `*-api` 中定义的接口，是跨模块能力的实现边界。
- Service 使用具体类，例如 `AdminUserService`，不再创建 `IAdminUserService` 和 `AdminUserServiceImpl`。
- DAL 实体建议以 `DO` 结尾，避免与 API 层 `VO/AO/Query` 混淆。
- Mapper 继承 MyBatis-Plus `BaseMapper<DO>`。
- XML 只放复杂 SQL，简单 CRUD 使用 MyBatis-Plus。
- `convert` 负责 `DO <-> VO/AO/Query` 转换，避免转换逻辑散落在 Controller 和 Service。
- `cache` 放模块内缓存读写与失效逻辑。

## 调用链规范

HTTP 调用：

```text
Controller -> Facade -> Service -> Mapper
```

模块间调用：

```text
other-biz -> app-system-api.facade -> app-system-biz.facade impl -> Service -> Mapper
```

数据流向：

```text
AO / Query -> Facade -> Service -> DO -> Mapper
Mapper -> DO -> Service -> VO
```

## 命名规范

- 查询对象：`*Query`
  - 示例：`AdminUserPageQuery`、`RoleListQuery`
- 写操作请求对象：`*AO`
  - 示例：`LoginAO`、`AdminUserCreateAO`、`AdminUserUpdateAO`
- 响应对象：`*VO`
  - 示例：`LoginVO`、`AdminUserVO`、`MenuTreeVO`
- 数据库实体：`*DO`
  - 示例：`AdminUserDO`、`RoleDO`、`MenuDO`
- Facade 接口：`*Facade`
  - 示例：`AdminUserFacade`
- Facade 实现：`*FacadeImpl`
  - 示例：`AdminUserFacadeImpl`
- Service 具体类：`*Service`
  - 示例：`AdminUserService`
- Mapper：`*Mapper`
  - 示例：`AdminUserMapper`
- 模块错误码枚举：`*ErrorCode`
  - 示例：`SystemErrorCode`

## 异常与错误码规范

每个业务模块在 `*-api` 中定义自己的错误码枚举。

示例：

```java
public enum SystemErrorCode implements IResponseCode {
    USER_NOT_FOUND(10001, "用户不存在"),
    USER_DISABLED(10002, "用户已禁用"),
    ROLE_NOT_FOUND(10101, "角色不存在"),
    MENU_NOT_FOUND(10201, "菜单不存在");
}
```

建议错误码区间：

```text
system: 10000-19999
tenant:  20000-29999
file:    30000-39999
job:     40000-49999
```

业务异常应携带模块错误码，最终由统一异常处理转换为统一响应。

## 依赖方向

允许：

```text
app-server -> *-biz
app-server -> service-starter-core
*-biz -> *-api
*-biz -> service-common-*
*-biz -> service-starter-* 的必要能力
other-biz -> app-system-api
```

禁止：

```text
*-api -> *-biz
*-api -> MyBatis-Plus / Mapper / Entity
service-starter -> app-system-biz
service-common -> app-system-biz
other-biz -> app-system-biz
app-server 写业务 Controller / Service / Mapper
```

## 迁移建议

存量代码可以分阶段迁移，避免一次性大改。

1. 新增 `app-server/app-server-admin`，迁移当前启动类和应用配置。
2. 新增 `app-system/app-system-api`，迁移 facade、AO、Query、VO、错误码、枚举、异常。
3. 将当前系统业务逐步迁移到 `app-system/app-system-biz`。
4. `app-system-biz` 中保留 Controller，并实现 `app-system-api` 的 facade。
5. 移除空壳 `IService + ServiceImpl` 结构，收敛为具体 Service 类。
6. 将实体、Mapper、XML 收敛到 `app-system-biz/dal`。
7. 抽取 `service-starter-web/data/security/doc/core`。
8. 调整 `app-server-admin` 只依赖 `service-starter-core` 和业务 `*-biz`。
9. 最后清理旧模块和旧包名引用。

## 代码生成器要求

后续代码生成器应按本规范生成模块代码：

- `*-api` 生成 facade、AO、Query、VO、错误码占位。
- `*-biz` 生成 Controller、FacadeImpl、Service、DO、Mapper、XML、Convert。
- Controller 默认生成在 `*-biz`。
- Service 默认生成具体类，不生成接口和 impl。
- DAL 默认使用 MyBatis-Plus 基础 CRUD。
- 包名默认使用 `com.fnk`。
