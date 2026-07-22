-- ============================================
-- 学校模块 - 修复数据权限：禁用系统部门权限过滤
--
-- 问题：学校角色 data_scope=2（自定义部门）但 data_scope_dept_ids 为空，
-- 导致 DeptDataPermissionRule 对所有 SQL 注入 "WHERE null = null"，
-- 所有数据库查询返回空结果。
--
-- 解决：将学校角色的 data_scope 设为 1（全部数据权限），
-- 学校模块的实际数据权限由 SchoolDataPermissionUtil 在应用层控制。
-- ============================================

SET NAMES utf8mb4;

-- 教师、班主任、院长 → 全部数据（data_scope=1）
UPDATE `system_role` SET `data_scope` = 1 WHERE `id` IN (5001, 5002, 5003);

-- 验证
SELECT id, name, code, data_scope FROM `system_role` WHERE id IN (5001, 5002, 5003, 5004);
