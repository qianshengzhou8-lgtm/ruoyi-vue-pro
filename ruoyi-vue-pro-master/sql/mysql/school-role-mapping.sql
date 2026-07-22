-- ============================================
-- 学校模块 - 系统角色定义与权限映射
-- 修复教职工默认角色为超级管理员的安全漏洞
-- ============================================

-- 确保使用 UTF-8 编码连接，避免中文乱码
SET NAMES utf8mb4;

-- 1. 创建学校专用的系统角色
-- 注意：角色ID使用5000+范围，避免与系统角色冲突

-- 教师：只有查询权限
INSERT INTO `system_role` (`id`, `name`, `code`, `sort`, `data_scope`, `data_scope_dept_ids`, `status`, `type`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
VALUES (5001, '学校教师', 'school_teacher', 1, 2, '', 0, 2, '学校教师角色：仅拥有查询权限', 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `code` = VALUES(`code`);

-- 班主任：拥有查询和管理本班学生的权限
INSERT INTO `system_role` (`id`, `name`, `code`, `sort`, `data_scope`, `data_scope_dept_ids`, `status`, `type`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
VALUES (5002, '班主任', 'school_head_teacher', 2, 2, '', 0, 2, '班主任角色：管理本班学生', 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `code` = VALUES(`code`);

-- 学院院长：拥有本学院所有管理权限
INSERT INTO `system_role` (`id`, `name`, `code`, `sort`, `data_scope`, `data_scope_dept_ids`, `status`, `type`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
VALUES (5003, '学院院长', 'school_dean', 3, 2, '', 0, 2, '学院院长角色：管理本学院数据', 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `code` = VALUES(`code`);

-- 校长：拥有学校全部管理权限
INSERT INTO `system_role` (`id`, `name`, `code`, `sort`, `data_scope`, `data_scope_dept_ids`, `status`, `type`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
VALUES (5004, '学校校长', 'school_principal', 4, 1, '', 0, 2, '学校校长角色：管理全校数据', 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `code` = VALUES(`code`);

-- 2. 分配菜单权限给各角色

-- 教师角色 (5001): 查询权限（需包含 type=2 菜单页，否则侧边栏不显示）
-- 学校管理目录
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5001, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id = 5000
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 6个管理页(type=2)，必须分配，否则查询按钮在侧边栏不可见
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5001, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (5001, 5002, 5003, 5004, 5005, 5006)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 6个查询按钮(type=3)，控制页内只有查询功能
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5001, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50010, 50020, 50030, 50040, 50050, 50060)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 班主任角色 (5002): 学生管理全部权限 + 课程查询
-- 继承教师的所有权限(含所有管理页+查询按钮)
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5002, menu_id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_role_menu` WHERE role_id = 5001
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 学生管理 CRUD 全部按钮
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5002, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50051, 50052, 50053)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 学院院长角色 (5003): 学院级别管理权限（所有管理页 + CRUD）
-- 继承班主任的所有权限
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5003, menu_id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_role_menu` WHERE role_id = 5002
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 学院管理 CRUD
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5003, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50011, 50012, 50013)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 专业管理 CRUD
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5003, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50021, 50022, 50023)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 班级管理 CRUD
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5003, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50031, 50032, 50033)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 教职工管理 CRUD
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5003, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50041, 50042, 50043)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 课程管理 CRUD
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5003, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50061, 50062, 50063)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 导入导出按钮（院长可导入导出本院数据）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5003, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id IN (50014, 50015, 50024, 50025, 50034, 50035, 50044, 50045, 50054, 50055)
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 校长角色 (5004): 学校全部管理权限
-- 分配所有学校管理菜单
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 5004, id, 'admin', NOW(), 'admin', NOW(), b'0', 1 FROM `system_menu` WHERE id BETWEEN 5000 AND 5999
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;