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
| `snail-sys`           | 系统功能的 Module 模块    |
| `snail-sys-api`       | 系统功能的 api 模块       |


### 后端

| 框架                                                                                          | 说明               | 版本         | 学习指南                                                                          |
|---------------------------------------------------------------------------------------------|------------------|------------|-------------------------------------------------------------------------------|
| [Spring Boot](https://spring.io/projects/spring-boot)                                       | 应用开发框架           | 2.7.18     | [文档](https://github.com/YunaiV/SpringBoot-Labs)                               |
| [Spring Cloud](https://spring.io/projects/spring-cloud)                                     | 微服务框架            | 2021.0.8   | [文档](https://spring.io/projects/spring-cloud)                                 |
| [Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba)                     | 微服务解决方案          | 2021.0.5.0 | [文档](https://github.com/alibaba/spring-cloud-alibaba)                         |
| [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)                     | 网关框架             | -          | [文档](https://spring.io/projects/spring-cloud-gateway)                         |
| [Nacos](https://nacos.io/)                                                                  | 服务注册与配置中心        | -          | [文档](https://nacos.io/docs/latest/what-is-nacos/)                             |
| [MySQL](https://www.mysql.com/cn/)                                                          | 数据库服务器           | 8.0+       |                                                                               |
| [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/)                                 | MySQL JDBC 驱动    | 9.2.0      |                                                                               |
| [Dynamic Datasource](https://dynamic-datasource.com/)                                       | 动态数据源            | 3.5.2      | [文档](http://www.iocoder.cn/Spring-Boot/datasource-pool/?yudao)                |
| [MyBatis](https://mybatis.org/mybatis-3/)                                                   | ORM 框架           | 3.5.13     | [文档](http://www.iocoder.cn/Spring-Boot/MyBatis/?yudao)                        |
| [MyBatis Plus](https://mp.baomidou.com/)                                                    | MyBatis 增强工具包    | 3.5.4      | [文档](http://www.iocoder.cn/Spring-Boot/MyBatis/?yudao)                        |
| [P6Spy](https://github.com/p6spy/p6spy)                                                     | SQL 性能分析插件       | 3.9.1      | [文档](https://p6spy.readthedocs.io/)                                           |
| [Redis](https://redis.io/)                                                                  | key-value 数据库    | 6.0+       |                                                                               |
| [Redisson](https://github.com/redisson/redisson)                                            | Redis 客户端        | 3.46.0     | [文档](http://www.iocoder.cn/Spring-Boot/Redis/?yudao)                          |
| [Lock4j](https://github.com/baomidou/lock4j)                                                | 分布式锁组件           | 2.2.3      | [文档](https://github.com/baomidou/lock4j)                                      |
| [Sa-Token](https://sa-token.cc/)                                                            | 权限认证框架           | 1.42.0     | [文档](https://sa-token.cc/doc.html)                                            |
| [Spring MVC](https://github.com/spring-projects/spring-framework/tree/master/spring-webmvc) | MVC 框架           | 5.3.20     | [文档](http://www.iocoder.cn/SpringMVC/MVC/?yudao)                              |
| [Undertow](https://undertow.io/)                                                            | Web 服务器          | -          | [文档](https://undertow.io/documentation.html)                                  |
| [Hibernate Validator](https://github.com/hibernate/hibernate-validator)                     | 参数校验组件           | 6.2.3      | [文档](http://www.iocoder.cn/Spring-Boot/Validation/?yudao)                     |
| [XXL-Job](https://github.com/xuxueli/xxl-job)                                               | 分布式任务调度平台        | 2.4.0      | [文档](https://www.xuxueli.com/xxl-job/)                                        |
| [SpringDoc OpenAPI](https://springdoc.org/)                                                 | API 文档工具         | 1.6.15     | [文档](https://springdoc.org/)                                                  |
| [Springfox Swagger](https://github.com/springfox/springfox)                                 | Swagger 实现       | 2.9.2      | [文档](http://www.iocoder.cn/Spring-Boot/Swagger/?yudao)                        |
| [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin)                       | Spring Boot 监控平台 | 2.7.11     | [文档](http://www.iocoder.cn/Spring-Boot/Admin/?yudao)                          |
| [Jackson](https://github.com/FasterXML/jackson)                                             | JSON 工具库         | 2.13.3     |                                                                               |
| [MapStruct Plus](https://github.com/linpeilie/mapstruct-plus)                               | Java Bean 转换     | 1.4.6      | [文档](https://github.com/linpeilie/mapstruct-plus)                             |
| [Lombok](https://projectlombok.org/)                                                        | 消除冗长的 Java 代码    | 1.18.30    | [文档](http://www.iocoder.cn/Spring-Boot/Lombok/?yudao)                         |
| [Hutool](https://hutool.cn/)                                                                | Java 工具类库        | 5.8.37     | [文档](https://hutool.cn/docs/)                                                 |
| [EasyExcel](https://github.com/alibaba/easyexcel)                                           | Excel 处理工具       | 3.3.2      | [文档](https://easyexcel.opensource.alibaba.com/)                               |
| [Apache POI](https://poi.apache.org/)                                                       | Office 文档处理工具    | 5.2.3      | [文档](https://poi.apache.org/)                                                 |
| [Easy-ES](https://github.com/dromara/easy-es)                                               | Elasticsearch 工具 | 1.1.1      | [文档](https://www.easy-es.cn/)                                                 |
| [Elasticsearch](https://www.elastic.co/cn/elasticsearch/)                                   | 搜索引擎             | 7.14.0     | [文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.14/index.html) |
| [SkyWalking](https://skywalking.apache.org/)                                                | 分布式应用追踪系统        | 8.14.0     | [文档](http://www.iocoder.cn/Spring-Boot/SkyWalking/?yudao)                     |
| [Logstash Logback Encoder](https://github.com/logstash/logstash-logback-encoder)            | 日志编码器            | 7.2        | [文档](https://github.com/logstash/logstash-logback-encoder)                    |
| [SMS4j](https://github.com/dromara/sms4j)                                                   | 短信服务框架           | 2.2.0      | [文档](https://gitee.com/dromara/sms4j)                                         |
| [ip2region](https://github.com/lionsoul2014/ip2region)                                      | 离线IP地址定位库        | 2.7.0      | [文档](https://github.com/lionsoul2014/ip2region)                               |
| [Fastjson](https://github.com/alibaba/fastjson)                                             | JSON 解析库         | 1.2.83     | [文档](https://github.com/alibaba/fastjson/wiki)                                |
| [AWS S3 SDK](https://aws.amazon.com/sdk-for-java/)                                          | 对象存储 SDK         | 1.12.540   | [文档](https://docs.aws.amazon.com/sdk-for-java/)                               |
| [OkHttp](https://square.github.io/okhttp/)                                                  | HTTP 客户端         | 4.10.0     | [文档](https://square.github.io/okhttp/)                                        |
| [Bouncycastle](https://www.bouncycastle.org/)                                               | 加密算法库            | 1.72       | [文档](https://www.bouncycastle.org/documentation.html)                         |
| [Apache Velocity](https://velocity.apache.org/)                                             | 模板引擎             | 2.3        | [文档](https://velocity.apache.org/)                                            |
| [JUnit](https://junit.org/junit5/)                                                          | Java 单元测试框架      | 5.8.2      | -                                                                             |
| [Mockito](https://github.com/mockito/mockito)                                               | Java Mock 框架     | 4.0.0      | -                                                                             |


### 前端

| 框架                                                                           | 说明            | 版本     |
|------------------------------------------------------------------------------|---------------|--------|
| [Vue](https://cn.vuejs.org/index.html)                                       | JavaScript 框架 | 2.6.12 |
| [Vue Element Admin](https://panjiachen.github.io/vue-element-admin-site/zh/) | 后台前端解决方案      | -      |
