-- 调度中心标准菜单初始化脚本
-- 说明
-- 1. 该脚本用于全新环境初始化
-- 2. 一级菜单固定使用 ID=4
-- 3. 页面菜单统一使用 *:list 权限

-- 一级目录
INSERT INTO sys_menu (
    id, menu_name, parent_id, order_num, path, name_en, component, query_param,
    is_frame, is_cache, menu_type, visible, status, deleted, perms, icon,
    create_by, create_time, update_by, update_time, remark
) VALUES (
    4, '调度中心', 0, 4, 'job', NULL, '', '',
    1, 0, 'M', 0, 0, 0, '', 'job',
    'admin', sysdate(), '', NULL, '调度中心目录'
);

-- 二级菜单
INSERT INTO sys_menu (
    id, menu_name, parent_id, order_num, path, name_en, component, query_param,
    is_frame, is_cache, menu_type, visible, status, deleted, perms, icon,
    create_by, create_time, update_by, update_time, remark
) VALUES
    (400, '调度概览', 4, 1, 'dashboard', NULL, 'job/dashboard/index', '', 1, 0, 'C', 0, 0, 0, 'job:dashboard:list', 'dashboard', 'admin', sysdate(), '', NULL, '调度概览菜单'),
    (401, '任务管理', 4, 2, 'info', NULL, 'job/info/index', '', 1, 0, 'C', 0, 0, 0, 'job:info:list', 'clipboard', 'admin', sysdate(), '', NULL, '任务管理菜单'),
    (402, '执行器管理', 4, 3, 'group', NULL, 'job/group/index', '', 1, 0, 'C', 0, 0, 0, 'job:group:list', 'server', 'admin', sysdate(), '', NULL, '执行器管理菜单'),
    (403, '调度日志', 4, 4, 'log', NULL, 'job/log/index', '', 1, 0, 'C', 0, 0, 0, 'job:log:list', 'form', 'admin', sysdate(), '', NULL, '调度日志菜单');

-- 任务管理按钮
INSERT INTO sys_menu (
    id, menu_name, parent_id, order_num, path, name_en, component, query_param,
    is_frame, is_cache, menu_type, visible, status, deleted, perms, icon,
    create_by, create_time, update_by, update_time, remark
) VALUES
    (410, '任务查询', 401, 1, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:query', '#', 'admin', sysdate(), '', NULL, ''),
    (411, '任务新增', 401, 2, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:add', '#', 'admin', sysdate(), '', NULL, ''),
    (412, '任务修改', 401, 3, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:edit', '#', 'admin', sysdate(), '', NULL, ''),
    (413, '任务删除', 401, 4, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:remove', '#', 'admin', sysdate(), '', NULL, ''),
    (414, '任务启动', 401, 5, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:start', '#', 'admin', sysdate(), '', NULL, ''),
    (415, '任务停止', 401, 6, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:stop', '#', 'admin', sysdate(), '', NULL, ''),
    (416, '手动触发', 401, 7, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:trigger', '#', 'admin', sysdate(), '', NULL, ''),
    (417, '预览下次执行', 401, 8, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:info:nextTriggerTime', '#', 'admin', sysdate(), '', NULL, '');

-- 执行器管理按钮
INSERT INTO sys_menu (
    id, menu_name, parent_id, order_num, path, name_en, component, query_param,
    is_frame, is_cache, menu_type, visible, status, deleted, perms, icon,
    create_by, create_time, update_by, update_time, remark
) VALUES
    (420, '执行器查询', 402, 1, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:group:query', '#', 'admin', sysdate(), '', NULL, ''),
    (421, '执行器新增', 402, 2, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:group:add', '#', 'admin', sysdate(), '', NULL, ''),
    (422, '执行器修改', 402, 3, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:group:edit', '#', 'admin', sysdate(), '', NULL, ''),
    (423, '执行器删除', 402, 4, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:group:remove', '#', 'admin', sysdate(), '', NULL, ''),
    (424, '执行器详情', 402, 5, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:group:detail', '#', 'admin', sysdate(), '', NULL, '');

-- 调度日志按钮
INSERT INTO sys_menu (
    id, menu_name, parent_id, order_num, path, name_en, component, query_param,
    is_frame, is_cache, menu_type, visible, status, deleted, perms, icon,
    create_by, create_time, update_by, update_time, remark
) VALUES
    (430, '日志查询', 403, 1, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:log:query', '#', 'admin', sysdate(), '', NULL, ''),
    (431, '日志详情', 403, 2, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:log:detail', '#', 'admin', sysdate(), '', NULL, ''),
    (432, '终止执行', 403, 3, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:log:kill', '#', 'admin', sysdate(), '', NULL, ''),
    (433, '清理日志', 403, 4, '', NULL, '', '', 1, 0, 'F', 0, 0, 0, 'job:log:clear', '#', 'admin', sysdate(), '', NULL, '');
