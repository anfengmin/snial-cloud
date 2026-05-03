# 登录终端、连续登录天数、在线设备方案

## 1. 目标

本方案覆盖 3 个需求：

1. 登录时识别并记录终端类型，支持 `pc / mobile / app / miniapp`，后续登录日志需要展示。
2. 设计基于 Redis 的连续登录天数方案，既能快速算出连续天数，也能看出某天登录了、某天没登录。
3. 登录成功后，当前用户可以查看自己的在线设备，并且可以按设备踢人下线。

本方案先以当前仓库已有的 Sa-Token + Redis + 登录日志实现为基础扩展，不推翻现有登录链路。

## 2. 当前实现现状

当前登录成功主链路：

- [SysLoginService.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/auth/service/SysLoginService.java:70)
- [LoginUtils.java](/Users/ansir/work/project/gitee/snail-cloud/snail-common/snail-common-satoken/src/main/java/com/snail/common/satoken/utils/LoginUtils.java:48)
- [UserActionListener.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/auth/listener/UserActionListener.java:45)

当前已有数据落点：

- token-session 中缓存 `loginUser`、`userId`
- `online_tokens:{token}` 缓存在线用户对象 [SysUserOnline.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/sys/domain/SysUserOnline.java:17)
- 登录日志表 `sys_login_info` 记录 IP、地点、浏览器、系统 [SysLoginInfo.java](/Users/ansir/work/project/gitee/snail-cloud/snail-common/snail-common-log/src/main/java/com/snail/common/log/domain/SysLoginInfo.java:20)
- 用户表 `sys_user` 已有 `login_ip`、`login_date`，但当前登录成功逻辑没有更新连续登录数据 [SysUser.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/sys/domain/SysUser.java:76)

## 3. 统一术语

为了避免和当前 `LoginType`（密码登录、短信登录等）混淆，后续新增概念统一叫：

- `clientType`：终端类型，值为 `pc / mobile / app / miniapp / unknown`
- `deviceId`：设备标识，客户端生成并尽量稳定
- `deviceName`：设备展示名，例如 `Windows Chrome`、`iPhone App`、`微信小程序`

说明：

- `LoginType` 继续表示登录方式，不改现有语义
- `clientType` 表示终端来源

## 4. 终端类型设计

### 4.1 识别规则

终端类型不能只靠 User-Agent。

原因：

- `pc / mobile` 可以大致通过 UA 判断
- `app / miniapp` 仅靠 UA 不可靠，尤其后续原生 App、小程序、H5 混合时会误判

推荐优先级：

1. 客户端显式传 `X-Client-Type`
2. 客户端可选传 `X-Device-Id`
3. 客户端可选传 `X-Device-Name`
4. 如果没传 `X-Client-Type`，后端再用 UA 兜底：
   - `userAgent.isMobile() == true` => `mobile`
   - 否则 => `pc`

### 4.2 前端与客户端约定

前端 Web：

- PC 浏览器登录时传 `X-Client-Type: pc`
- 手机浏览器登录时传 `X-Client-Type: mobile`
- 浏览器首次进入时生成 `deviceId` 并持久化到 localStorage

App：

- 传 `X-Client-Type: app`
- 传 `X-Device-Id`
- 可选传 `X-Device-Name`、`X-App-Version`

小程序：

- 传 `X-Client-Type: miniapp`
- 传 `X-Device-Id`

### 4.3 后端落点

终端信息建议同时落 4 处：

1. `LoginUser`
2. token-session
3. 在线设备缓存 `online_tokens:{token}`
4. 登录日志 `sys_login_info`

这样做的好处：

- 当前请求内能拿到
- 后续请求通过 token 还能拿到
- 在线用户页能展示
- 登录日志能展示

### 4.4 需要新增字段

#### `LoginUser`

文件：

- [LoginUser.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys-api/src/main/java/com/snail/sys/api/domain/LoginUser.java)

新增建议：

- `private String clientType;`
- `private String deviceId;`
- `private String deviceName;`

#### `SysUserOnline`

文件：

- [SysUserOnline.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/sys/domain/SysUserOnline.java)

新增建议：

- `private String clientType;`
- `private String deviceId;`
- `private String deviceName;`
- `private Boolean currentDevice;`

#### `SysLoginInfo`

文件：

