# snail-cloud 项目知识文档

## 1. 项目定位

- 仓库路径：`/Users/ansir/work/project/gitee/snail-cloud`
- 本地参考项目：`/Users/ansir/work/project/gitee/RuoYi-Cloud-Plus`
- 当前联调重点模块：用户、角色、菜单、部门、岗位、字典、在线用户
- 前端联调仓库对应：`/Users/ansir/work/project/gitee/snail-vue`

## 1.1 当前模块结构

- 根聚合模块当前为：
  - `snail-common`
  - `snail-gateway`
  - `snail-sys-provider`
- `snail-sys-provider` 作为系统域聚合模块，内部包含：
  - `snail-sys-provider/snail-sys-api`
  - `snail-sys-provider/snail-sys`
- 当前不再增加最外层 `snail-modules` 目录，直接按业务域建立 `xxx-provider`
- 后续新增业务域时，推荐继续保持同样结构，例如：
  - `snail-flow-provider/snail-flow-api`
  - `snail-flow-provider/snail-flow`
- 目标是让每个业务域的 API 与 Provider 就近放置，根目录避免随着模块增加而持续平铺膨胀

## 2. 当前前后端联调约定

- 前端请求参数直接按后端字段名走
- 用户管理编辑详情使用：`GET /sysUser/info/{userId}`
- 用户管理列表使用：`POST /sysUser/queryByPage`
- 部门列表使用：`POST /dept/list`
- 字典类型分页使用：`POST /dict/type/queryByPage`

## 3. 关键控制器

### 3.1 用户

- 文件：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysUserController.java`
- 关键接口：
  - `POST /sysUser/queryByPage`
  - `GET /sysUser/getUserInfo`
  - `GET /sysUser/info/{userId}`
  - `POST /sysUser/add`
  - `PUT /sysUser/edit`
  - `DELETE /sysUser/remove`

### 3.2 角色

- 文件：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysRoleController.java`

### 3.3 菜单

- 文件：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysMenuController.java`

### 3.4 部门

- 文件：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysDeptController.java`

### 3.5 岗位

- 主数据控制器：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysPostController.java`
- 说明：`SysUserPostController` 只负责用户与岗位关联，不负责岗位管理页面

### 3.6 字典

- 字典类型：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysDictTypeController.java`
- 字典数据：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysDictDataController.java`

### 3.7 在线用户

- 文件：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysUserOnlineController.java`

## 4. 已做过的重要后端修正

### 4.1 用户模块

- 新增了用户详情接口：`/sysUser/info/{userId}`
- 编辑/新增用户时角色岗位空值做过防御处理
- 用户头像支持自动生成默认头像
- 默认头像格式已经从 PNG base64 调整为更短的 `data:image/svg+xml;charset=UTF-8,...`
- 中文名规则：少于 3 个字显示全部，超过 2 个字取后 2 个字
- 英文/数字规则：取 2 到 3 位缩写
- 背景色按用户名稳定取色，不会覆盖用户自己上传的头像地址

### 4.2 角色模块

- 新增角色时不再只保存角色主表，已补齐角色菜单关联保存
- 角色详情支持回显 `menuIds`
- 角色编辑判重已修正为排除自身

### 4.3 部门模块

- 新增部门时补齐 `parentId` 和 `ancestors`
- 禁用部门时对子部门的校验逻辑已修正

### 4.4 字典模块

- 字典类型分页接口改成前端可直接调用的 `POST`
- 字典数据列表支持按 `dictType`、`dictLabel`、`status` 过滤
- 字典数据编辑/删除后会刷新字典缓存

### 4.5 在线用户

- 列表搜索已调整为 `loginLocation`、`userName`
- 查询改为模糊匹配
- 结果按 `loginTime` 倒序

### 4.6 Dubbo 与日志链路

