drop table if exists sys_user;
create table sys_user
(
    id          bigint(20)  not null comment '用户ID',
    dept_id     bigint(20)   default null comment '部门ID',
    user_code   varchar(30) not null comment '用户账号',
    user_name   varchar(30) not null comment '用户名称',
    nick_name   varchar(30) not null comment '用户昵称',
    user_type   varchar(10)  default 'sys_user' comment '用户类型（sys_user系统用户）',
    email       varchar(50)  default '' comment '用户邮箱',
    phone_no    varchar(11)  default '' comment '手机号码',
    sex         char(1)      default '0' comment '用户性别（0男 1女 2未知）',
    avatar      varchar(100) default '' comment '头像地址',
    pass_word   varchar(100) default '' comment '密码',
    status      char(1)      default '0' comment '帐号状态（0正常 1停用）',
    deleted     char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    login_ip    varchar(128) default '' comment '最后登录IP',
    login_date  datetime comment '最后登录时间',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb comment = '用户';


drop table if exists sys_dept;
create table sys_dept
(
    id          bigint(20) not null comment '部门id',
    parent_id   bigint(20)   default 0 comment '父部门id',
    ancestors   varchar(500) default '' comment '祖级列表',
    dept_name   varchar(30)  default '' comment '部门名称',
    order_no    int(4)       default 0 comment '显示顺序',
    leader      varchar(20)  default null comment '负责人',
    phone       varchar(11)  default null comment '联系电话',
    email       varchar(50)  default null comment '邮箱',
    status      char(1)      default '0' comment '部门状态（0正常 1停用）',
    deleted     char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb comment = '部门';
