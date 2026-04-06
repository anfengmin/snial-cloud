# snail-cloud 项目知识文档

## 1. 项目定位

- 仓库路径：`/Users/ansir/work/project/gitee/snail-cloud`
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
- 用户头像支持自动生成 base64 默认头像

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

## 5. 用户头像默认生成规则

### 5.1 生效场景

- 注册用户
- 后台新增用户
- 后台修改用户
- 查询用户详情/当前用户/用户列表时发现数据库头像为空

### 5.2 生成规则

- 头像格式：`data:image/png;base64,...`
- 文本来源：`userName`
- 中文：
  - 2 个字时显示全部
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

- 本地 Maven 全局 `settings.xml` 存在解析错误
- 因此当前环境下后端经常无法稳定做完整 Maven 编译验证
- 出问题时优先做代码级修正，再视情况单独处理 Maven 环境

## 9. 后续维护建议

- 每次完成一个模块联调后，把接口路径和关键文件补到本文件
- 如果后端控制器请求方式有调整，优先在这里记录“前端当前依赖的请求方法”
- 如果增加新的全局约定，例如头像、菜单图标、动态路由规范，也放在本文件集中维护
