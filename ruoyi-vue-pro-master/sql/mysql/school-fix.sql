-- ============================================
-- 学校管理模块 - 修复脚本 (已有表的增量修复)
-- ============================================

-- 确保使用 UTF-8 编码连接，避免中文乱码
SET NAMES utf8mb4;

-- 1. 添加 tenant_id 列 (如果还未添加)
ALTER TABLE school_college ADD COLUMN IF NOT EXISTS tenant_id bigint NOT NULL DEFAULT 1 COMMENT '租户ID' AFTER deleted;
ALTER TABLE school_major   ADD COLUMN IF NOT EXISTS tenant_id bigint NOT NULL DEFAULT 1 COMMENT '租户ID' AFTER deleted;
ALTER TABLE school_class   ADD COLUMN IF NOT EXISTS tenant_id bigint NOT NULL DEFAULT 1 COMMENT '租户ID' AFTER deleted;
ALTER TABLE school_staff   ADD COLUMN IF NOT EXISTS tenant_id bigint NOT NULL DEFAULT 1 COMMENT '租户ID' AFTER deleted;
ALTER TABLE school_student ADD COLUMN IF NOT EXISTS tenant_id bigint NOT NULL DEFAULT 1 COMMENT '租户ID' AFTER deleted;

-- 2. 添加外键索引
ALTER TABLE school_major   ADD INDEX IF NOT EXISTS idx_college_id (college_id);
ALTER TABLE school_class   ADD INDEX IF NOT EXISTS idx_major_id (major_id);
ALTER TABLE school_staff   ADD INDEX IF NOT EXISTS idx_college_id (college_id);
ALTER TABLE school_student ADD INDEX IF NOT EXISTS idx_class_id (class_id);
