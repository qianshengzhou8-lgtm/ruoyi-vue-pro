package cn.iocoder.yudao.module.school.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * School 错误码枚举类
 *
 * school 系统，使用 1-008-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 学院模块 1-008-001-000 ==========
    ErrorCode COLLEGE_NOT_EXISTS = new ErrorCode(1_008_001_000, "学院不存在");
    ErrorCode COLLEGE_HAS_MAJORS = new ErrorCode(1_008_001_001, "学院下存在专业，无法删除");

    // ========== 专业模块 1-008-002-000 ==========
    ErrorCode MAJOR_NOT_EXISTS = new ErrorCode(1_008_002_000, "专业不存在");
    ErrorCode MAJOR_HAS_CLASSES = new ErrorCode(1_008_002_001, "专业下存在班级，无法删除");

    // ========== 班级模块 1-008-003-000 ==========
    ErrorCode CLASS_NOT_EXISTS = new ErrorCode(1_008_003_000, "班级不存在");
    ErrorCode CLASS_HAS_STUDENTS = new ErrorCode(1_008_003_001, "班级下存在学生，无法删除");

    // ========== 教职工模块 1-008-004-000 ==========
    ErrorCode STAFF_NOT_EXISTS = new ErrorCode(1_008_004_000, "教职工不存在");
    ErrorCode STAFF_USERNAME_DUPLICATE = new ErrorCode(1_008_004_001, "教职工账号已存在");
    ErrorCode STAFF_PASSWORD_BLANK = new ErrorCode(1_008_004_002, "教职工密码不能为空");
    ErrorCode STAFF_BAD_CREDENTIALS = new ErrorCode(1_008_004_003, "账号或密码错误");
    ErrorCode STAFF_CLASS_REQUIRED = new ErrorCode(1_008_004_004, "班主任必须指定负责班级");

    // ========== 学生模块 1-008-005-000 ==========
    ErrorCode STUDENT_NOT_EXISTS = new ErrorCode(1_008_005_000, "学生不存在");
    ErrorCode STUDENT_USERNAME_DUPLICATE = new ErrorCode(1_008_005_001, "学生账号已存在");
    ErrorCode STUDENT_PASSWORD_BLANK = new ErrorCode(1_008_005_002, "学生密码不能为空");
    ErrorCode STUDENT_BAD_CREDENTIALS = new ErrorCode(1_008_005_003, "账号或密码错误");

    // ========== 课程模块 1-008-006-000 ==========
    ErrorCode COURSE_NOT_EXISTS = new ErrorCode(1_008_006_000, "课程不存在");
    ErrorCode COURSE_ALREADY_SELECTED = new ErrorCode(1_008_006_001, "已选过该课程");
    ErrorCode COURSE_CAPACITY_FULL = new ErrorCode(1_008_006_002, "课程已满");
    ErrorCode COURSE_NOT_SELECTED = new ErrorCode(1_008_006_003, "未选择该课程");

    // ========== 数据权限模块 1-008-007-000 ==========
    ErrorCode DATA_PERMISSION_DENIED = new ErrorCode(1_008_007_000, "无权访问该数据");

}
