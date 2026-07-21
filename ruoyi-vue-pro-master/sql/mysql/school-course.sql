-- ============================================
-- 学校模块 - 课程管理
-- ============================================
CREATE TABLE IF NOT EXISTS school_course (
    id          bigint        NOT NULL AUTO_INCREMENT COMMENT '课程ID',
    name        varchar(100)  NOT NULL              COMMENT '课程名称',
    type        tinyint       NOT NULL DEFAULT 0    COMMENT '类型(0:必修课,1:选修课)',
    capacity    int           DEFAULT NULL          COMMENT '选修课人数上限(null=不限)',
    teacher_id  bigint        NOT NULL              COMMENT '授课教师ID(关联school_staff)',
    college_id  bigint        DEFAULT NULL          COMMENT '所属学院ID',
    sort        int           DEFAULT 0             COMMENT '排序',
    status      tinyint       DEFAULT 0             COMMENT '状态(0:开启,1:禁用)',
    creator     varchar(64)   DEFAULT ''            COMMENT '创建者',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater     varchar(64)   DEFAULT ''            COMMENT '更新者',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT 0    COMMENT '是否删除',
    tenant_id   bigint        NOT NULL DEFAULT 1    COMMENT '租户ID',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

CREATE TABLE IF NOT EXISTS school_course_selection (
    id          bigint        NOT NULL AUTO_INCREMENT COMMENT '选课记录ID',
    course_id   bigint        NOT NULL              COMMENT '课程ID',
    student_id  bigint        NOT NULL              COMMENT '学生ID',
    creator     varchar(64)   DEFAULT ''            COMMENT '创建者',
    create_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    updater     varchar(64)   DEFAULT ''            COMMENT '更新者',
    update_time datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     bit(1)        NOT NULL DEFAULT 0    COMMENT '是否删除',
    tenant_id   bigint        NOT NULL DEFAULT 1    COMMENT '租户ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_course_student (course_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课记录表';
