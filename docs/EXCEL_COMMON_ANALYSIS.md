# 公共 Excel 导入导出组件分析报告

## 1. 背景与目标

目标是在 `common` 层沉淀一套可复用的 Excel 导入导出能力，供系统内多个业务模块统一接入，并补齐以下能力：

- 公共导入
- 公共导出
- 大数据量分页写出
- 自动分 Sheet
- 可选压缩包导出
- 前后端统一接入方式
- 统一错误回执与模板机制

本报告基于当前仓库现状给出差距分析与推荐方案，不直接进入实现。

## 2. 当前现状

### 2.1 后端已有基础能力

当前后端已经存在一套 Excel 基础工具，位于：

- `snail-common/snail-common-core/src/main/java/com/snail/common/core/excel`
- 核心工具类：
  - `com.snail.common.core.excel.utils.ExcelUtil`
  - `com.snail.common.core.excel.core.DefaultExcelListener`
  - `com.snail.common.core.excel.core.ExcelDownHandler`
  - `com.snail.common.core.excel.convert.ExcelDictConvert`
  - `com.snail.common.core.excel.convert.ExcelEnumConvert`

已具备的能力：

- 基于 `EasyExcel` 的普通导入
- 基于监听器的导入校验
- 普通单 Sheet 导出
- 模板导出
- 字典/枚举转换
- 单元格合并
- 下拉框生成
- 大数字转字符串防失真

### 2.2 当前能力边界

现有 `ExcelUtil` 更偏向“单次内存式工具类”，主要特征如下：

- 导出入参以 `List<T>` 为主，调用方需要一次性把全部数据查到内存
- 普通导出只支持单 Sheet 写出
- 没有“按页查询 + 边查边写”的能力
- 没有“达到阈值后自动拆分 Sheet”的能力
- 没有“多工作簿 + ZIP 打包”的能力
- 没有导出任务进度、任务记录、异步通知能力
- 导入回执较简单，缺少标准错误文件导出机制

### 2.3 前端现状

前端目前没有统一的 Excel 导入导出公共组件。

现状特征：

- 表格公共头组件 [table-header-operation.vue](/Users/ansir/work/project/gitee/snail-vue/src/components/advanced/table-header-operation.vue:1) 只支持新增、批量删除、刷新、列设置
- `system-manage.ts` 中目前没有统一的 Excel 导入导出 API 封装
- 请求层 [request/index.ts](/Users/ansir/work/project/gitee/snail-vue/src/service/request/index.ts:1) 也没有专门的 `blob` 下载封装
- 多个页面虽有“导入/导出”权限和文案，但还没有形成统一接入协议

### 2.4 业务层现状

系统控制器层当前几乎没有真正落地的 Excel 导入导出接口，典型如：

- [SysUserController.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/sys/controller/SysUserController.java:1)

说明当前系统更像是：

- 权限点和文案已预留
- common 已有底层 Excel 工具
- 但缺少“业务 Controller + Service + 前端组件”的完整闭环

## 3. 主要问题

### 3.1 大数据量导出不可用

当前普通导出要求先构造完整 `List<T>`，存在明显问题：

- 大表导出会占用大量 JVM 堆内存
- 查询和写出无法流水化
- 容易超时
- 不适合百万级记录导出

### 3.2 单 Sheet 能力不足

当前导出没有分 Sheet 机制，而 `xlsx` 单 Sheet 理论上限为 1,048,576 行。实际业务里通常不建议写满，原因包括：

- 打开慢
- 内存占用高
- 用户体验差
- 样式、下拉、合并等复杂能力会进一步放大成本

### 3.3 压缩能力定义不清

这里需要先明确一个关键事实：

- 单个 `.xlsx` 文件本身就是 ZIP 容器格式

所以“压缩”不能简单理解为“所有导出文件都再 zip 一次”，否则收益有限，还会增加使用复杂度。

更合理的压缩场景是：

- 导出结果被拆成多个工作簿时，统一打成 ZIP
- 主文件 + 错误文件 + 说明文件一起打包
- 模板包批量下载

### 3.4 导入能力不够完整

当前导入监听器 [DefaultExcelListener.java](/Users/ansir/work/project/gitee/snail-cloud/snail-common/snail-common-core/src/main/java/com/snail/common/core/excel/core/DefaultExcelListener.java:1) 有几个明显问题：