- 参考项目：`RuoYi-Cloud-Plus` 的 `ruoyi-common-dubbo`、`ruoyi-common-log`、`ruoyi-common-satoken`
- 当前项目已新增公共模块：`snail-common/snail-common-dubbo`
- `snail-sys-provider/snail-sys` 已接入 `snail-common-dubbo`，启动类增加了 `@EnableDubbo`
- 全局 Nacos 配置 `config/nacos/snail-global.yml` 已补齐 `dubbo` 配置块，核心包括：
  - `metadataType: remote`
  - `register-mode: instance`
  - `service-discovery.migration: FORCE_APPLICATION`
  - `consumer.check: false`
  - `scan.base-packages: com.snail.**.dubbo`
- 为适配当前项目使用的 `Sa-Token 1.42.0`，没有直接引入 `sa-token-context-dubbo`，而是在 `snail-common-dubbo` 中补了兼容过滤器：
  - `cn.dev33.satoken.context.dubbo.filter.SaTokenDubboConsumerFilter`
  - `cn.dev33.satoken.context.dubbo.filter.SaTokenDubboProviderFilter`
- 这两个过滤器会提前触发 `SaBeanInject` 初始化，解决 Dubbo 内网调用时 Sa-Token 元数据加载报错
- `snail-common-log` 已改成通过 `AsyncLogService + Dubbo RemoteLogService` 远程写日志，不再依赖本地 Spring 事件监听落库
- `snail-common-log`、`snail-common-core`、`snail-common-dubbo` 都已补 `AutoConfiguration.imports`
- 当前日志落库链路：
  - `@Log` -> `LogAspect`
  - `LogAspect` -> `AsyncLogService`
  - `AsyncLogService` -> `RemoteLogService`
  - `snail-sys-provider/snail-sys` 中的 `RemoteLogServiceImpl` 写入 `sys_operate_log` / `sys_login_info`
- 原 `snail-sys-api` 中的 `SysOperateLog`、`SysLoginInfo` 已迁移到公共层，避免 `common -> sys-api` 反向依赖

### 4.7 异步线程池配置

- 公共异步配置文件：`snail-common/snail-common-core/src/main/java/com/snail/common/core/config/AsyncConfig.java`
- 当前不再自定义 `applicationTaskExecutor`，避免与 Spring Boot 默认线程池 bean 冲突
- 项目自定义线程池 bean 名称为：`taskExecutor`
- 异步线程池参数改为读取 `spring.task.execution.*`
- 默认值维护在 Nacos 全局配置：`config/nacos/snail-global.yml`
- 当前默认值：
  - `thread-name-prefix: snail-async-`
  - `core-size: 8`
  - `max-size: 16`
  - `queue-capacity: 200`
  - `keep-alive: 60s`
  - `await-termination: true`
  - `await-termination-period: 60s`
- 后续可直接通过 Nacos 修改这些参数
- 当前实现为“改配置后重启服务生效”，如果要做运行时热刷新，需要额外补线程池动态刷新逻辑

### 4.8 common 与 sys-api 依赖边界

- 当前已清理 `common -> sys-api` 的反向依赖，`snail-common` 可以独立打包
- 调整原则：公共能力产生的模型，放回公共层，不放业务 API 层
- 当前业务域结构保留 `sys-api`，但 `sys-api` 只承载真正对外暴露给其他服务使用的契约
- 设计目标：
  - `common` 不依赖任何业务 `api`
  - `snail-sys-provider/snail-sys-api` 可以被未来的 `snail-flow-provider/snail-flow` 等模块直接依赖
  - `snail-sys-provider/snail-sys` 只负责系统域实现
- 已迁移到公共层的对象：
  - `LoginUser`
  - `RoleDTO`
  - `RemoteLogService`
  - `SysOperateLog`
  - `SysLoginInfo`
- 当前位置：
  - `LoginUser`、`RoleDTO` 在 `snail-common-core`，当前仍保留 `com.snail.sys.api.*` 包名以降低安全上下文相关改动面
  - `RemoteLogService`、`SysOperateLog`、`SysLoginInfo` 在 `snail-common-log`