- [SysLoginInfo.java](/Users/ansir/work/project/gitee/snail-cloud/snail-common/snail-common-log/src/main/java/com/snail/common/log/domain/SysLoginInfo.java)

新增建议：

- `private String clientType;`
- `private String deviceId;`
- `private String deviceName;`

对应表 `sys_login_info` 建议加列：

- `client_type varchar(20)`
- `device_id varchar(64)`
- `device_name varchar(128)`

## 5. 连续登录天数 Redis 方案

### 5.1 设计目标

这个需求其实包含两类数据：

1. 汇总值：当前连续登录多少天、历史最长多少天
2. 日历值：某年某月哪天登录了、哪天没登录

最适合的方案不是只存一个计数，而是：

- 用 Redis Hash 存汇总
- 用 Redis Bitmap 存日历

### 5.2 Redis Key 设计

#### A. 连续登录汇总

Key：

- `login:streak:summary:{userId}`

类型：

- `RMap` 或 Redis Hash

字段：

- `lastLoginDate`：最后一次成功登录的自然日，格式 `yyyy-MM-dd`
- `currentStreakDays`：当前连续登录天数
- `maxStreakDays`：历史最长连续登录天数
- `lastLoginAt`：最后登录时间戳

示例：

```json
{
  "lastLoginDate": "2026-05-03",
  "currentStreakDays": "7",
  "maxStreakDays": "18",
  "lastLoginAt": "1777745322000"
}
```

#### B. 登录日历位图

Key：

- `login:calendar:{userId}:{yyyy}`

类型：

- `RBitSet`

位偏移：

- `offset = dayOfYear - 1`

说明：

- 第 1 天为 0
- 第 32 天为 31
- 闰年最多 366 位

示例：

- `login:calendar:1001:2026`
- 2026-05-03 如果是当年第 123 天，则把第 `122` 位设为 `1`

### 5.3 为什么用“按年位图”

不推荐“按月一个 key”的主要原因：

- key 数量太多
- 跨月计算连续天数需要频繁切 key

按年位图的优点：

- 一年只 1 个 key
- 366 位即可覆盖全年
- 月视图和年视图都能从同一个 key 读

### 5.4 登录成功时的更新规则

只在“成功登录”时更新。

登录成功后的处理顺序：

1. 取 `today = LocalDate.now(Asia/Shanghai)`
2. 读取 `login:streak:summary:{userId}`
3. 如果 `lastLoginDate == today`
   - 说明今天已登录过
   - 连续天数不变
   - 只补齐今日位图即可
4. 如果 `lastLoginDate == yesterday`
   - `currentStreakDays = currentStreakDays + 1`
5. 其他情况
   - `currentStreakDays = 1`
6. `maxStreakDays = max(maxStreakDays, currentStreakDays)`
7. 更新 `lastLoginDate / lastLoginAt`
8. 在当年位图把今天对应位设为 `1`

示例：

- 2026-05-01 首次登录 => `1`
- 2026-05-01 当天再登录 => 还是 `1`
- 2026-05-02 登录 => `2`
- 2026-05-03 登录 => `3`
- 2026-05-05 登录 => 因为 2026-05-04 断了，重置为 `1`

### 5.5 并发控制

同一用户可能在极短时间内多个端同时成功登录。

否则会出现：

- 连续天数加两次
- `lastLoginDate` 竞争覆盖

推荐方案：

- 使用 Redisson 分布式锁
- 锁 key：`login:streak:lock:{userId}`

只包住“读取汇总 + 更新汇总 + 设置位图”这段逻辑。

这是后续实现阶段必须做的，不建议省略。

### 5.6 查询接口设计

#### 当前连续登录摘要

接口建议：

- `GET /sysUser/login-streak`

返回：

```json
{
  "currentStreakDays": 7,
  "maxStreakDays": 18,
  "lastLoginDate": "2026-05-03"
}
```

#### 月度登录日历

接口建议：

- `GET /sysUser/login-calendar?year=2026&month=5`

返回建议：

```json
{
  "year": 2026,
  "month": 5,
  "currentStreakDays": 7,
  "maxStreakDays": 18,
  "days": [
    { "date": "2026-05-01", "loggedIn": true },
    { "date": "2026-05-02", "loggedIn": true },
    { "date": "2026-05-03", "loggedIn": true },
    { "date": "2026-05-04", "loggedIn": false }
  ]
}
```

