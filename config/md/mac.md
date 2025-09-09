### 技术版本
```
spring cloud alibab 2025 -> nacos-2.2.0
```

---

### Mac启动命令
##### 开机启动mysql
```shell
sudo /usr/local/mysql/support-files/mysql.server start
```
#### 开机启动nacos-2.3.2
```shell
sudo /Users/ansir/work/nacos/nacos-2.2.0/bin/startup.sh -m standalone
```

---

### Redis创建用户
##### 1. 创建只读用户（密码为'readonly123'）
```redis
ACL SETUSER reader on >readonly123 ~* -@all +GET +HGET +LRANGE +SMEMBERS +PING +INFO
```
##### 2. 验证用户权限
```redis
AUTH reader readonly123
```
##### 3. 测试权限（应成功）
```redis
127.0.0.1:6379> GET any_key
```
##### 4. 测试写入（应失败）
```redis
127.0.0.1:6379> SET test 123
(error) NOPERM
```
##### 5. 查看用户
```redis
ACL LIST
```
##### 6. 删除用户
```redis
ACL DELUSER reader
```
-2