- 其中 `snail-common-log` 相关包名已进一步收口为模块内语义：
  - `com.snail.common.log.api.RemoteLogService`
  - `com.snail.common.log.domain.SysOperateLog`
  - `com.snail.common.log.domain.SysLoginInfo`
- `snail-common-satoken` 已去掉对 `snail-sys-api` 的依赖
- 验证结果：
  - `mvn -f snail-common/pom.xml -DskipTests package` 通过
  - `mvn -pl snail-sys-provider/snail-sys -am -DskipTests compile` 通过

### 4.9 Provider 聚合结构调整

- 根 `pom.xml` 不再直接平铺 `snail-sys-api`、`snail-sys`
- 当前改为只聚合 `snail-sys-provider`
- `snail-sys-provider/pom.xml` 为系统域聚合 pom，内部统一管理：
  - `snail-sys-api`
  - `snail-sys`
- 这样做的目的：
  - 保留 `sys-api` 作为跨模块远程契约
  - 避免根目录随着服务数量增长持续平铺
  - 后续新增 `snail-flow-provider` 时可完全复用该模式
- 当前构建约定：
  - 根目录构建系统服务：`mvn -pl snail-sys-provider/snail-sys -am -DskipTests package`
  - 单独构建公共层：`mvn -f snail-common/pom.xml -DskipTests package`
- 单独构建 `snail-sys-provider` 时，如果本地仓库未提前安装 `snail-common-bom` 等公共父/依赖，需要先从根工程或公共层完成 install/package
- 这属于正常 Maven 分层结果，不是循环依赖

### 4.10 POM 结构收口

- 2026-04-07 对 `snail-cloud` 全项目 `pom.xml` 做过一轮结构清理，目标是减少重复版本、重复依赖和分散的插件配置
- 根 `pom.xml` 当前统一管理了以下依赖版本：
  - `sa-token-reactor-spring-boot-starter`
  - `sa-token-jwt`
  - `dynamic-datasource-spring-boot-starter`
  - `hutool-captcha`
- 根 `pom.xml` 已统一维护 `spring-boot-maven-plugin` 版本，业务模块不再各自重复写 `${spring-boot.version}`
- `snail-gateway/pom.xml` 已清理掉重复的 `spring-cloud-loadbalancer` 依赖，只保留 `spring-cloud-starter-loadbalancer`
- `snail-common-mybatis`、`snail-common-satoken`、`snail-gateway`、`snail-sys-provider/snail-sys` 中部分原本手写的版本号已回收至根 `dependencyManagement`
- `snail-sys-provider/pom.xml` 已新增对 `snail-sys-api` 的局部 `dependencyManagement`，后续 `snail-sys` 依赖兄弟模块无需重复写版本
- 注意：
  - `snail-common-bom` 作为被根工程 `import` 的 BOM，不能再挂到 `snail-common` 或 `snail-cloud` 父子继承链上
  - 否则 Maven 会报 `type=pom and with scope=import form a cycle`
- 当前验证结果：
  - `mvn -DskipTests validate` 通过
  - `mvn -pl snail-sys-provider/snail-sys -am -DskipTests compile` 通过

### 4.11 应用配置占位符约定

- `snail-gateway` 与 `snail-sys` 的 `application.yml` 不再使用 Maven 资源过滤占位符，例如：
  - `@profiles.active@`
  - `@nacos.server@`
  - `@nacos.username@`
  - `@nacos.password@`
- 原因：
  - IDEA 直接运行应用时，资源文件不会先走 Maven filtering
  - YAML 解析阶段会把 `@...@` 视为非法 token，导致应用在启动前直接失败
- 当前统一改为 Spring 环境变量占位符 + 默认值，例如：
  - `${PROFILES_ACTIVE:dev}`
  - `${NACOS_SERVER:127.0.0.1:8848}`
  - `${NACOS_USERNAME:nacos}`
  - `${NACOS_PASSWORD:nacos}`
- 这样既支持本地 IDE 直接启动，也支持通过环境变量覆盖部署参数

### 4.12 缓存监控接口

