-- ============================================
-- 学校模块 - 测试数据
-- 层级：学院 → 专业 → 班级 → 学生，每班一个班主任
--
-- 数据权限验证：
--   admin     → 全校数据
--   profzhang → 校长，全校数据
--   yuanzhang → 信息工程学院院长，只看本院
--   banzhuren → 计科2401班班主任，只看本班学生
--   jiaoshi   → 教师，无数据权限
-- ============================================
SET NAMES utf8mb4;

-- ============================================
-- 1. 清理 e2e 测试垃圾数据
-- ============================================
DELETE FROM school_student WHERE name LIKE 'e2e_%' OR username LIKE 'e2e_%';
DELETE FROM school_class   WHERE name LIKE 'e2e_%';
DELETE FROM school_major   WHERE name LIKE 'e2e_%';
DELETE FROM school_college WHERE name LIKE 'e2e_%' OR name = 'test';
DELETE FROM school_staff   WHERE username LIKE 'e2e_%' OR username IN ('debugstaff99','t999','lifang','wangqiang','zhangprof2','zhangming1','zhangming2','t1','debugstaff');

-- ============================================
-- 2. 学院（清晰命名）
-- ============================================
-- 保留：id=1 信息工程学院, id=5 商学院, id=21 大数据学院
-- 删除重复/脏数据
DELETE FROM school_college WHERE id IN (3,4,6,7,8,12,13,14,15,16,18,19,20);
-- 确保现有学院名称正确
UPDATE school_college SET name = '信息工程学院' WHERE id = 1 AND name != '信息工程学院';
UPDATE school_college SET name = '商学院'         WHERE id = 5 AND name != '商学院';
UPDATE school_college SET name = '大数据学院'     WHERE id = 21 AND name != '大数据学院';

-- ============================================
-- 3. 删除脏专业/班级/学生
-- ============================================
DELETE FROM school_student WHERE id IN (20,21,22,23);
DELETE FROM school_class   WHERE id IN (2,3,4,5,6);
DELETE FROM school_major   WHERE id IN (2,3,4,5);

-- ============================================
-- 4. 专业（每个学院下属专业）
-- ============================================
-- 信息工程学院(id=1) 的专业
UPDATE school_major SET name = '计算机科学与技术', college_id = 1 WHERE id = 1;
INSERT INTO school_major (id, name, college_id, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES (2, '软件工程', 1, 2, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), college_id = VALUES(college_id);

-- 商学院(id=5) 的专业
INSERT INTO school_major (id, name, college_id, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES (3, '工商管理', 5, 1, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), college_id = VALUES(college_id);

-- 大数据学院(id=21) 的专业
INSERT INTO school_major (id, name, college_id, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES (4, '数据科学与大数据技术', 21, 1, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), college_id = VALUES(college_id);

-- ============================================
-- 5. 班级（每个专业下属班级）
-- ============================================
-- 计算机科学与技术(id=1) → 计科2401班、计科2402班
UPDATE school_class SET name = '计科2401班', major_id = 1 WHERE id = 1;
INSERT INTO school_class (id, name, major_id, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES (2, '计科2402班', 1, 2, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), major_id = VALUES(major_id);

-- 软件工程(id=2) → 软工2401班
INSERT INTO school_class (id, name, major_id, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES (3, '软工2401班', 2, 1, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), major_id = VALUES(major_id);

-- 工商管理(id=3) → 工商2401班
INSERT INTO school_class (id, name, major_id, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES (4, '工商2401班', 3, 1, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), major_id = VALUES(major_id);

-- 大数据(id=4) → 大数据2401班
INSERT INTO school_class (id, name, major_id, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES (5, '大数据2401班', 4, 1, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), major_id = VALUES(major_id);

-- ============================================
-- 6. 学生（每个班级有学生）
-- ============================================
-- 计科2401班(class_id=1) 的学生 → banzhuren 只能看这班
INSERT INTO school_student (id, name, username, password, mobile, class_id, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES
(1, '张三', 'zhangsan', '$2a$04$Tv6qoYDECBlu/SKQCoslzeBlf.CqypWEeIDp9ZAltGQ36zAfD7edm', '13900001001', 1, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1),
(2, '李四', 'lisi',   '$2a$04$Tv6qoYDECBlu/SKQCoslzeBlf.CqypWEeIDp9ZAltGQ36zAfD7edm', '13900001002', 1, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1),
(3, '王五', 'wangwu', '$2a$04$Tv6qoYDECBlu/SKQCoslzeBlf.CqypWEeIDp9ZAltGQ36zAfD7edm', '13900001003', 2, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1),
(4, '赵六', 'zhaoliu','$2a$04$Tv6qoYDECBlu/SKQCoslzeBlf.CqypWEeIDp9ZAltGQ36zAfD7edm', '13900001004', 3, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1),
(5, '孙七', 'sunqi',  '$2a$04$Tv6qoYDECBlu/SKQCoslzeBlf.CqypWEeIDp9ZAltGQ36zAfD7edm', '13900001005', 4, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1),
(6, '周八', 'zhouba', '$2a$04$Tv6qoYDECBlu/SKQCoslzeBlf.CqypWEeIDp9ZAltGQ36zAfD7edm', '13900001006', 5, 0, 'admin', NOW(), 'admin', NOW(), b'0', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name), class_id = VALUES(class_id);

-- ============================================
-- 7. 清理旧学生数据
-- ============================================
DELETE FROM school_student WHERE name IN ('孙同学','周同学','吴同学','TestStudent') OR username = 'teststudent';

-- ============================================
-- 8. 验证数据层级
-- ============================================
SELECT '=== 学院 ===' AS '';
SELECT id, name FROM school_college WHERE deleted = b'0' ORDER BY id;
SELECT '=== 专业 ===' AS '';
SELECT m.id, m.name, c.name as college FROM school_major m JOIN school_college c ON m.college_id = c.id WHERE m.deleted = b'0' ORDER BY m.id;
SELECT '=== 班级 ===' AS '';
SELECT cl.id, cl.name, m.name as major FROM school_class cl JOIN school_major m ON cl.major_id = m.id WHERE cl.deleted = b'0' ORDER BY cl.id;
SELECT '=== 学生 ===' AS '';
SELECT s.id, s.name, cl.name as class FROM school_student s JOIN school_class cl ON s.class_id = cl.id WHERE s.deleted = b'0' ORDER BY s.id;