- 以同步返回结果为主，不适合超大文件导入
- 校验异常后会中断解析，不利于收集完整错误
- 错误信息只有字符串列表，没有标准错误文件
- 没有“逐批处理 / 逐行回调 / 分批入库”协议

### 3.5 前后端接入不统一

目前即使后端补齐能力，前端也缺：

- 公共导出按钮
- 公共导入弹窗
- 模板下载入口
- `blob` 下载与文件名解析
- 导入结果回执展示
- 异步导出任务列表

## 4. 推荐目标形态

建议把“公共 Excel 组件”拆成两层，而不是只做一个大工具类。

### 4.1 后端 common 层

职责：

- 提供真正可复用的 Excel 核心能力
- 不依赖具体业务表
- 不依赖具体 controller
- 支持同步小数据和异步大数据两种模式

建议沉淀在：

- `snail-common/snail-common-core`
- 可新增包：`com.snail.common.core.excel.support`

### 4.2 前端 common 层

职责：

- 提供统一导入导出交互
- 屏蔽下载、上传、回执细节
- 让各列表页只关心“传什么参数、用什么接口”

建议沉淀在：

- `snail-vue/src/components/common/excel`
- 或 `snail-vue/src/hooks/business/excel`

## 5. 推荐后端方案

### 5.1 导出能力拆分

建议至少分成 3 个级别：

#### 级别 A：同步小数据导出

适用场景：

- 1 万以内
- 后台管理小表
- 需要即时下载

特点：

- 保留 `List<T>` 入参
- 直接写 `HttpServletResponse`
- 用于替代当前 `ExcelUtil.exportExcel`

#### 级别 B：分页流式导出

适用场景：

- 1 万到 50 万
- 数据分页查询明显
- 单个工作簿可以容纳

建议接口形态：

- `ExcelPageFetcher<T>`
- `ExcelExportRequest<T>`
- `ExcelExportService.exportByPage(...)`

核心思路：

- 调用方传“分页取数函数”
- 组件内部循环查页
- 每页写入当前 Sheet
- 达到阈值自动切下一个 Sheet

#### 级别 C：异步大数据导出

适用场景：

- 50 万以上
- 导出耗时长
- 需要离线下载

核心思路：

- 后端创建导出任务
- 后台异步执行
- 生成 Excel 或 ZIP
- 上传到 OSS
- 前端轮询任务状态并下载

### 5.2 分 Sheet 策略

建议不要等到 Excel 理论极限才拆分，推荐做业务阈值：

- 默认每个 Sheet 10 万或 20 万行
- 可在请求里覆盖
- 超过阈值自动生成 `sheetName_1`、`sheetName_2`

这样更稳，打开体验也更好。

建议新增：

- `ExcelSheetPolicy`
- `maxRowsPerSheet`
- `sheetNameGenerator`

### 5.3 压缩策略

推荐按以下规则处理：

- 单工作簿导出：直接返回 `.xlsx`
- 多 Sheet 但仍在一个工作簿内：仍直接返回 `.xlsx`
- 超过单工作簿规划上限，需要拆多个工作簿：输出 `.zip`
- 导入错误明细 + 原始模板 + 导入回执一起下载：输出 `.zip`

建议不要对所有导出都强制 zip。

### 5.4 导入能力增强

建议新增 2 套导入模式：

#### 模式 A：同步校验导入

适合小文件：

- 读完后返回 `successCount`、`failCount`、`errors`

#### 模式 B：批处理导入

适合大文件：

- 监听器按批次回调
- 每 500 / 1000 行交给业务处理器
- 避免全量堆积在内存

建议新增接口：

- `ExcelBatchConsumer<T>`
- `ExcelImportRequest<T>`
- `ExcelImportResult`

导入错误建议支持：

- 行号
- 列名
- 原始值
- 错误原因
- 错误 Excel 回执文件

### 5.5 与 OSS 的结合

当前存储层已经具备字节上传能力：

- [StorageManager.java](/Users/ansir/work/project/gitee/snail-cloud/snail-common/snail-common-storage/src/main/java/com/snail/common/storage/service/StorageManager.java:1)
- [SysOssService.java](/Users/ansir/work/project/gitee/snail-cloud/snail-sys-provider/snail-sys/src/main/java/com/snail/sys/service/SysOssService.java:1)

