# snail-cloud

`snail-cloud` is a microservice backend project built on Spring Boot 2.7, Spring Cloud 2021, Spring Cloud Alibaba 2021, Dubbo 3, and Sa-Token.

The current repository focuses on the system domain and already includes core capabilities such as user management, role management, menu management, department management, post management, dictionary management, and online users. The `sys-api` module is kept as the contract layer for future business services.

## Highlights

- Built on `Spring Cloud Gateway + Nacos + Dubbo + Sa-Token`
- Uses a domain-oriented module layout instead of flattening all services in the repository root
- `common` is decoupled from business `api` modules and can be built independently
- System logs are written asynchronously through `Dubbo RemoteLogService`
- Sa-Token has been adapted for Dubbo internal calls to avoid context initialization issues

## Tech Stack

### Backend

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

### Related Repositories

- Backend: `/Users/ansir/work/project/gitee/snail-cloud`
- Frontend: `/Users/ansir/work/project/gitee/snail-vue`
- Local reference project: `/Users/ansir/work/project/gitee/RuoYi-Cloud-Plus`

## Current Directory Layout

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

## Module Overview

### Common Layer

- `snail-common-bom`
  - shared dependency version management
- `snail-common-core`
  - base utilities, shared models, common configuration
- `snail-common-dubbo`
  - Dubbo shared configuration and Sa-Token Dubbo filters
- `snail-common-log`
  - `@Log`, log aspect, remote log API, log domain models
- `snail-common-mybatis`
  - MyBatis, dynamic datasource, database drivers
- `snail-common-redis`
  - Redis, Redisson, distributed locking
- `snail-common-satoken`
  - Sa-Token core integration
- `snail-common-security`
  - security context and permission helpers
- `snail-common-web`
  - common web capabilities such as Undertow and Actuator

### Gateway Layer

- `snail-gateway`
  - unified gateway entry
  - routing, authentication, and common gateway filters

### System Domain

- `snail-sys-provider/snail-sys-api`
  - contract layer for the system domain
  - intended to be used directly by future services such as `flow`
- `snail-sys-provider/snail-sys`
  - system domain implementation
  - includes auth, user, role, menu, department, post, dictionary, and log features

## Recommended Expansion Pattern

This repository intentionally does not add an extra `snail-modules` directory.  
For future business domains, keep the same layout directly under the root:

```text
snail-flow-provider
├─ snail-flow-api
└─ snail-flow
```

This keeps:

- each domain's `api` and provider close to each other
- the repository root from becoming too flat as services grow
- `common` focused on infrastructure only

## Requirements

- JDK `8+`
- Maven `3.9+`
- MySQL `8+`
- Redis `6+`
- Nacos `2.x`

Recommended local setup:

- JDK `1.8`
- Maven `3.9.14`

## Configuration

### Nacos Configuration

Global shared configuration is maintained in:

- `config/nacos/snail-global.yml`

This file contains important shared settings such as:

- Dubbo configuration
- Nacos configuration
- async executor defaults

### SQL Initialization

- `config/sql/snail_sys.sql`

## Build Commands

### Full Validation

```bash
mvn -DskipTests validate
```

### Compile the System Service

```bash
mvn -pl snail-sys-provider/snail-sys -am -DskipTests compile
```

### Package the System Service

```bash
mvn -pl snail-sys-provider/snail-sys -am -DskipTests package
```

### Build the Common Layer Only

```bash
mvn -f snail-common/pom.xml -DskipTests package
```

Notes:

- Because `snail-sys-provider` is a nested module, you should reference the full path `snail-sys-provider/snail-sys` from the repository root
- If you build a provider outside the root reactor, make sure `snail-common-bom` and common artifacts already exist in your local repository

## Suggested Startup Order

For local development, start services in this order:

1. Nacos
2. MySQL
3. Redis
4. `snail-sys`
5. `snail-gateway`
6. frontend `snail-vue`

## Key Architecture Notes

### Dubbo and Sa-Token

The project provides dedicated Dubbo filters in `snail-common-dubbo`:

- `SaTokenDubboConsumerFilter`
- `SaTokenDubboProviderFilter`

They are used to:

- initialize the Sa-Token context in the Dubbo invocation chain
- avoid metadata/context initialization errors in internal RPC calls
- support token and same-token propagation in Dubbo calls

### Log Pipeline

The current log pipeline is:

```text
@Log -> LogAspect -> AsyncLogService -> RemoteLogService -> database tables
```

Important files:

- `snail-common/snail-common-log/src/main/java/com/snail/common/log/aspect/LogAspect.java`
- `snail-common/snail-common-log/src/main/java/com/snail/common/log/service/AsyncLogService.java`
- `snail-sys-provider/snail-sys/src/main/java/com/snail/sys/dubbo/RemoteLogServiceImpl.java`

## Covered System Modules

- User Management
- Role Management
- Menu Management
- Department Management
- Post Management
- Dictionary Management
- Online Users

## Project Knowledge File

Operational conventions and recent architecture decisions are recorded in:

- `docs/PROJECT_KNOWLEDGE.md`

It should be updated whenever you:

- add a new domain module
- change module layout
- adjust important API paths
- introduce a new global convention
- refactor the build or dependency boundaries

## FAQ

### Why should `common` not depend on `sys-api`?

Because multiple business domains will be added later.  
`common` must remain infrastructure-only, otherwise build boundaries and deployment dependencies become harder to manage.

### Why can `snail-common-bom` not inherit from `snail-common`?

Because the root `pom.xml` imports `snail-common-bom`.  
If `snail-common-bom` is also linked back into the same parent chain, Maven reports an `import cycle`.

## License

MIT
