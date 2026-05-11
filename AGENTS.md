# AGENTS.md

本文件用于指导 Codex / 自动化代理在当前仓库内工作。优先遵循本文件，其次遵循用户在当前会话中的明确要求。

## 项目概况

- 当前项目是前后端一体的后台管理系统脚手架。
- 后端：Java 17、Spring Boot 3.2.0、Maven 多模块、MyBatis-Plus、Sa-Token、Hutool、Knife4j / springdoc。
- 前端：Vue 3、TypeScript、Vite、Naive UI、UnoCSS、Pinia、Vue Router、Axios，依赖管理使用 pnpm。
- 服务端根包名以 `com.fnk` 为准，不要继续新增 `fun.isite.service` 旧包名代码。

## 目录地图

- `pom.xml`：后端聚合父工程，包含 `app-server`、`service-starter`、`service-common`、`app-system`、`service-code-generate`。
- `app-server/app-server-admin`：后台管理应用启动入口，启动类为 `com.fnk.app.admin.AdminApplication`，不放 Controller / Service / Mapper。
- `service-starter`：服务端基础设施 starter，包含 web、security、data、doc、core。
- `app-system/app-system-api`：系统模块契约层，包含 facade、AO、Query、VO、错误码、异常、常量。
- `app-system/app-system-biz`：系统模块业务实现，包含 Controller、FacadeImpl、Service、DO、Mapper、XML、Convert、Cache。
- `service-common/service-common-bean`：公共常量、异常、HTTP 响应对象等轻量基础类型。
- `service-common/service-common-db`：数据库基础能力，包含 BaseEntity、BaseService、分页对象等。
- `service-common/service-common-tools`：公共工具类。
- `service-code-generate`：代码生成器模块与模板配置。
- `frontend`：前端管理端工程。
- `service.sql`：数据库初始化脚本。涉及表结构、初始化数据、权限菜单时需要同步检查。
- 服务端后续模块拆分、包名、`app-server`、`*-api` / `*-biz`、AO/Query/VO 命名等规则，遵循 `doc/server-module-spec.md`。

## 工作流要求

- 开始修改前先查看 `git status --short --branch`，识别已有用户改动。
- 不要回滚、覆盖或格式化与当前任务无关的文件；遇到已存在改动时，在其基础上继续工作。
- 搜索文件和文本优先使用 `rg` / `rg --files`。
- 手工改文件优先使用 `apply_patch`，避免用脚本做不必要的大范围重写。
- 改动应尽量小而完整：业务改动要覆盖后端接口、前端调用、页面状态、SQL/菜单/字典等真实联动面。
- 完成功能后必须做可执行验证；如果环境缺失、依赖无法下载、数据库不可用或测试被项目配置跳过，需要在最终回复中明确说明。

## 后端开发规则

- Controller 放在各业务 `*-biz` 模块，保持轻薄：只做参数接收、权限注解、响应包装和调用 facade。
- API 响应统一使用 `RestResponse`；分页列表优先沿用 `SplitPageDTO`、`PageVO` 和已有 `basicPage` 模式。
- Controller 方法保留 OpenAPI 注解习惯：类上使用 `@Tag`，方法上使用 `@Operation`。
- 权限控制沿用 Sa-Token，新增管理接口时检查是否需要 `@SaCheckPermission`，权限标识要与前端菜单/按钮和数据库初始化数据一致。
- 业务逻辑放在 `*-biz/service` 具体类；跨表写操作使用 `@Transactional(rollbackFor = Exception.class)`。
- 数据访问优先使用 MyBatis-Plus、现有 `BaseService` / mapper 模式和 LambdaQueryWrapper，不要绕过已有基础能力。
- 公共能力按稳定性下沉：基础设施放 `service-starter-*`，跨模块基础类型放 `service-common-bean`，数据库基础能力放 `service-common-db`，工具函数放 `service-common-tools`，业务契约放 `*-api`。
- 校验和业务断言沿用 `AssertUtils`、项目内异常与响应码，不要引入另一套异常返回结构。
- 密码、Token、角色缓存、登录态相关改动要同步检查 Sa-Token、`RoleCache`、用户角色关系和强制下线逻辑。
- 修改实体、字段、菜单、字典或初始化数据时，同步检查 `service.sql` 和代码生成模板是否需要更新。

## 前端开发规则

- 前端代码位于 `frontend`，依赖安装和脚本执行都在该目录下进行。
- 使用 pnpm，不要混用 npm/yarn 生成新的锁文件。
- 页面优先沿用现有结构：`app-container`、`BaseTable`、`BaseForm`、抽屉/弹窗组件、`src/service` API 封装、`src/router/modules` 路由模块、Pinia store。
- Vue 单文件组件优先使用 `<script setup lang="ts">`，保持类型声明和接口字段与后端一致。
- API 调用统一通过 `frontend/src/service` 及其子模块封装，不要在页面里散写 axios 请求。
- 样式优先使用 UnoCSS 工具类、现有主题变量和 Naive UI 能力；不要引入新的 UI 组件库或不必要的全局样式体系。
- 图标沿用 Iconify / 本地 SVG 机制。需要动态换色的 SVG 使用 `currentColor`。
- 新增页面时同步检查路由、菜单权限、按钮权限、接口路径和空/加载/错误状态。
- 页面交互应给出明确反馈，例如成功消息、加载态、空态或错误态，不要让操作静默失败。

## 代码生成与模板

- `service-code-generate` 会影响后续生成代码；改模板前先确认目标是修模板还是修当前业务文件。
- 生成后的代码要按当前项目风格收敛，不要把一次性生成痕迹扩散到手写业务逻辑中。
- 新增生成配置文件时避免提交本地私有数据库连接、账号、密码或绝对路径。

## 常用验证命令

后端：

```bash
./mvnw clean compile
./mvnw -pl app-server/app-server-admin -am package -DskipTests
```

前端：

```bash
cd frontend
pnpm install
pnpm run build
```

按改动范围选择最小充分验证。只改文档时可用 `sed` / `rg` 检查内容；涉及前端 UI 时优先启动 `pnpm run dev` 并在浏览器中验证关键页面；涉及后端运行时行为时优先启动 `AdminApplication` 或执行相关 Maven 模块验证。

## 提交与交付

- 交付前再次查看 `git status --short`，只说明自己改动的文件。
- 如果用户要求提交代码，先确认验证结果，再按用户指定或项目约定的提交信息提交。
- 不要把 `target/`、`frontend/dist/`、日志、IDE 文件、本地环境配置、数据库账号密码等产物加入版本控制。
- 最终回复要包含：改动摘要、验证命令与结果、未完成或受环境限制的事项。
