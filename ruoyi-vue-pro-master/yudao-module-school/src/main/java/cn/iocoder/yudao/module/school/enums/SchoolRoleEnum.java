package cn.iocoder.yudao.module.school.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 学校教职工角色枚举
 */
@AllArgsConstructor
@Getter
public enum SchoolRoleEnum {

    TEACHER(0, "教师"),
    HEAD_TEACHER(1, "班主任"),
    DEAN(2, "学院院长"),
    PRINCIPAL(3, "校长");

    private final Integer value;
    private final String name;

}
