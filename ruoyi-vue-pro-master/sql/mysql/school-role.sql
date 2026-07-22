-- ============================================
-- 学校模块 - 角色权限升级
-- ============================================

-- 确保使用 UTF-8 编码连接，避免中文乱码
SET NAMES utf8mb4;
-- 教职工表新增角色、班级字段
ALTER TABLE school_staff ADD COLUMN IF NOT EXISTS role tinyint DEFAULT 0 COMMENT '角色(0:教师,1:班主任,2:学院院长,3:校长)' AFTER college_id;
ALTER TABLE school_staff ADD COLUMN IF NOT EXISTS class_id bigint DEFAULT NULL COMMENT '负责班级ID（班主任角色时使用）' AFTER role;