- 系统监控新增缓存监控接口：`GET /monitor/cache`
- 控制器文件：
  - `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysCacheController.java`
- 权限标识：
  - `monitor:cache:list`
- 返回结构：
  - `basicInfo`：Redis 基础信息
  - `commandStats`：Redis 命令统计
  - `memoryInfo`：Redis 内存信息
- 实现方式：

### 4.13 2026-04-10 公共组件优化

- 本轮已优先优化以下公共能力：
  - `snail-common-mybatis`
  - `snail-common-log`
  - `snail-common-satoken`
  - `snail-common-redis`
- MyBatis Plus 公共配置新增 `snail.mybatis.*`：
  - `max-limit` 默认 `1000`
  - `overflow` 默认 `true`
  - `worker-id`、`datacenter-id` 可选
- 雪花 ID 生成器不再固定绑定 `localhost`，默认改为 MP 自身策略；若显式配置 `worker-id/datacenter-id`，则优先使用配置值
- `@Log` 切面已修正请求参数分支判断优先级，避免所有 `POST` 请求都错误走对象参数分支
- `AsyncLogService` 已增加本地兜底接口 `LocalLogPersistenceHandler`
  - 远程 Dubbo 日志服务失败时，系统模块会自动退回本地服务落库
  - 系统模块实现类：`SysLocalLogPersistenceHandler`
- Sa-Token Redis 搜索不再先全量收集 key 再分页，改为基于 Redisson key stream 的轻量分页实现
- `RedisUtils` 不再在类加载时静态固化 `RedissonClient` Bean，改为按调用延迟获取，降低启动时序和测试替换风险
- `RedisConfiguration` 已增加互斥校验：

### 4.14 2026-04-14 sys-api 边界再收口

- `snail-sys-provider/snail-sys-api` 不再承载系统域 ORM 实体
- 以下类已从 `snail-sys-api` 迁回 `snail-sys` 内部域模型：
  - `SysUser`
  - `SysRole`
  - `SysDept`
  - `SysUserOnline`
- 迁移后的包位置：
  - `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/domain`
- 当前 `snail-sys-api` 仅保留真正的 API 契约对象：
  - `UserRoleDeptDTO`
- 2026-04-14 第二次收口：
  - `LoginUser`
  - `RoleDTO`
  已从 `snail-common-core` 迁回 `snail-sys-api`
- 现在的职责边界：
  - `snail-sys-api` 放跨模块复用的系统契约 DTO / 登录态对象
  - `snail-common-core` 不再承载 `com.snail.sys.api.*` 这类系统专属契约
  - `snail-common-log`、`snail-common-satoken`、`snail-common-mybatis` 如需 `LoginUser`，统一直接依赖 `snail-sys-api`
- 这次收口后的原则：
  - `snail-sys-api` 只放跨服务调用需要复用的 DTO / VO / 请求响应契约
  - `snail-sys` 自己的数据库实体、MyBatis Plus 注解模型、在线会话缓存模型，全部留在 `snail-sys`
  - 如果某个对象依赖 `@TableName`、`@TableField`、`BaseEntity`、业务内部枚举/常量，默认判定为实现层模型，不应继续留在 `sys-api`
- 收口结果：
  - `snail-sys-api/pom.xml` 已去掉 `snail-common-core`、`mybatis-plus-annotation`
  - 当前 `snail-sys-api` 只需要 `lombok` 以及保留的 OpenAPI/Validation/Hutool 基础依赖
- 验证结果：
  - `mvn -pl snail-sys-provider/snail-sys-api -am -DskipTests compile` 通过
  - `redisson.singleServerConfig`
  - `redisson.clusterServersConfig`
  - 两者不可同时存在
  - 复用 `snail-common-redis` 中的 `RedisUtils.getClient()`
  - 通过 Redisson `RedisNode.info(...)` 获取 `SERVER / CLIENTS / MEMORY / PERSISTENCE / STATS / CPU / COMMANDSTATS`
  - 单机、主从、哨兵、集群模式都支持，优先选择可用 master 节点
