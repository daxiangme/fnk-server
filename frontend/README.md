# EG Service Admin

EG Service 的管理端工程，基于 [Art Design Pro](https://github.com/Daymychen/art-design-pro) 二次开发，并对接本仓库 Spring Boot 服务端的认证、动态菜单、权限和业务接口。

## 上游项目

- 项目：Art Design Pro
- 仓库：[Daymychen/art-design-pro](https://github.com/Daymychen/art-design-pro)
- 文档：[artd.pro/docs](https://www.artd.pro/docs)
- 许可证：MIT License
- 原始版权：`Copyright (c) 2025 SuperManTT`

原始许可证全文见 [`LICENSE`](LICENSE)，基线和改造范围见 [`UPSTREAM.md`](UPSTREAM.md)。本目录不是 Art Design Pro 官方发行仓库，问题反馈和贡献应根据问题归属提交到本项目或上游项目。

## 版本说明

当前管理端已从原有 Naive UI、UnoCSS 前端迁移到 Art Design Pro、Element Plus 和 Tailwind CSS。该迁移替换了布局、路由、状态管理接入、请求封装和业务页面基础结构，不能作为原前端的兼容性依赖升级使用。

需要维护旧版前端时，请使用仓库的 `feat/naive-ui-maintenance` 分支；迁移前的固定代码基线为 `naive-ui-baseline` 标签。当前目录只维护 Art Design Pro、Element Plus 版本。

## 技术栈

Vue 3、TypeScript、Vite 7、Element Plus、Tailwind CSS、Pinia、Vue Router 和 Axios。

## 本地运行

```bash
cp .env.example .env
cp .env.development.example .env.development
pnpm install
pnpm dev
```

环境要求：Node.js 20.19 或更高版本，pnpm 10.12.4。

## 生产构建

```bash
cp .env.example .env
cp .env.production.example .env.production
pnpm install --frozen-lockfile
pnpm build
```

真实 `.env` 文件已由 Git 忽略。Example 文件只能存放公开默认值和说明，不能填写密码、Token 或内部服务地址。
