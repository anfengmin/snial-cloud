-- 调度中心菜单改造前备份脚本
-- 说明
-- 1. 执行修复脚本前先执行本脚本
-- 2. 备份范围包含旧调度中心菜单、旧外链官网菜单、以及所有 job:* 权限菜单

CREATE TABLE IF NOT EXISTS sys_menu_backup_20260426 LIKE sys_menu;

INSERT IGNORE INTO sys_menu_backup_20260426
SELECT *
FROM sys_menu
WHERE id IN (
    4, 400, 401, 402, 403, 410, 411, 412, 413, 414, 415, 416, 417, 420, 421, 422, 423, 424, 430, 431, 432, 433,
    700, 701, 702, 703, 704, 710, 711, 712, 713, 714, 715, 716, 717, 720, 721, 722, 723, 724, 730, 731, 732, 733
)
OR parent_id IN (
    4, 400, 401, 402, 403, 700, 701, 702, 703, 704
)
OR path = 'job'
OR perms LIKE 'job:%';
