# snail-cloud

`snail-cloud` 是一个基于 Spring Boot 2.7、Spring Cloud 2021、Spring Cloud Alibaba 2021、Dubbo 3 和 Sa-Token 的微服务后端项目。

当前仓库重点围绕系统域能力建设，已完成用户、角色、菜单、部门、岗位、字典、在线用户等核心模块，并支持通过 `sys-api` 向后续业务服务提供统一契约。

## 项目特点

- 基于 `Spring Cloud Gateway + Nacos + Dubbo + Sa-Token`
- 采用“业务域聚合”结构，而不是把所有模块平铺在根目录
- `common` 与业务 `api` 解耦，支持公共层独立构建
- 系统日志通过 `Dubbo RemoteLogService` 异步落库
- 已适配 Dubbo 场景下的 Sa-Token 内网鉴权上下文初始化

## 技术栈

### 后端

- Spring Boot `2.7.18`
- Spring Cloud `2021.0.8`
- Spring Cloud Alibaba `2021.0.5.0`
- Spring Cloud Gateway
- Nacos
- Apache Dubbo `3.1.3`
- Sa-Token `1.42.0`
- MyBatis / MyBatis-Plus
- Dynamic Datasource
- Redis / Redisson / Lock4j
- Undertow

### 相关仓库

- 后端仓库：`/Users/ansir/work/project/gitee/snail-cloud`
- 前端仓库：`/Users/ansir/work/project/gitee/snail-vue`
- 本地参考项目：`/Users/ansir/work/project/gitee/RuoYi-Cloud-Plus`

## 当前目录结构

```text
snail-cloud
├─ config
│  ├─ nacos
│  └─ sql
├─ docs
│  └─ PROJECT_KNOWLEDGE.md
├─ snail-common
│  ├─ snail-common-bom
│  ├─ snail-common-core
│  ├─ snail-common-dubbo
│  ├─ snail-common-log
│  ├─ snail-common-mybatis
│  ├─ snail-common-redis
│  ├─ snail-common-satoken
│  ├─ snail-common-security
│  └─ snail-common-web
├─ snail-gateway
└─ snail-sys-provider
   ├─ snail-sys-api
   └─ snail-sys
```

## 模块说明

### 公共层

- `snail-common-bom`
  - 公共依赖版本管理
- `snail-common-core`
  - 基础工具、公共实体、通用配置
- `snail-common-dubbo`
  - Dubbo 公共配置、Sa-Token Dubbo Filter
- `snail-common-log`
  - `@Log`、日志切面、远程日志接口与日志实体
- `snail-common-mybatis`
  - MyBatis、动态数据源、数据库驱动依赖
- `snail-common-redis`
  - Redis、Redisson、分布式锁
- `snail-common-satoken`
  - Sa-Token 核心整合
- `snail-common-security`
  - 权限上下文与安全辅助能力
- `snail-common-web`
  - Web、Undertow、Actuator 等通用 Web 能力

### 网关层

- `snail-gateway`
  - 统一网关入口
  - 路由转发、统一鉴权、基础过滤能力

### 系统域

- `snail-sys-provider/snail-sys-api`
  - 系统域对外契约
  - 供未来 `flow` 等业务服务直接依赖
- `snail-sys-provider/snail-sys`
  - 系统域实现
  - 包含认证、用户、角色、菜单、部门、岗位、字典、日志等能力

## 后续扩展建议

当前项目不再引入额外一层 `snail-modules`。  
后续新增业务域时，推荐直接在根目录保持同样模式：

```text
snail-flow-provider
├─ snail-flow-api
└─ snail-flow
```

这样能保证：

- 每个业务域自己的 `api` 和 `provider` 就近放置
- 根目录不会因为模块增加而持续平铺膨胀
- `common` 始终只负责基础设施，不反向依赖业务模块

## 环境要求

- JDK `8+`
- Maven `3.9+`
- MySQL `8+`
- Redis `6+`
- Nacos `2.x`

推荐本地环境：

- JDK `1.8`
- Maven `3.9.14`

## 配置位置

### Nacos 配置

全局配置文件位于：

- `config/nacos/snail-global.yml`

这里维护了项目的核心公共配置，例如：

- Dubbo 配置
- Nacos 配置
- 异步线程池默认参数

### SQL 初始化脚本

- `config/sql/snail_sys.sql`

## 构建方式

### 全量校验

```bash
mvn -DskipTests validate
```

### 编译系统服务

```bash
mvn -pl snail-sys-provider/snail-sys -am -DskipTests compile
```

### 打包系统服务

```bash
mvn -pl snail-sys-provider/snail-sys -am -DskipTests package
```

### 单独构建公共层

```bash
mvn -f snail-common/pom.xml -DskipTests package
```

说明：

- `snail-sys-provider` 使用的是嵌套模块结构，因此从根目录选择模块时要写完整路径 `snail-sys-provider/snail-sys`
- 如果你要脱离根工程单独构建某个 provider，需要先确保本地仓库里已经有 `snail-common-bom` 和公共模块产物

## 启动顺序建议

本地联调建议按下面顺序启动：

1. Nacos
2. MySQL
3. Redis
4. `snail-sys`
5. `snail-gateway`
6. 前端 `snail-vue`

## 关键能力说明

### Dubbo 与 Sa-Token

项目在 `snail-common-dubbo` 中补了 Dubbo 过滤器：

- `SaTokenDubboConsumerFilter`
- `SaTokenDubboProviderFilter`

作用：

- 在 Dubbo 调用链中提前初始化 Sa-Token 上下文
- 解决内网服务调用时的 Sa-Token 元数据加载报错
- 支持 token / same-token 在 Dubbo 场景下透传

### 日志链路

系统日志链路为：

```text
@Log -> LogAspect -> AsyncLogService -> RemoteLogService -> sys 表落库
```

日志相关核心文件：

- `snail-common/snail-common-log/src/main/java/com/snail/common/log/aspect/LogAspect.java`
- `snail-common/snail-common-log/src/main/java/com/snail/common/log/service/AsyncLogService.java`
- `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/dubbo/RemoteLogServiceImpl.java`

## 当前系统域已覆盖模块

- 用户管理
- 角色管理
- 菜单管理
- 部门管理
- 岗位管理
- 字典管理
- 在线用户

## 知识文档

项目维护约定和近期重要改动统一记录在：

- `docs/PROJECT_KNOWLEDGE.md`

建议在以下场景同步更新这份文档：

- 新增业务模块
- 调整模块结构
- 修改关键接口路径
- 增加新的全局约定
- 修复构建链路或依赖边界问题

## 常见说明

### 为什么 `common` 不能依赖 `sys-api`

因为后续会新增多个业务域，`common` 必须保持基础设施属性。  
如果 `common -> sys-api`，构建和部署边界会越来越混乱，后续 `flow` 等服务也很难复用。

### 为什么 `snail-common-bom` 不能继承 `snail-common`

因为 `snail-cloud` 根 `pom` 会 `import snail-common-bom`。  
如果 `snail-common-bom` 再挂回父子继承链，会触发 Maven 的 `import cycle`。

## License

MIT
