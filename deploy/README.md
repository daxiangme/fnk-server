# 编译与 Docker 部署

本目录保存公开、可复现的编译和容器部署说明。实际密码、密钥和环境地址只能写入本地配置或部署平台的密钥管理服务。

所有命令均从仓库根目录执行。

## 环境要求

- JDK 17
- Node.js 20.19 或更高版本
- pnpm 10.12.4
- Docker Engine 24 或更高版本，Docker Compose v2

## 本地编译

后端编译：

```bash
./mvnw clean compile
```

后端生产包：

```bash
./mvnw -pl app-server/app-server-admin -am package -DskipTests -Pprod
```

可执行 JAR 输出到：

```text
app-server/app-server-admin/target/app-server-admin-1.0.0.jar
```

`prod/output-dir/` 是 Maven 现有打包配置生成的中间输出目录，其中的薄 JAR 不能脱离依赖目录单独运行；容器和直接运行均使用 `target/` 下由 Spring Boot 重新打包的可执行 JAR。

前端生产构建：

```bash
cd frontend
cp .env.example .env
cp .env.production.example .env.production
pnpm install --frozen-lockfile
pnpm build
```

静态文件输出到 `frontend/dist/`。

## 单独构建镜像

后端：

```bash
docker build -f deploy/Dockerfile.backend -t eg-service-backend:local .
```

前端：

```bash
docker build \
  -f deploy/Dockerfile.frontend \
  --build-arg APP_API_URL=/ \
  --build-arg APP_LOCK_SALT=replace-for-your-environment \
  -t eg-service-frontend:local .
```

前端 Nginx 默认把 `/account`、`/system` 和 `/infra` 请求代理到名为 `backend`、端口为 `12345` 的容器。

## Docker Compose 运行

复制配置并修改所有 `change-*` 示例值：

```bash
cp deploy/.env.example deploy/.env
```

构建并启动 MySQL、Redis、服务端和管理端：

```bash
docker compose \
  --env-file deploy/.env \
  -f deploy/docker-compose.example.yml \
  up -d --build
```

查看状态和日志：

```bash
docker compose --env-file deploy/.env -f deploy/docker-compose.example.yml ps
docker compose --env-file deploy/.env -f deploy/docker-compose.example.yml logs -f backend
```

默认访问地址：

- 管理端：`http://localhost:3006`
- 服务端：`http://localhost:12345`

停止服务：

```bash
docker compose --env-file deploy/.env -f deploy/docker-compose.example.yml down
```

需要同时删除数据库和 Redis 数据卷时显式执行：

```bash
docker compose --env-file deploy/.env -f deploy/docker-compose.example.yml down -v
```

`down -v` 会永久删除本地容器数据，只应在确认不需要保留数据时使用。

## 生产部署注意事项

- 不要直接使用 Example 中的密码和密钥。
- 不要把 `deploy/.env`、`application-*.yml` 或平台密钥提交到 Git。
- 生产环境建议使用外部 MySQL、Redis、镜像仓库和密钥管理服务。
- 应由网关或负载均衡器终止 TLS，并限制数据库、Redis 和服务端管理端口的公网访问。
- 升级前备份数据库和上传文件卷，并使用明确的镜像版本号，避免覆盖唯一可回滚版本。
