# snail-cloud

## 平台简介

[![码云Gitee](https://gitee.com/beets/snail-cloud/badge/star.svg?theme=blue)](https://gitee.com/beets/snail-cloud)
[![GitHub](https://img.shields.io/github/stars/JavaLionLi/RuoYi-Cloud-Plus.svg?style=social&label=Stars)](https://github.com/dromara/RuoYi-Cloud-Plus)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://gitee.com/dromara/RuoYi-Cloud-Plus/blob/master/LICENSE)
[![使用IntelliJ IDEA开发维护](https://img.shields.io/badge/IntelliJ%20IDEA-提供支持-blue.svg)](https://www.jetbrains.com/?from=RuoYi-Cloud-Plus)
<br>
[![Snail-Cloud](https://img.shields.io/badge/RuoYi_Cloud_Plus-1.8.2-success.svg)](https://gitee.com/beets/snail-cloud)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-blue.svg)]()
[![JDK-8+](https://img.shields.io/badge/JDK-8-green.svg)]()
[![JDK-11](https://img.shields.io/badge/JDK-11-green.svg)]()


## 🐨 技术栈

| 项目                    | 说明                 |
|-----------------------|--------------------|
| `yudao-dependencies`  | Maven 依赖版本管理       |
| `yudao-framework`     | Java 框架拓展          |
| `yudao-server`        | 管理后台 + 用户 APP 的服务端 |
| `yudao-admin-ui`      | 管理后台的 UI 界面        |
| `yudao-user-ui`       | 用户 APP 的 UI 界面     |
| `snail-sys`           | 系统功能的 Module 模块    |
| `yudao-module-member` | 会员中心的 Module 模块    |
| `yudao-module-infra`  | 基础设施的 Module 模块    |
| `yudao-module-tool`   | 研发工具的 Module 模块    |
| `yudao-module-bpm`    | 工作流程的 Module 模块    |
| `yudao-module-pay`    | 支付系统的 Module 模块    |

### 后端

| 框架                                                                                          | 说明               | 版本       | 学习指南                                                           |
|---------------------------------------------------------------------------------------------|------------------|----------|----------------------------------------------------------------|
| [Spring Boot](https://spring.io/projects/spring-boot)                                       | 应用开发框架           | 2.7.18   | [文档](https://github.com/YunaiV/SpringBoot-Labs)                |
| [MySQL](https://www.mysql.com/cn/)                                                          | 数据库服务器           | 8.0.22   |                                                                |
| [Druid](https://github.com/alibaba/druid)                                                   | JDBC 连接池、监控组件    | 1.2.8    | [文档](http://www.iocoder.cn/Spring-Boot/datasource-pool/?yudao) |
| [MyBatis Plus](https://mp.baomidou.com/)                                                    | MyBatis 增强工具包    | 3.5.4    | [文档](http://www.iocoder.cn/Spring-Boot/MyBatis/?yudao)         |
| [Dynamic Datasource](https://dynamic-datasource.com/)                                       | 动态数据源            | 3.5.2    | [文档](http://www.iocoder.cn/Spring-Boot/datasource-pool/?yudao) |
| [Redis](https://redis.io/)                                                                  | key-value 数据库    | 6.0.2    |                                                                |
| [Redisson](https://github.com/redisson/redisson)                                            | Redis 客户端        | 3.46.0   | [文档](http://www.iocoder.cn/Spring-Boot/Redis/?yudao)           |
| [Spring MVC](https://github.com/spring-projects/spring-framework/tree/master/spring-webmvc) | MVC 框架           | 5.3.20   | [文档](http://www.iocoder.cn/SpringMVC/MVC/?yudao)               |
| [Spring Security](https://github.com/spring-projects/spring-security)                       | Spring 安全框架      | 5.6.5    | [文档](http://www.iocoder.cn/Spring-Boot/Spring-Security/?yudao) |
| [Hibernate Validator](https://github.com/hibernate/hibernate-validator)                     | 参数校验组件           | 6.2.3    | [文档](http://www.iocoder.cn/Spring-Boot/Validation/?yudao)      |
| [Activiti](https://github.com/Activiti/Activiti)                                            | 工作流引擎            | 7.1.0.M6 | [文档](TODO)                                                     |
| [Quartz](https://github.com/quartz-scheduler)                                               | 任务调度组件           | 2.3.2    | [文档](http://www.iocoder.cn/Spring-Boot/Job/?yudao)             |
| [Knife4j](https://gitee.com/xiaoym/knife4j)                                                 | Swagger 增强 UI 实现 | 3.0.3    | [文档](http://www.iocoder.cn/Spring-Boot/Swagger/?yudao)         |
| [Resilience4j](https://github.com/resilience4j/resilience4j)                                | 服务保障组件           | 1.7.1    | [文档](http://www.iocoder.cn/Spring-Boot/Resilience4j/?yudao)    |
| [SkyWalking](https://skywalking.apache.org/)                                                | 分布式应用追踪系统        | 8.5.0    | [文档](http://www.iocoder.cn/Spring-Boot/SkyWalking/?yudao)      |
| [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin)                       | Spring Boot 监控平台 | 2.6.7    | [文档](http://www.iocoder.cn/Spring-Boot/Admin/?yudao)           |
| [Jackson](https://github.com/FasterXML/jackson)                                             | JSON 工具库         | 2.13.3   |                                                                |
| [MapStruct](https://mapstruct.org/)                                                         | Java Bean 转换     | 1.4.6    | [文档](http://www.iocoder.cn/Spring-Boot/MapStruct/?yudao)       |
| [Lombok](https://projectlombok.org/)                                                        | 消除冗长的 Java 代码    | 1.18.30  | [文档](http://www.iocoder.cn/Spring-Boot/Lombok/?yudao)          |
| [JUnit](https://junit.org/junit5/)                                                          | Java 单元测试框架      | 5.8.2    | -                                                              |
| [Mockito](https://github.com/mockito/mockito)                                               | Java Mock 框架     | 4.0.0    | -                                                              |

### 前端

| 框架                                                                           | 说明            | 版本     |
|------------------------------------------------------------------------------|---------------|--------|
| [Vue](https://cn.vuejs.org/index.html)                                       | JavaScript 框架 | 2.6.12 |
| [Vue Element Admin](https://panjiachen.github.io/vue-element-admin-site/zh/) | 后台前端解决方案      | -      |