这样前端最容易直接做日历。

### 5.7 Redis-only 的风险与补偿

你要求设计成 Redis 方案，这可以做，但要明确：

- 如果 Redis 数据被清空，连续登录和历史登录日历都会丢

因此建议同时保留两件事：

1. 登录日志继续落库到 `sys_login_info`
2. 预留一个“从登录日志重建某个用户登录位图与 streak 摘要”的修复能力

这样运行态走 Redis，审计与修复走数据库。

## 6. 当前用户在线设备方案

### 6.1 目标

当前用户登录后，能看到自己有哪些在线设备，例如：

- PC
- Mobile
- App
- MiniApp

并且可以按设备下线。

### 6.2 现状

当前系统已有全量在线用户接口：

- [SysUserOnlineController.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysUserOnlineController.java:35)

但它是管理员视角，不是“当前用户自己的设备列表”。

### 6.3 推荐接口

#### 查询当前用户在线设备

- `GET /online/current-user/devices`

逻辑：

- 取当前登录用户 `userId`
- 遍历 `online_tokens:*`
- 过滤 `userId` 相同的记录
- 标记当前 token 为 `currentDevice = true`

返回字段建议：

- `tokenId`
- `clientType`
- `deviceName`
- `deviceId`
- `browser`
- `os`
- `ipaddr`
- `loginLocation`
- `loginTime`
- `currentDevice`

#### 踢掉当前用户某个设备

- `DELETE /online/current-user/devices/{tokenId}`

逻辑：

- 先校验这个 token 对应的在线记录是否属于当前用户
- 如果不属于，拒绝
- 属于则执行 `StpUtil.kickoutByTokenValue(tokenId)`
- 并删除 `online_tokens:{tokenId}`

说明：

- 这个接口既可以踢别的端
- 也可以允许踢自己当前端
- 如果踢的是当前 token，前端需要走登出并回登录页

### 6.4 在线缓存结构建议

在线缓存对象 [SysUserOnline.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/sys/domain/SysUserOnline.java:17) 建议扩展：

- `userId`
- `clientType`
- `deviceId`
- `deviceName`

原因：

- 当前在线设备列表必须靠这些字段过滤与展示
- 只存 `userCode` 不如存 `userId` 稳

## 7. 实施顺序

建议按 4 个阶段落地：

### Phase 1. 终端类型基础链路

范围：

- 增加 `clientType / deviceId / deviceName`
- 登录请求头读取与兜底识别
- 写入 `LoginUser`、token-session、在线缓存、登录日志

产出：

- 登录日志能展示终端类型
- 在线用户缓存能区分 `pc / mobile / app / miniapp`

### Phase 2. Redis 连续登录能力

范围：

- 增加 Redis streak summary + yearly bitmap
- 登录成功更新 streak
- 提供“连续登录摘要”和“月日历”接口

产出：

- 能看到当前连续登录天数
- 能看到某天登录 / 某天未登录

### Phase 3. 当前用户在线设备

范围：

- 新增“当前用户设备列表”接口
- 新增“按 token 踢掉自己的其他设备”接口

产出：

- 用户中心可查看自己的在线终端
- 可以踢掉某个端

### Phase 4. 前端接入

范围：

- 登录日志页新增 `clientType`
- 在线用户页新增 `clientType / deviceName`
- 用户中心新增“我的在线设备”
- 用户中心新增“连续登录日历”

## 8. 本次执行建议

后续真正开始开发时，建议严格按下面顺序执行：

1. 后端终端类型识别与字段扩展
2. 登录日志与在线缓存扩展
3. Redis streak 服务与接口
4. 当前用户在线设备接口
5. 前端登录日志页
6. 前端用户中心“在线设备 + 连续登录”

## 9. 结论

最终推荐方案：

- 终端类型：`X-Client-Type` 为主，UA 为辅
- 登录缓存：把 `clientType / deviceId / deviceName` 进入 `LoginUser + token-session + online cache`
- 登录日志：新增终端类型字段，后续页面展示
- 连续登录：Redis `Hash + Yearly Bitmap`
- 在线设备：新增“当前用户自己的设备列表与踢下线接口”

这个方案和当前仓库最贴合，后续扩展到 `pc / mobile / app / miniapp` 也不会推翻重做。
