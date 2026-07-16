-- ============================================
-- 学校管理模块 - 菜单和权限初始化
-- 运行前请确保 school.sql 中的表已创建
-- ============================================

-- 1. 学校管理（顶级目录）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES (5000, '学校管理', '', 1, 30, 0, '/school', 'ep:school', NULL, NULL, 0);

-- 2. 学院管理（菜单页）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES (5001, '学院管理', '', 2, 1, 5000, 'college', 'ep:office-building', 'school/college/index', 'SchoolCollege', 0);
-- 学院管理 - 按钮权限
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50010, '学院查询', 'school:college:query', 3, 1, 5001, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50011, '学院创建', 'school:college:create', 3, 2, 5001, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50012, '学院更新', 'school:college:update', 3, 3, 5001, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50013, '学院删除', 'school:college:delete', 3, 4, 5001, '', '', '', 0);

-- 3. 专业管理（菜单页）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES (5002, '专业管理', '', 2, 2, 5000, 'major', 'ep:reading', 'school/major/index', 'SchoolMajor', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50020, '专业查询', 'school:major:query', 3, 1, 5002, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50021, '专业创建', 'school:major:create', 3, 2, 5002, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50022, '专业更新', 'school:major:update', 3, 3, 5002, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50023, '专业删除', 'school:major:delete', 3, 4, 5002, '', '', '', 0);

-- 4. 班级管理（菜单页）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES (5003, '班级管理', '', 2, 3, 5000, 'class', 'ep:grid', 'school/class/index', 'SchoolClass', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50030, '班级查询', 'school:class:query', 3, 1, 5003, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50031, '班级创建', 'school:class:create', 3, 2, 5003, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50032, '班级更新', 'school:class:update', 3, 3, 5003, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50033, '班级删除', 'school:class:delete', 3, 4, 5003, '', '', '', 0);

-- 5. 教职工管理（菜单页）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES (5004, '教职工管理', '', 2, 4, 5000, 'staff', 'ep:user-filled', 'school/staff/index', 'SchoolStaff', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50040, '教职工查询', 'school:staff:query', 3, 1, 5004, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50041, '教职工创建', 'school:staff:create', 3, 2, 5004, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50042, '教职工更新', 'school:staff:update', 3, 3, 5004, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50043, '教职工删除', 'school:staff:delete', 3, 4, 5004, '', '', '', 0);

-- 6. 学生管理（菜单页）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`)
VALUES (5005, '学生管理', '', 2, 5, 5000, 'student', 'ep:user', 'school/student/index', 'SchoolStudent', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50050, '学生查询', 'school:student:query', 3, 1, 5005, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50051, '学生创建', 'school:student:create', 3, 2, 5005, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50052, '学生更新', 'school:student:update', 3, 3, 5005, '', '', '', 0);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `component_name`, `status`)
VALUES (50053, '学生删除', 'school:student:delete', 3, 4, 5005, '', '', '', 0);

-- 7. 给超级管理员(角色ID=1)分配学校管理所有菜单权限
INSERT INTO `system_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `system_menu` WHERE id BETWEEN 5000 AND 50053;
