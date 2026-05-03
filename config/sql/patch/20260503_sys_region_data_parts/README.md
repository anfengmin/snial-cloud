# sys_region 分片导入说明

这个目录把 `20260503_sys_region_full_data.sql` 按 500 行一片拆分，适合数据库客户端逐个执行。

推荐命令行导入方式：

```bash
mysql --default-character-set=utf8mb4 -u用户名 -p 数据库名 < ../20260503_create_sys_region_and_user_address.sql
mysql --default-character-set=utf8mb4 -u用户名 -p 数据库名 < ../20260503_sys_region_full_data.sql
```

如果只能粘贴执行，请按文件名顺序执行 `20260503_sys_region_data_part_01.sql` 到最后一个分片。