- 相关 VO：
  - `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/vo/CacheMonitorVO.java`
  - `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/vo/CacheBasicInfoVO.java`
  - `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/vo/CacheCommandStatVO.java`
  - `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/vo/CacheMemoryInfoVO.java`

## 5. 用户头像默认生成规则

### 5.1 生效场景

- 注册用户
- 后台新增用户
- 后台修改用户
- 查询用户详情/当前用户/用户列表时发现数据库头像为空

### 5.2 生成规则

- 头像格式：`data:image/svg+xml;charset=UTF-8,...`
- 文本来源：`userName`
- 中文：
  - 少于 3 个字时显示全部
  - 超过 2 个字时取最后 2 个字
- 英文/数字：
  - 优先取 2 到 3 位缩写
- 颜色：
  - 背景色按用户名哈希稳定取色
  - 字体颜色按背景亮度自动切换深色/白色

### 5.3 历史数据兜底

- 如果数据库中历史用户头像为空，用户在被查询到时会自动生成并回填数据库

## 6. 当前服务层关键文件

- 用户服务：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/service/impl/SysUserServiceImpl.java`
- 角色服务：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/service/impl/SysRoleServiceImpl.java`
- 部门服务：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/service/impl/SysDeptServiceImpl.java`
- 字典类型服务：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/service/impl/SysDictTypeServiceImpl.java`
- 字典数据服务：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/service/impl/SysDictDataServiceImpl.java`

## 7. 数据库与脚本

- 初始化 SQL：`config/sql/snail_sys.sql`
- 如果联调字段与数据库不一致，优先以实际控制器、实体类和 SQL 三方交叉确认

## 8. 当前环境已知问题

- Maven `settings.xml` 解析问题已修复
- 2026-04-07 已完成一次完整验证：`mvn -pl snail-sys-provider/snail-sys -am -DskipTests package` 通过
- `snail-sys-provider/snail-sys/pom.xml` 中重复的 `spring-cloud-starter-alibaba-nacos-discovery` 依赖声明已清理
- `snail-gateway` 本地启动时，如果 Nacos 客户端仍处于 `STARTING`，可能出现 `Client not connected` 注册失败
- 已在 `snail-gateway/src/main/resources/application.yml` 中将 `spring.cloud.nacos.discovery.fail-fast` 调整为 `false`，避免本地联调时因瞬时注册失败直接导致应用启动中断

## 9. 关键模块与文件

- Dubbo 公共模块：`snail-common/snail-common-dubbo`
- Dubbo 自动配置：`snail-common/snail-common-dubbo/src/main/java/com/snail/common/dubbo/config/DubboConfiguration.java`
- Sa-Token Dubbo 过滤器：
  - `snail-common/snail-common-dubbo/src/main/java/cn/dev33/satoken/context/dubbo/filter/SaTokenDubboConsumerFilter.java`
  - `snail-common/snail-common-dubbo/src/main/java/cn/dev33/satoken/context/dubbo/filter/SaTokenDubboProviderFilter.java`
- Dubbo Filter SPI：`snail-common/snail-common-dubbo/src/main/resources/META-INF/dubbo/org.apache.dubbo.rpc.Filter`
- 日志切面：`snail-common/snail-common-log/src/main/java/com/snail/common/log/aspect/LogAspect.java`
- 异步日志服务：`snail-common/snail-common-log/src/main/java/com/snail/common/log/service/AsyncLogService.java`
- 系统日志 Dubbo Provider：`snail-sys-provider/snail-sys/src/main/java/com/snail/sys/dubbo/RemoteLogServiceImpl.java`
- 系统启动类：`snail-sys-provider/snail-sys/src/main/java/com/snail/SnailSysApplication.java`

## 10. 后续维护建议

- 每次完成一个模块联调后，把接口路径和关键文件补到本文件
- 如果后端控制器请求方式有调整，优先在这里记录“前端当前依赖的请求方法”
- 如果增加新的全局约定，例如头像、菜单图标、动态路由规范，也放在本文件集中维护
