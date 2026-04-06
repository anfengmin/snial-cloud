# snail-cloud 项目知识文档

## 1. 项目定位

- 仓库路径：`/Users/ansir/work/project/gitee/snail-cloud`
- 本地参考项目：`/Users/ansir/work/project/gitee/RuoYi-Cloud-Plus`
- 当前联调重点模块：用户、角色、菜单、部门、岗位、字典、在线用户
- 前端联调仓库对应：`/Users/ansir/work/project/gitee/snail-vue`

## 2. 当前前后端联调约定

- 前端请求参数直接按后端字段名走
- 用户管理编辑详情使用：`GET /sysUser/info/{userId}`
- 用户管理列表使用：`POST /sysUser/queryByPage`
- 部门列表使用：`POST /dept/list`
- 字典类型分页使用：`POST /dict/type/queryByPage`

## 3. 关键控制器

### 3.1 用户

- 文件：`snail-sys/src/main/java/com/snail/sys/controller/SysUserController.java`
- 关键接口：
  - `POST /sysUser/queryByPage`
  - `GET /sysUser/getUserInfo`
  - `GET /sysUser/info/{userId}`
  - `POST /sysUser/add`
  - `PUT /sysUser/edit`
  - `DELETE /sysUser/remove`

### 3.2 角色

- 文件：`snail-sys/src/main/java/com/snail/sys/controller/SysRoleController.java`

### 3.3 菜单

- 文件：`snail-sys/src/main/java/com/snail/sys/controller/SysMenuController.java`

### 3.4 部门

- 文件：`snail-sys/src/main/java/com/snail/sys/controller/SysDeptController.java`

### 3.5 岗位

- 主数据控制器：`snail-sys/src/main/java/com/snail/sys/controller/SysPostController.java`
- 说明：`SysUserPostController` 只负责用户与岗位关联，不负责岗位管理页面

### 3.6 字典

- 字典类型：`snail-sys/src/main/java/com/snail/sys/controller/SysDictTypeController.java`
- 字典数据：`snail-sys/src/main/java/com/snail/sys/controller/SysDictDataController.java`

### 3.7 在线用户

- 文件：`snail-sys/src/main/java/com/snail/sys/controller/SysUserOnlineController.java`

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
- `snail-sys` 已接入 `snail-common-dubbo`，启动类增加了 `@EnableDubbo`
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
  - `snail-sys` 中的 `RemoteLogServiceImpl` 写入 `sys_operate_log` / `sys_login_info`
- `snail-sys-api` 中的 `SysOperateLog`、`SysLoginInfo` 已去掉 `Model` 继承，保持 API 模块轻量，避免额外依赖 `mybatis-plus-extension`

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

- 用户服务：`snail-sys/src/main/java/com/snail/sys/service/impl/SysUserServiceImpl.java`
- 角色服务：`snail-sys/src/main/java/com/snail/sys/service/impl/SysRoleServiceImpl.java`
- 部门服务：`snail-sys/src/main/java/com/snail/sys/service/impl/SysDeptServiceImpl.java`
- 字典类型服务：`snail-sys/src/main/java/com/snail/sys/service/impl/SysDictTypeServiceImpl.java`
- 字典数据服务：`snail-sys/src/main/java/com/snail/sys/service/impl/SysDictDataServiceImpl.java`

## 7. 数据库与脚本

- 初始化 SQL：`config/sql/snail_sys.sql`
- 如果联调字段与数据库不一致，优先以实际控制器、实体类和 SQL 三方交叉确认

## 8. 当前环境已知问题

- Maven `settings.xml` 解析问题已修复
- 2026-04-06 已完成一次完整验证：`mvn -pl snail-sys -am -DskipTests compile` 通过
- `snail-sys/pom.xml` 中重复的 `spring-cloud-starter-alibaba-nacos-discovery` 依赖声明已清理

## 9. 关键模块与文件

- Dubbo 公共模块：`snail-common/snail-common-dubbo`
- Dubbo 自动配置：`snail-common/snail-common-dubbo/src/main/java/com/snail/common/dubbo/config/DubboConfiguration.java`
- Sa-Token Dubbo 过滤器：
  - `snail-common/snail-common-dubbo/src/main/java/cn/dev33/satoken/context/dubbo/filter/SaTokenDubboConsumerFilter.java`
  - `snail-common/snail-common-dubbo/src/main/java/cn/dev33/satoken/context/dubbo/filter/SaTokenDubboProviderFilter.java`
- Dubbo Filter SPI：`snail-common/snail-common-dubbo/src/main/resources/META-INF/dubbo/org.apache.dubbo.rpc.Filter`
- 日志切面：`snail-common/snail-common-log/src/main/java/com/snail/common/log/aspect/LogAspect.java`
- 异步日志服务：`snail-common/snail-common-log/src/main/java/com/snail/common/log/service/AsyncLogService.java`
- 系统日志 Dubbo Provider：`snail-sys/src/main/java/com/snail/sys/dubbo/RemoteLogServiceImpl.java`
- 系统启动类：`snail-sys/src/main/java/com/snail/SnailSysApplication.java`

## 10. 后续维护建议

- 每次完成一个模块联调后，把接口路径和关键文件补到本文件
- 如果后端控制器请求方式有调整，优先在这里记录“前端当前依赖的请求方法”
- 如果增加新的全局约定，例如头像、菜单图标、动态路由规范，也放在本文件集中维护