这意味着异步导出结果完全可以：

- 先在内存/临时文件生成
- 再通过 `uploadContent(...)` 或扩展流式上传接口落到 OSS

建议中期方案直接复用现有 OSS 能力，不重复造文件存储轮子。

## 6. 推荐前端方案

### 6.1 公共组件建议

建议至少提供以下公共能力：

- `ExcelExportButton`
- `ExcelImportButton`
- `ExcelImportDialog`
- `useExcelTransfer`

### 6.2 前端能力范围

建议统一封装：

- 下载模板
- 上传 Excel
- `blob` 文件下载
- 从响应头解析文件名
- 导入结果弹窗
- 异步导出任务轮询

### 6.3 与现有表头组件的关系

可以扩展 [table-header-operation.vue](/Users/ansir/work/project/gitee/snail-vue/src/components/advanced/table-header-operation.vue:1)，但不建议把所有 Excel 逻辑直接塞进去。

更稳的方式是：

- `TableHeaderOperation` 保持轻量
- 通过 `prefix` / `suffix` 插槽挂 `ExcelExportButton`、`ExcelImportButton`

这样不会把列表头组件做成巨无霸。

## 7. 推荐类设计

### 7.1 后端 common

建议新增：

- `ExcelExportRequest<T>`
- `ExcelImportRequest<T>`
- `ExcelExportResult`
- `ExcelImportResult`
- `ExcelPageFetcher<T>`
- `ExcelBatchConsumer<T>`
- `ExcelSheetPolicy`
- `ExcelZipBundle`
- `ExcelExportService`
- `ExcelImportService`

### 7.2 任务化能力

如果要做异步大导出，不建议直接塞进 common-core。更建议：

- common 提供“导出执行器”
- sys 或独立业务模块提供“任务记录表 + 任务状态接口”

原因是：

- 任务状态、下载权限、清理策略都属于业务层
- common 负责能力，sys 负责治理

## 8. 实施建议

### Phase 1：重构 common Excel 核心

目标：

- 保留现有 `ExcelUtil` 兼容能力
- 新增分页导出、分 Sheet、标准导入结果

输出：

- 新 `ExcelExportService`
- 新 `ExcelImportService`
- 支持按页写出
- 支持自动分 Sheet

### Phase 2：前端统一组件

目标：

- 页面接入成本降到最低

输出：

- `ExcelExportButton`
- `ExcelImportDialog`
- `useExcelTransfer`
- `blob` 下载封装

### Phase 3：异步大数据导出

目标：

- 支持超大数据量导出

输出：

- 导出任务表
- 导出状态查询接口
- OSS 落盘
- ZIP 打包下载

## 9. 风险与注意点

### 9.1 不能把所有场景都做成同步下载

否则会遇到：

- 请求超时
- 网关超时
- 浏览器中断
- 服务内存压力大

### 9.2 ZIP 不是越早引入越好

单个 `.xlsx` 再额外 zip，收益不高。压缩应该服务于：

- 多文件打包
- 超大导出拆分
- 错误回执集合

### 9.3 common 不宜耦合业务任务表

common 应提供能力抽象，不应直接绑定：

- 用户表
- 日志表
- 某个业务模块的任务状态表

### 9.4 导入必须支持错误回执

否则业务人员很难定位第几行、第几列、什么值出错。

## 10. 结论

结论很明确：

1. 当前仓库已经有 Excel 基础设施，但还不是“完整的公共导入导出组件”。
2. 现在最缺的是分页写出、自动分 Sheet、统一前端接入、异步任务化、ZIP 打包策略。
3. 推荐先在 `snail-common-core` 做能力升级，再在 `snail-vue` 做统一交互组件，最后再接异步导出任务。
4. “压缩”建议作为多工作簿/多文件结果的增强能力，而不是所有导出默认动作。

## 11. 推荐下一步

建议下一步直接进入方案设计稿，而不是立刻开写全部代码。

推荐顺序：

1. 先定义后端公共接口与对象模型
2. 再补一个最小可用同步分页导出实现
3. 然后落一个示例页面接入
4. 最后扩展 ZIP 和异步任务
