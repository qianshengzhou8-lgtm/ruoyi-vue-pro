-- ============================================
-- 学校用户管理模块 - 初始化表结构
-- ============================================

-- 确保使用 UTF-8 编码连接，避免中文乱码
SET NAMES utf8mb4;

-- 1. 学院表
CREATE TABLE IF NOT EXISTS school_college (
    id          bigint        NOT NULL AUTO_INCREMENT COMMENT '学院ID',
    name        varchar(100)  NOT NULL              COMMENT '学院名称',
    sort        int           DEFAULT 0             COMMENT '排序',
    status      tinyint       DEFAULT 0             COMMENT '状态(0:开启,1:禁用)',
    creator     varchar(64)   DEFAULT ''            COMMENT '创建者',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     varchar(64)   DEFAULT ''            COMMENT '更新者',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT 0    COMMENT '是否删除',
    tenant_id   bigint        NOT NULL DEFAULT 1    COMMENT '租户ID',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院表';

-- 2. 专业表
CREATE TABLE IF NOT EXISTS school_major (
    id          bigint        NOT NULL AUTO_INCREMENT COMMENT '专业ID',
    name        varchar(100)  NOT NULL              COMMENT '专业名称',
    college_id  bigint        NOT NULL              COMMENT '所属学院ID',
    sort        int           DEFAULT 0             COMMENT '排序',
    status      tinyint       DEFAULT 0             COMMENT '状态(0:开启,1:禁用)',
    creator     varchar(64)   DEFAULT ''            COMMENT '创建者',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     varchar(64)   DEFAULT ''            COMMENT '更新者',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT 0    COMMENT '是否删除',
    tenant_id   bigint        NOT NULL DEFAULT 1    COMMENT '租户ID',
    PRIMARY KEY (id),
    INDEX idx_college_id (college_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业表';

-- 3. 班级表
CREATE TABLE IF NOT EXISTS school_class (
    id          bigint        NOT NULL AUTO_INCREMENT COMMENT '班级ID',
    name        varchar(100)  NOT NULL              COMMENT '班级名称',
    major_id    bigint        NOT NULL              COMMENT '所属专业ID',
    sort        int           DEFAULT 0             COMMENT '排序',
    status      tinyint       DEFAULT 0             COMMENT '状态(0:开启,1:禁用)',
    creator     varchar(64)   DEFAULT ''            COMMENT '创建者',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     varchar(64)   DEFAULT ''            COMMENT '更新者',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT 0    COMMENT '是否删除',
    tenant_id   bigint        NOT NULL DEFAULT 1    COMMENT '租户ID',
    PRIMARY KEY (id),
    INDEX idx_major_id (major_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 4. 教职工表
CREATE TABLE IF NOT EXISTS school_staff (
    id          bigint        NOT NULL AUTO_INCREMENT COMMENT '教职工ID',
    name        varchar(100)  NOT NULL              COMMENT '姓名',
    username    varchar(64)   NOT NULL              COMMENT '登录账号',
    password    varchar(128)  NOT NULL              COMMENT '登录密码',
    mobile      varchar(20)   DEFAULT ''            COMMENT '手机号',
    college_id  bigint        NOT NULL              COMMENT '所属学院ID',
    status      tinyint       DEFAULT 0             COMMENT '状态(0:开启,1:禁用)',
    creator     varchar(64)   DEFAULT ''            COMMENT '创建者',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     varchar(64)   DEFAULT ''            COMMENT '更新者',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT 0    COMMENT '是否删除',
    tenant_id   bigint        NOT NULL DEFAULT 1    COMMENT '租户ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    INDEX idx_college_id (college_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工表';

-- 5. 学生表
CREATE TABLE IF NOT EXISTS school_student (
    id          bigint        NOT NULL AUTO_INCREMENT COMMENT '学生ID',
    name        varchar(100)  NOT NULL              COMMENT '姓名',
    username    varchar(64)   NOT NULL              COMMENT '登录账号',
    password    varchar(128)  NOT NULL              COMMENT '登录密码',
    mobile      varchar(20)   DEFAULT ''            COMMENT '手机号',
    class_id    bigint        NOT NULL              COMMENT '所属班级ID',
    status      tinyint       DEFAULT 0             COMMENT '状态(0:开启,1:禁用)',
    creator     varchar(64)   DEFAULT ''            COMMENT '创建者',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     varchar(64)   DEFAULT ''            COMMENT '更新者',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT 0    COMMENT '是否删除',
    tenant_id   bigint        NOT NULL DEFAULT 1    COMMENT '租户ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    INDEX idx_class_id (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';
