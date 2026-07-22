-- 确保使用 UTF-8 编码连接，避免中文乱码
SET NAMES utf8mb4;

-- 7. 课程管理（菜单页，后台）
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status)
VALUES (5006, '课程管理', '', 2, 6, 5000, 'course', 'ep:notebook', 'school/course/index', 'SchoolCourse', 0);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, component, component_name, status)
VALUES (50060, '课程查询', 'school:course:query', 3, 1, 5006, '', '', '', 0);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, component, component_name, status)
VALUES (50061, '课程创建', 'school:course:create', 3, 2, 5006, '', '', '', 0);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, component, component_name, status)
VALUES (50062, '课程更新', 'school:course:update', 3, 3, 5006, '', '', '', 0);
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, component, component_name, status)
VALUES (50063, '课程删除', 'school:course:delete', 3, 4, 5006, '', '', '', 0);

-- 8. 给超级管理员(角色ID=1)分配课程管理菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT 1, id FROM system_menu WHERE id BETWEEN 5006 AND 50063;
